package com.wangkang.goodwillghservice.feature.user.distributor;

import com.wangkang.goodwillghservice.feature.user.UserDTO;

import java.util.List;

public interface DistributorService {

    List<UserDTO> getDistributors();

    /**
     * 从金蝶云拉取经销商信息到本地
     */
    void updateDistributorExternal();
}
