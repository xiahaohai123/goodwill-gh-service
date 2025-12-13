package com.wangkang.goodwillghservice.feature.user.distributor;

import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorExternalInfo;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository.DistributorExternalInfoRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import com.wangkang.goodwillghservice.feature.k3cloud.model.customer.Customer;
import com.wangkang.goodwillghservice.feature.k3cloud.service.K3cloudCustomerService;
import com.wangkang.goodwillghservice.feature.user.UserDTO;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import com.wangkang.goodwillghservice.share.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DistributorServiceImpl implements DistributorService {

    private final UserRepository userRepository;
    private final DistributorExternalInfoRepository distributorExternalInfoRepository;
    private final K3cloudCustomerService k3cloudCustomerService;

    public DistributorServiceImpl(UserRepository userRepository,
                                  DistributorExternalInfoRepository distributorExternalInfoRepository,
                                  K3cloudCustomerService k3cloudCustomerService) {
        this.userRepository = userRepository;
        this.distributorExternalInfoRepository = distributorExternalInfoRepository;
        this.k3cloudCustomerService = k3cloudCustomerService;
    }

    @Override
    public List<UserDTO> getDistributors() {
        List<User> distinctByGroupsName = userRepository.findDistinctByGroups_Name(
                BuiltInPermissionGroup.DISTRIBUTOR.name());
        return distinctByGroupsName.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).toList();
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
}
