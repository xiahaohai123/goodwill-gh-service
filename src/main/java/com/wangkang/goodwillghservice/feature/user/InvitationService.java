package com.wangkang.goodwillghservice.feature.user;

import org.springframework.stereotype.Service;

/**
 * 负责邀请注册工作
 * 邀请码的创建与核销，主要基于 redis
 */
@Service
public interface InvitationService {

    /**
     * 为内部管理员创建邀请码，在现实中，内部管理员一般为销售，
     * 本方法返回的邀请码只可以用于创建内部管理员账号，且只能创建一个账号
     * 邀请码要可以被核销
     * @return 邀请码
     */
    Invitation generateInvitation4Manager();
}
