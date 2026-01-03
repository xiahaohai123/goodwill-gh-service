package com.wangkang.goodwillghservice.feature.user.distributor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface DistributorService {

    /**
     * 获取本系统内的经销商列表
     * @return 用户信息
     */
    Page<Distributor4ManagerDTO> getDistributors(Pageable pageable);

    /**
     * 获取账号信息
     * @param uuid 经销商账号 id
     * @return 信息
     */
    Distributor4ManagerDTO getDistributorProfile(UUID uuid);

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

    Page<DistributorExternalInfoDTO> getUnboundDistributorsExternal(Pageable pageable);

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

    /**
     * 记录贴砖工销量
     * @param recorderId    出售方，也是记录方的 UID，也是经销商 UID
     * @param tilerSalesDTO 记录内容和贴砖工信息
     * @return 记录的总和销售量
     */
    int recordTilerSale(UUID recorderId, TilerSalesDTO tilerSalesDTO);


    /**
     * 为某个经销商更新计算计算销售可用量计算基线
     * 更新基线后会重新计算快照
     * @param distributorId 经销商用户 id
     * @param baseline      基线时间，不可以小于 2024 年
     */
    void updateAvailableSalesCalBaseline4Distributor(UUID distributorId, OffsetDateTime baseline);
}
