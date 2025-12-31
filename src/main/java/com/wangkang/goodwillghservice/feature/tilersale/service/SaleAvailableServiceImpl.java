package com.wangkang.goodwillghservice.feature.tilersale.service;

import com.wangkang.goodwillghservice.core.exception.I18nBusinessException;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorExternalInfo;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorProfile;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository.DistributorProfileRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.order.model.K3SaleOrder;
import com.wangkang.goodwillghservice.dao.goodwillghservice.order.repository.K3SaleOrderRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.product.model.Tile;
import com.wangkang.goodwillghservice.dao.goodwillghservice.product.repository.TileRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.ColorDeltaProjection;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.SaleAvailableSnapshot;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository.SaleAvailableSnapshotRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository.TilerSalesRecordRepository;
import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderCloseStatus;
import com.wangkang.goodwillghservice.feature.tilersale.model.SaleAvailableDTO;
import com.wangkang.goodwillghservice.share.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/** 用于计算剩余可用销量快照的服务 */
@Service
public class SaleAvailableServiceImpl implements SaleAvailableService {


    private final DistributorProfileRepository distributorProfileRepository;
    private final K3SaleOrderRepository k3SaleOrderRepository;
    private final TileRepository tileRepository;
    private final TilerSalesRecordRepository tilerSalesRecordRepository;
    private final SaleAvailableSnapshotRepository saleAvailableSnapshotRepository;

