package com.wangkang.goodwillghservice.feature.user;

/**
 * 处理用户相关服务接口
 */
public interface UserService {

    /**
     * 注册用户
     * @param userDTO 用户信息
     * @return 用户信息，不包含密码
     */
    UserDTO registerUser(UserDTO userDTO);
}
