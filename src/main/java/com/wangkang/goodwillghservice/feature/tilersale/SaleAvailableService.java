package com.wangkang.goodwillghservice.feature.tilersale;

import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorExternalInfo;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorProfile;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository.DistributorProfileRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.order.model.K3SaleOrder;
import com.wangkang.goodwillghservice.dao.goodwillghservice.order.repository.K3SaleOrderRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.product.model.Tile;
import com.wangkang.goodwillghservice.dao.goodwillghservice.product.repository.TileRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.SaleAvailableSnapshot;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecord;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecordType;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository.SaleAvailableSnapshotRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository.TilerSalesRecordRepository;
import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderCloseStatus;
import com.wangkang.goodwillghservice.share.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/** 用于计算剩余可用销量快照的服务 */
@Service
public class SaleAvailableService {


    private final DistributorProfileRepository distributorProfileRepository;
    private final K3SaleOrderRepository k3SaleOrderRepository;
    private final TileRepository tileRepository;
    private final TilerSalesRecordRepository tilerSalesRecordRepository;
    private final SaleAvailableSnapshotRepository saleAvailableSnapshotRepository;

    public SaleAvailableService(DistributorProfileRepository distributorProfileRepository,
                                K3SaleOrderRepository k3SaleOrderRepository, TileRepository tileRepository,
                                TilerSalesRecordRepository tilerSalesRecordRepository,
                                SaleAvailableSnapshotRepository saleAvailableSnapshotRepository) {
        this.distributorProfileRepository = distributorProfileRepository;
        this.k3SaleOrderRepository = k3SaleOrderRepository;
        this.tileRepository = tileRepository;
        this.tilerSalesRecordRepository = tilerSalesRecordRepository;
        this.saleAvailableSnapshotRepository = saleAvailableSnapshotRepository;
    }

    @Auditable(actionType = ActionType.TILER_SALES, actionName = "Take full snapshot for all distributor")
    public void takeFullSnapshot4AllDistributor() {
        // 按经销商分组，一个经销商算一次，后期可以考虑使用多线程模型做
        List<DistributorProfile> allProfile = distributorProfileRepository.findAllWithExternalDistributor();
        for (DistributorProfile distributorProfile : allProfile) {
            takeFullSnapshot4Distributor(distributorProfile);
        }
    }

    public void takeFullSnapshot4Distributor(DistributorProfile distributorProfile) {

        // 获取经销商的基线时间
        OffsetDateTime availableSalesCalFrom = distributorProfile.getAvailableSalesCalFrom();
        // TODO 按经销商分页查询同步到本地的订单，条件是已关闭且关闭时长大于7天的订单
        DistributorExternalInfo externalDistributor = distributorProfile.getExternalDistributor();
        Integer distributorExternalId = externalDistributor.getExternalId();
        OffsetDateTime closeDateBaseLine = DateUtil.currentDateTimeUTC().minusDays(7);

        Map<String, Integer> colorAvailable = computeColorAvailable4Distributor(distributorExternalId,
                availableSalesCalFrom, closeDateBaseLine);
        // 计算经销商已用销量
        UUID distributorId = distributorProfile.getUser().getId();
        computeUsedSales(distributorId, colorAvailable);

        // 构建快照
        List<SaleAvailableSnapshot> toSaveSnapshotList = new ArrayList<>();
        for (Map.Entry<String, Integer> availableEntry : colorAvailable.entrySet()) {
            String color = availableEntry.getKey();
            Integer available = availableEntry.getValue();
            SaleAvailableSnapshot saleAvailableSnapshot = new SaleAvailableSnapshot();
            saleAvailableSnapshot.setDistributorId(distributorId);
            saleAvailableSnapshot.setColor(color);
            saleAvailableSnapshot.setAvailable(available);
            saleAvailableSnapshot.setBasedOn(closeDateBaseLine);
            toSaveSnapshotList.add(saleAvailableSnapshot);
        }
        saleAvailableSnapshotRepository.saveAll(toSaveSnapshotList);
    }

    private void computeUsedSales(UUID distributorId, Map<String, Integer> colorAvailable) {
        for (Map.Entry<String, Integer> colorAvailableEntry : colorAvailable.entrySet()) {
            String color = colorAvailableEntry.getKey();
            Integer quantity = colorAvailableEntry.getValue();
            int totalDelta = 0;
            int page = 0;
            int size = 500; // 建议 100~500，根据数据库和接口性能调
            Page<TilerSalesRecord> recordPage;
            do {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
                recordPage = tilerSalesRecordRepository.findAllByDistributorIdAndProductColor(distributorId, color,
                        pageable);
                List<TilerSalesRecord> content = recordPage.getContent();
                int delta = content.stream()
                        .mapToInt(r -> {
                            if (r.getRecordType() == TilerSalesRecordType.SALE) {
                                return -r.getQuantity();
                            } else if (r.getRecordType() == TilerSalesRecordType.CANCEL) {
                                return +r.getQuantity();
                            }
                            return 0;
                        })
                        .sum();
                totalDelta += delta;
                page++;
            } while (recordPage.hasNext());
            colorAvailableEntry.setValue(quantity + totalDelta);
        }
    }

    @NotNull
    private Map<String, Integer> computeColorAvailable4Distributor(Integer distributorExternalId,
                                                                   OffsetDateTime availableSalesCalFrom,
                                                                   OffsetDateTime offsetDateTime) {
        int page = 0;
        int size = 500; // 建议 100~500，根据数据库和接口性能调
        Page<K3SaleOrder> orderPage;
        Map<String, Integer> colorAvailable = new HashMap<>();
        // TODO 这段代码在工程上有优化空间，可以尝试先按物料编码排序，再按时间排序，以此按花色分层计算快照，索引也有优化空间
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "closeDate"));
            orderPage = k3SaleOrderRepository.findByCustomerIdAndCloseDateBetweenAndCloseStatus(distributorExternalId,
                    availableSalesCalFrom, offsetDateTime, OrderCloseStatus.CLOSED, pageable);
            // 获取 SKU 列表，只算优等品
            List<K3SaleOrder> orderList = orderPage.getContent()
                    .stream()
                    .filter(order -> !order.getMaterialNumber().endsWith("A3"))
                    .filter(order -> !order.getMaterialNumber().endsWith("C10"))
                    .toList();
            List<String> materialNumberList = orderList.stream()
                    .map(K3SaleOrder::getMaterialNumber)
                    .distinct()
                    .toList();
            // TODO 考虑改 redis 缓存查询
            List<Tile> tileList = tileRepository.findAllByCodeIn(materialNumberList);
            // SKU 列表翻译成花色列表
            Map<String, String> numberMap2Color = tileList.stream()
                    .collect(Collectors.toMap(Tile::getCode, Tile::getColor));
            // 统计这个经销商的花色可用销量
            for (K3SaleOrder k3SaleOrder : orderList) {
                String materialNumber = k3SaleOrder.getMaterialNumber();
                Integer saleQuantity = k3SaleOrder.getQuantity();
                String color = numberMap2Color.getOrDefault(materialNumber, "UNKNOWN");
                Integer availableQty = colorAvailable.computeIfAbsent(color, s -> 0);
                availableQty += saleQuantity;
                colorAvailable.put(color, availableQty);
            }
            page++;
        } while (orderPage.hasNext());
        return colorAvailable;
    }
}