    public SaleAvailableServiceImpl(DistributorProfileRepository distributorProfileRepository,
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
    @Override
    public void buildFullSnapshot4AllDistributor() {
        // 按经销商分组，一个经销商算一次，后期可以考虑使用多线程模型做
        List<DistributorProfile> allProfile = distributorProfileRepository.findAllWithExternalDistributor();
        for (DistributorProfile distributorProfile : allProfile) {
            takeFullSnapshot4Distributor(distributorProfile);
        }
    }

    @Auditable(actionType = ActionType.TILER_SALES, actionName = "Take full snapshot for specific distributor")
    @Override
    public void buildFullSnapshot4Distributor(UUID distributorId) {
        DistributorProfile distributorProfile = distributorProfileRepository.findByUserIdWithExternalDistributor(
                distributorId);
        if (distributorProfile == null) {
            throw new IllegalArgumentException("Can't find distributor profile for " + distributorId);
        }
        takeFullSnapshot4Distributor(distributorProfile);
    }

    private void takeFullSnapshot4Distributor(DistributorProfile distributorProfile) {

        // 获取经销商的基线时间
        OffsetDateTime availableSalesCalFrom = distributorProfile.getAvailableSalesCalFrom();
        // 按经销商分页查询同步到本地的订单，条件是已关闭且关闭时长大于7天的订单
        DistributorExternalInfo externalDistributor = distributorProfile.getExternalDistributor();
        Integer distributorExternalId = externalDistributor.getExternalId();
        OffsetDateTime closeDateBaseLine = DateUtil.currentDateTimeUTC().minusDays(7);

        Map<String, Integer> colorAvailable = computeColorAvailable4Distributor(distributorExternalId,
                availableSalesCalFrom, closeDateBaseLine);
        // 计算经销商已用销量
        // 基于序列的计算
        UUID distributorId = distributorProfile.getUser().getId();
        Long maxSeqL = computeUsedSales(distributorId, colorAvailable);
        long maxSeq = maxSeqL != null && maxSeqL >= 0 ? maxSeqL : 0L;

        // 构建快照
        saveNewSnapshot(colorAvailable, distributorId, closeDateBaseLine, maxSeq);
    }

    /**
     * 基于使用记录计算使用量并合并进入可用量
     * @param distributorId  经销商用户 id
     * @param colorAvailable 可用量集合，会被修改
     * @return 本次计算后覆盖的最大序列值，方便下一次调用基于此序列+1进行计算
     */
    private Long computeUsedSales(UUID distributorId, Map<String, Integer> colorAvailable) {
        Long maxSeq = tilerSalesRecordRepository.findMaxSeq(distributorId);
        if (maxSeq == null) {
            return maxSeq;
        }
        List<ColorDeltaProjection> deltas = tilerSalesRecordRepository.sumDeltaGroupByColorBeforeOrEqualSeq(
                distributorId, maxSeq);

        for (ColorDeltaProjection p : deltas) {
            String color = p.getColor();
            int delta = p.getDelta() == null ? 0 : p.getDelta();
            colorAvailable.merge(color, delta, Integer::sum);
        }
        return maxSeq;
    }

    /**
     * 基于使用记录计算使用量并合并进入可用量
     * @param distributorId  经销商用户 id
     * @param fromSeq        从哪个序列号开始计算，闭区间
     * @param colorAvailable 可用量集合，会被修改
     * @return 本次计算后覆盖的最大序列值，方便下一次调用基于此序列+1进行计算
     */
    private Long computeUsedSales(UUID distributorId, long fromSeq, Map<String, Integer> colorAvailable) {
        Long maxSeq = tilerSalesRecordRepository.findMaxSeq(distributorId);
        // 起步序列比最大序列还大，系统内没有更新的记录了，直接返回
        if (maxSeq == null) {
            return null;
        }
        if (fromSeq > maxSeq) {
            return maxSeq;
        }
        List<ColorDeltaProjection> deltas = tilerSalesRecordRepository.sumDeltaGroupByColorAfterOrEqualFromSeqBeforeOrEqualSeq(
                distributorId, fromSeq, maxSeq);
        for (ColorDeltaProjection p : deltas) {
            String color = p.getColor();
            int delta = p.getDelta() == null ? 0 : p.getDelta();
            colorAvailable.merge(color, delta, Integer::sum);
        }
        return maxSeq;
    }

    /**
     * 基于使用记录计算使用量并合并进入可用量
     * @param distributorId 经销商用户 id
     * @param fromSeq       从哪个序列号开始计算，闭区间
     * @return 本次计算后覆盖的最大序列值，方便下一次调用基于此序列+1进行计算
     */
    private Collection<ColorDeltaProjection> getUsedSales(UUID distributorId,
                                                          long fromSeq) {
        Long maxSeq = tilerSalesRecordRepository.findMaxSeq(distributorId);
        // 起步序列比最大序列还大，系统内没有更新的记录了，直接返回
        if (maxSeq == null || fromSeq > maxSeq) {
            return Collections.emptyList();
        }

        return tilerSalesRecordRepository.sumDeltaGroupByColorAfterOrEqualFromSeqBeforeOrEqualSeq(
                distributorId, fromSeq, maxSeq);
    }

    /**
     * 为某个经销商计算花色可用量，基于金蝶云订单的裸计算，没有算消耗量
     * 注意时间上是左闭右开区间
     * @param distributorExternalId 外部经销商 ID
     * @param availableSalesCalFrom 可用量计算基线
     * @param closeDateTime         关闭时间
     * @return 可用量信息 color -> quantity
     */
    @NotNull
    private Map<String, Integer> computeColorAvailable4Distributor(Integer distributorExternalId,
                                                                   OffsetDateTime availableSalesCalFrom,
                                                                   OffsetDateTime closeDateTime) {
        int page = 0;
        int size = 500; // 建议 100~500，根据数据库和接口性能调
        Page<K3SaleOrder> orderPage;
        Map<String, Integer> colorAvailable = new HashMap<>();
        // TODO 这段代码在工程上有优化空间，可以尝试先按物料编码排序，再按时间排序，以此按花色分层计算快照，索引也有优化空间
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "closeDate"));
            orderPage = k3SaleOrderRepository.findByCustomerIdAndCloseDateGreaterThanEqualAndCloseDateLessThanAndCloseStatus(
                    distributorExternalId, availableSalesCalFrom, closeDateTime, OrderCloseStatus.CLOSED, pageable);
            List<K3SaleOrder> orderList = filterGradeA(orderPage.getContent());
            Map<String, String> numberMap2Color = computeCode2ColorMap(orderList);
            // 统计这个经销商的花色可用销量
            computeColorAvailable(orderList, numberMap2Color, colorAvailable);
            page++;
        } while (orderPage.hasNext());
        return colorAvailable;
    }

    private static void computeColorAvailable(List<K3SaleOrder> orderList,
                                              Map<String, String> numberMap2Color,
                                              Map<String, Integer> colorAvailable) {
        for (K3SaleOrder k3SaleOrder : orderList) {
            String materialNumber = k3SaleOrder.getMaterialNumber();
            Integer saleQuantity = k3SaleOrder.getQuantity();
            String color = numberMap2Color.getOrDefault(materialNumber, "UNKNOWN");
            Integer availableQty = colorAvailable.computeIfAbsent(color, s -> 0);
            availableQty += saleQuantity;
            colorAvailable.put(color, availableQty);
        }
    }


    @Override
    public Collection<SaleAvailableDTO> getSaleAvailable(UUID distributorId) {
        DistributorProfile distributorProfile = distributorProfileRepository.findByUserIdWithExternalDistributor(
                distributorId);
        if (distributorProfile == null) {
            throw new I18nBusinessException("distributor.not.bound");
        }
        List<SaleAvailableSnapshot> snapshotList = saleAvailableSnapshotRepository.findLatestBatchByDistributorId(
                distributorId);
        Integer externalDistributorId = distributorProfile.getExternalDistributor().getExternalId();
        SaleAvailableSnapshot firstShot = snapshotList.getFirst();
        OffsetDateTime basedOnCloseTime = firstShot.getBasedOn();
        OffsetDateTime beforeTime = DateUtil.currentDateTimeUTC().minusDays(7);
        // 加算快照基线时间后的可纳入计算的金蝶云订单
        List<K3SaleOrder> updatedOrder = k3SaleOrderRepository.findByCustomerIdAndCloseDateGreaterThanEqualAndCloseDateLessThanAndCloseStatus(
                externalDistributorId, basedOnCloseTime, beforeTime, OrderCloseStatus.CLOSED);
        List<K3SaleOrder> orderList = filterGradeA(updatedOrder);
        Map<String, String> numberMap2Color = computeCode2ColorMap(orderList);
        Map<String, Integer> updatedColorAvailable = new HashMap<>();
        computeColorAvailable(orderList, numberMap2Color, updatedColorAvailable);

        Map<String, SaleAvailableDTO> color2DtoMap = snapshotList.stream().map(saleAvailableSnapshot -> {
            SaleAvailableDTO dto = new SaleAvailableDTO();
            dto.setDistributorId(saleAvailableSnapshot.getDistributorId());
            dto.setColor(saleAvailableSnapshot.getColor());
            dto.setAvailable(saleAvailableSnapshot.getAvailable());
            return dto;
        }).collect(Collectors.toMap(SaleAvailableDTO::getColor, Function.identity()));

        updatedColorAvailable.forEach((color, delta) ->
                accumulateAvailable(
                        color2DtoMap,
                        distributorId,
                        color,
                        delta));

        // TODO 被消耗量计算打磨：构建 redis LRU 缓存
        // 减去快照基线时间后的被消耗的销售量记录
        Collection<ColorDeltaProjection> usedSales = getUsedSales(distributorId,
                firstShot.getTilerSalesRecordSeq() + 1);
        usedSales.forEach(p ->
                accumulateAvailable(
                        color2DtoMap,
                        distributorId,
                        p.getColor(),
                        p.getDelta() == null ? 0 : p.getDelta()));
        return color2DtoMap.values();
    }

    private void accumulateAvailable(
            Map<String, SaleAvailableDTO> target,
            UUID distributorId,
            String color,
            int delta) {
        target.compute(color, (k, dto) -> {
            if (dto == null) {
                dto = new SaleAvailableDTO();
                dto.setDistributorId(distributorId);
                dto.setColor(k);
                dto.setAvailable(0);
            }
            dto.setAvailable(dto.getAvailable() + delta);
            return dto;
        });
    }

    // 获取 SKU 列表，只算优等品
    private List<K3SaleOrder> filterGradeA(Collection<K3SaleOrder> k3SaleOrders) {
        return k3SaleOrders.stream()
                .filter(order -> !order.getMaterialNumber().endsWith("A3"))
                .filter(order -> !order.getMaterialNumber().endsWith("C10"))
                .toList();
    }

    // SKU 列表翻译成花色列表
    @NotNull
    private Map<String, String> computeCode2ColorMap(List<K3SaleOrder> orderList) {
        List<String> materialNumberList = orderList.stream()
                .map(K3SaleOrder::getMaterialNumber)
                .distinct()
                .toList();
        // TODO 考虑改 redis 缓存查询
        List<Tile> tileList = tileRepository.findAllByCodeIn(materialNumberList);
        return tileList.stream().collect(Collectors.toMap(Tile::getCode, Tile::getColor));
    }


    @Auditable(actionType = ActionType.TILER_SALES, actionName = "Take incremental snapshot for all distributor")
    @Override
    public void buildIncrementalSnapshot4AllDistributor() {
        // 按经销商分组，一个经销商算一次，后期可以考虑使用多线程模型做
        List<DistributorProfile> allProfile = distributorProfileRepository.findAllWithExternalDistributor();
        for (DistributorProfile distributorProfile : allProfile) {
            buildIncrementalSnapshot4Distributor(distributorProfile);
        }
    }

    @Auditable(actionType = ActionType.TILER_SALES, actionName = "Take incremental snapshot for specific distributor")
    @Override
    public void buildIncrementalSnapshot4Distributor(UUID distributorId) {
        DistributorProfile distributorProfile = distributorProfileRepository.findByUserIdWithExternalDistributor(
                distributorId);
        if (distributorProfile == null) {
            throw new IllegalArgumentException("Can't find distributor profile for " + distributorId);
        }
        buildIncrementalSnapshot4Distributor(distributorProfile);
    }

    private void buildIncrementalSnapshot4Distributor(DistributorProfile distributorProfile) {
        UUID distributorId = distributorProfile.getUser().getId();
        // 获取最近的快照
        List<SaleAvailableSnapshot> snapshotList = saleAvailableSnapshotRepository.findLatestBatchByDistributorId(
                distributorId);
        Map<String, Integer> colorAvailable = snapshotList.stream()
                .collect(Collectors.toMap(SaleAvailableSnapshot::getColor, SaleAvailableSnapshot::getAvailable));
        // 增量计算
        Integer externalDistributorId = distributorProfile.getExternalDistributor().getExternalId();
        SaleAvailableSnapshot firstShot = snapshotList.getFirst();
        OffsetDateTime basedOnCloseTime = firstShot.getBasedOn();
        OffsetDateTime beforeTime = DateUtil.currentDateTimeUTC().minusDays(7);
        // 加算快照基线时间后的可纳入计算的金蝶云订单
        List<K3SaleOrder> updatedOrder = k3SaleOrderRepository.findByCustomerIdAndCloseDateGreaterThanEqualAndCloseDateLessThanAndCloseStatus(
                externalDistributorId, basedOnCloseTime, beforeTime, OrderCloseStatus.CLOSED);
        List<K3SaleOrder> orderList = filterGradeA(updatedOrder);
        Map<String, String> numberMap2Color = computeCode2ColorMap(orderList);
        Map<String, Integer> updatedColorAvailable = new HashMap<>();
        computeColorAvailable(orderList, numberMap2Color, updatedColorAvailable);
        updatedColorAvailable.forEach((color, delta) -> colorAvailable.merge(color, delta, Integer::sum));
        // 减去已用销量
        Long maxSeqL = computeUsedSales(distributorId, firstShot.getTilerSalesRecordSeq() + 1, colorAvailable);
        long maxSeq = maxSeqL != null && maxSeqL >= 0 ? maxSeqL : 0L;
        // 保存新快照
        saveNewSnapshot(colorAvailable, distributorId, beforeTime, maxSeq);
    }

    private void saveNewSnapshot(Map<String, Integer> colorAvailable,
                                 UUID distributorId,
                                 OffsetDateTime beforeTime,
                                 long maxSeq) {
        OffsetDateTime createTime = DateUtil.currentDateTimeUTC();
        UUID batchId = UUID.randomUUID();
        List<SaleAvailableSnapshot> toSaveSnapshotList = new ArrayList<>();
        for (Map.Entry<String, Integer> availableEntry : colorAvailable.entrySet()) {
            String color = availableEntry.getKey();
            Integer available = availableEntry.getValue();
            SaleAvailableSnapshot saleAvailableSnapshot = new SaleAvailableSnapshot();
            saleAvailableSnapshot.setDistributorId(distributorId);
            saleAvailableSnapshot.setColor(color);
            saleAvailableSnapshot.setAvailable(available);
            saleAvailableSnapshot.setBasedOn(beforeTime);
            saleAvailableSnapshot.setCreatedAt(createTime);
            saleAvailableSnapshot.setBatchId(batchId);
            saleAvailableSnapshot.setTilerSalesRecordSeq(maxSeq);
            toSaveSnapshotList.add(saleAvailableSnapshot);
        }
        saleAvailableSnapshotRepository.saveAll(toSaveSnapshotList);
    }
}
