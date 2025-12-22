package com.wangkang.goodwillghservice.feature.user.distributor;

import com.wangkang.goodwillghservice.core.exception.I18nBusinessException;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorExternalInfo;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorProfile;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository.DistributorExternalInfoRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository.DistributorProfileRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import com.wangkang.goodwillghservice.feature.k3cloud.model.customer.Customer;
import com.wangkang.goodwillghservice.feature.k3cloud.service.K3cloudCustomerService;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import com.wangkang.goodwillghservice.share.util.BizAssert;
import com.wangkang.goodwillghservice.share.util.DateUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DistributorServiceImpl implements DistributorService {

    private final UserRepository userRepository;
    private final DistributorExternalInfoRepository distributorExternalInfoRepository;
    private final K3cloudCustomerService k3cloudCustomerService;
    private final DistributorProfileRepository distributorProfileRepository;

    public DistributorServiceImpl(UserRepository userRepository,
                                  DistributorExternalInfoRepository distributorExternalInfoRepository,
                                  K3cloudCustomerService k3cloudCustomerService,
                                  DistributorProfileRepository distributorProfileRepository) {
        this.userRepository = userRepository;
        this.distributorExternalInfoRepository = distributorExternalInfoRepository;
        this.k3cloudCustomerService = k3cloudCustomerService;
        this.distributorProfileRepository = distributorProfileRepository;
    }

    @Override
    public Page<Distributor4ManagerDTO> getDistributors(Pageable pageable) {
        return userRepository.findDistributorWithExternalInfo(pageable);
    }

    @Override
    public Page<DistributorExternalInfoDTO> getDistributorsExternalList(Pageable pageable) {
        // XH. 开头编号的客户是销号的客户，不予显示
        Page<DistributorExternalInfo> page = distributorExternalInfoRepository.findAllByExternalCodeNotContainingIgnoreCase(
                "XH.", pageable);
        return page.map(info -> {
            DistributorExternalInfoDTO distributorExternalInfoDTO = new DistributorExternalInfoDTO();
            BeanUtils.copyProperties(info, distributorExternalInfoDTO);
            return distributorExternalInfoDTO;
        });
    }

    @Auditable(actionType = ActionType.DISTRIBUTOR, actionName = "Update external distributor")
    @Override
    public void updateDistributorExternal() {
        // 1. 拉取外部系统客户
        List<Customer> customerList = k3cloudCustomerService.getCustomerList();
        if (customerList == null || customerList.isEmpty()) {
            return;
        }

        OffsetDateTime currentDateTimeUTC = DateUtil.currentDateTimeUTC();

        // 2. 查询数据库中已有的 external distributor
        List<DistributorExternalInfo> existingList =
                distributorExternalInfoRepository.findAll();

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
        distributorProfileRepository.save(profile);
    }

    @Transactional
    @Override
    public void unbindDistributor2External(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new I18nBusinessException("distributor.notFound"));
        DistributorProfile profile = distributorProfileRepository.findByUser((user));
        BizAssert.notNull(profile, "distributor.notBound");
        distributorProfileRepository.delete(profile);
    }
}
