package com.wangkang.goodwillghservice.feature.user.distributor;

import com.wangkang.goodwillghservice.core.exception.I18nBusinessException;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorExternalInfo;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorProfile;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorProfileHistory;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.Status;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository.DistributorExternalInfoRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository.DistributorProfileHistoryRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository.DistributorProfileRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecord;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecordType;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository.TilerSalesRecordRepository;
import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import com.wangkang.goodwillghservice.feature.k3cloud.model.customer.Customer;
import com.wangkang.goodwillghservice.feature.k3cloud.service.K3cloudCustomerService;
import com.wangkang.goodwillghservice.feature.tilersale.model.SaleAvailableDTO;
import com.wangkang.goodwillghservice.feature.tilersale.service.SaleAvailableService;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import com.wangkang.goodwillghservice.share.util.BizAssert;
import com.wangkang.goodwillghservice.share.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DistributorServiceImpl implements DistributorService {

    private final UserRepository userRepository;
    private final DistributorExternalInfoRepository distributorExternalInfoRepository;
    private final K3cloudCustomerService k3cloudCustomerService;
    private final DistributorProfileRepository distributorProfileRepository;
    private final DistributorProfileHistoryRepository distributorProfileHistoryRepository;
    private final TilerSalesRecordRepository tilerSalesRecordRepository;
    private final SaleAvailableService saleAvailableService;

    public DistributorServiceImpl(UserRepository userRepository,
                                  DistributorExternalInfoRepository distributorExternalInfoRepository,
                                  K3cloudCustomerService k3cloudCustomerService,
                                  DistributorProfileRepository distributorProfileRepository,
                                  DistributorProfileHistoryRepository distributorProfileHistoryRepository,
                                  TilerSalesRecordRepository tilerSalesRecordRepository,
                                  SaleAvailableService saleAvailableService) {
        this.userRepository = userRepository;
        this.distributorExternalInfoRepository = distributorExternalInfoRepository;
        this.k3cloudCustomerService = k3cloudCustomerService;
        this.distributorProfileRepository = distributorProfileRepository;
        this.distributorProfileHistoryRepository = distributorProfileHistoryRepository;
        this.tilerSalesRecordRepository = tilerSalesRecordRepository;
        this.saleAvailableService = saleAvailableService;
    }

    @Override
    public Page<Distributor4ManagerDTO> getDistributors(Pageable pageable) {
        return userRepository.findDistributorWithExternalInfo(pageable);
    }

    @Override
    public Distributor4ManagerDTO getDistributorProfile(UUID uuid) {
        Distributor4ManagerDTO distributorWithExternalInfo = userRepository.findDistributorWithExternalInfo(uuid);
        if (distributorWithExternalInfo == null) {
            throw new I18nBusinessException("user.not.distributor");
        }
        return distributorWithExternalInfo;
    }

    @Override
    public Page<DistributorExternalInfoDTO> getDistributorsExternalList(Pageable pageable) {
        // XH. 开头编号的客户是销号的客户，不予显示
        return distributorExternalInfoRepository.findExternalListExcluded("XH.", pageable);
    }

    @Override
    public Page<DistributorExternalInfoDTO> getUnboundDistributorsExternal(Pageable pageable) {
        return distributorExternalInfoRepository.findUnboundExternalListExcluded("XH.", pageable);
    }

    @Auditable(actionType = ActionType.DISTRIBUTOR, actionName = "Update external distributor")
    @Override
    public void updateDistributorExternal() {
        // 1. 拉取外部系统客户
        List<Customer> customerList = k3cloudCustomerService.getCustomerList();
        if (customerList == null || customerList.isEmpty()) {
            return;
        }

        OffsetDateTime currentDateTimeUTC = DateUtil.currentOffsetDateTimeUTC();

        // 2. 查询数据库中已有的 external distributor
        List<DistributorExternalInfo> existingList = distributorExternalInfoRepository.findAll();

        // 3. 以 externalId 建立 Map，便于 O(1) 查找
        Map<Integer, DistributorExternalInfo> existingMap = existingList.stream()
                .collect(Collectors.toMap(DistributorExternalInfo::getExternalId, Function.identity(), (a, b) -> a));

        // 4. 同步数据（更新 or 新增）
        List<DistributorExternalInfo> toSave = new ArrayList<>(customerList.size());

        for (Customer customer : customerList) {

            Integer externalId = customer.getCustomerId();
            DistributorExternalInfo entity = existingMap.get(externalId);

            // 4.1 不存在 → 新建
            if (entity == null) {
                entity = new DistributorExternalInfo();
                entity.setExternalId(externalId);
            }

            // 4.2 更新字段（新老都走这里）
            entity.setExternalCode(customer.getCode());
            entity.setExternalName(customer.getName());
            entity.setSyncedAt(currentDateTimeUTC);

            toSave.add(entity);
        }

        distributorExternalInfoRepository.saveAll(toSave);
    }

    @Auditable(actionType = ActionType.DISTRIBUTOR, actionName = "Bind external distributor")
    @Transactional
    @Override
    public void bindDistributor2External(UUID userId, UUID externalId) {
        User user = userRepository.findByIdAndGroups_Name(userId, BuiltInPermissionGroup.DISTRIBUTOR.name());
        BizAssert.notNull(user, "distributor.notFound");
        boolean userAlreadyBound = distributorProfileRepository.existsByUser(user);
        if (userAlreadyBound) {
            throw new I18nBusinessException("distributor.alreadyBound");
        }
        DistributorExternalInfo externalDistributor = distributorExternalInfoRepository.findById(externalId)
                .orElseThrow(() -> new I18nBusinessException("distributor.external.notFound"));
        boolean externalDistributorAlreadyBound = distributorProfileRepository.existsByExternalDistributor(
                externalDistributor);
        if (externalDistributorAlreadyBound) {
            throw new I18nBusinessException("distributor.external.alreadyBound");
        }
        DistributorProfile profile = new DistributorProfile();
        profile.setUser(user);
        profile.setExternalDistributor(externalDistributor);
        // 以用户注册时间作为基线
        profile.setAvailableSalesCalFrom(user.getCreatedAt());
        DistributorProfile savedProfile = distributorProfileRepository.save(profile);

        DistributorProfileHistory history = new DistributorProfileHistory();
        history.setUserId(user.getId());
        history.setAction(Status.ACTIVE);
        history.setExternalDistributorId(externalDistributor.getId());
        history.setDistributorProfileId(savedProfile.getId());
        UUID operatorId = UUID.fromString(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        history.setOperatedBy(operatorId);
        distributorProfileHistoryRepository.save(history);
    }


    @Override
    public void unbindDistributor2External(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new I18nBusinessException("distributor.notFound"));
        DistributorProfile profile = distributorProfileRepository.findByUser((user));
        BizAssert.notNull(profile, "distributor.notBound");
        // 获取需要保存到 history 的信息
        UUID profileId = profile.getId();

        UUID externalDistributorId = profile.getExternalDistributor().getId();
        DistributorProfileHistory history = new DistributorProfileHistory();
        history.setUserId(user.getId());
        history.setAction(Status.SUSPENDED);
        history.setExternalDistributorId(externalDistributorId);
        history.setDistributorProfileId(profileId);
        UUID operatorId = UUID.fromString(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        history.setOperatedBy(operatorId);
        distributorProfileHistoryRepository.save(history);
        distributorProfileRepository.deleteById(profileId);
    }

    @Auditable(actionType = ActionType.DISTRIBUTOR, actionName = "Update available sales calculator baseline")
    @Transactional
    @Override
    public void updateAvailableSalesCalBaseline4Distributor(UUID distributorId, OffsetDateTime baseline) {
        int updatedRows = distributorProfileRepository.updateBaselineByUserId(distributorId, baseline);
        if (updatedRows == 0) {
            throw new I18nBusinessException("Update failed, profile not found");
        }
        // 注册一个事务同步回调，只有在 commit 成功后才执行异步逻辑
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 异步方法
                saleAvailableService.buildFullSnapshot4Distributor(distributorId);
            }
        });
    }

    @Override
    public int recordTilerSale(UUID recorderId, TilerSalesDTO tilerSalesDTO) {
        UUID tilerUserId = tilerSalesDTO.getTilerUuid();
        Collection<TilerSalesDataDTO> tilerSalesData = tilerSalesDTO.getTilerSalesData();
        Map<String, Integer> color2QuantityMap = new HashMap<>();
        // 合并重复花色记录，累加
        tilerSalesData.forEach(data -> {
            Integer originalQuantity = color2QuantityMap.computeIfAbsent(data.getColorCode(), color -> 0);
            color2QuantityMap.put(data.getColorCode(), originalQuantity + data.getQuantity());
        });
        Collection<SaleAvailableDTO> saleAvailable = saleAvailableService.getSaleAvailable(recorderId);
        // 转为 Map，便于按花色快速查找
        Map<String, Integer> color2AvailableMap = saleAvailable.stream()
                .collect(Collectors.toMap(
                        SaleAvailableDTO::getColor,
                        SaleAvailableDTO::getAvailable));

        // 校验是否有足够库存
        for (Map.Entry<String, Integer> entry : color2QuantityMap.entrySet()) {
            String color = entry.getKey();
            Integer requiredQuantity = entry.getValue();

            Integer available = color2AvailableMap.getOrDefault(color, 0);
            if (available < requiredQuantity) {
                throw new IllegalStateException(String.format("库存不足：color=%s, required=%d, available=%d",
                        color, requiredQuantity, available));
            }
        }

        OffsetDateTime createTime = DateUtil.currentOffsetDateTimeUTC();
        List<TilerSalesRecord> toSavedRecord = color2QuantityMap.entrySet().stream().map(data -> {
            String color = data.getKey();
            Integer quantity = data.getValue();
            TilerSalesRecord tilerSalesRecord = new TilerSalesRecord();
            tilerSalesRecord.setProductColor(color);
            tilerSalesRecord.setDistributorId(recorderId);
            tilerSalesRecord.setTilerId(tilerUserId);
            tilerSalesRecord.setQuantity(quantity);
            tilerSalesRecord.setRecordType(TilerSalesRecordType.SALE);
            tilerSalesRecord.setSaleTime(createTime);
            tilerSalesRecord.setCreatedAt(createTime);
            return tilerSalesRecord;
        }).toList();

        return tilerSalesRecordRepository.saveAll(toSavedRecord).size();
    }
}
