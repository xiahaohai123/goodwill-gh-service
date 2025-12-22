package com.wangkang.goodwillghservice.feature.user.distributor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DistributorService {

    /**
     * 获取本系统内的经销商列表
     * @return 用户信息
     */
    Page<DistributorDTO> getDistributors(Pageable pageable);

    /**
     * 从金蝶云拉取经销商信息到本地
     */
    void updateDistributorExternal();


    /**
     * 获取由外部系统同步到本系统的经销商列表
     * 即金蝶云同步过来的客户列表
     * @return 经销商列表
     */
    Page<DistributorExternalInfoDTO> getDistributorsExternalList(Pageable pageable);

    /**
     * 绑定经销商用户到外部经销商
     * 该操作实际上是认证本系统内的用户真的是客户
     * @param userId     系统内用户 ID
     * @param externalId 外部系统经销商 ID
     */
    void bindDistributor2External(UUID userId, UUID externalId);


    /**
     * 解除经销商用户到外部经销商的绑定关系
     * 会让用户进入未认证的状态
     * @param userId 系统内用户 ID
     */
    void unbindDistributor2External(UUID userId);

}
