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
     * 邀请码使用后会被废除
     * @return 邀请码
     */
    Invitation generateInvitation4Manager();


    /**
     * 为经销商创建邀请码
     * 本方法返回的邀请码只可以用于创建经销商账号，且只能创建一个账号
     * 邀请码使用后会被废除
     * @return 邀请码
     */
    Invitation generateInvitation4Dealer();

    /**
     * 为贴砖工创建邀请码
     * 本方法返回的邀请码只可以用于创建贴砖工账号，且只能创建一个账号
     * 邀请码使用后会被废除
     * @return 邀请码
     */
    Invitation generateInvitation4Tiler();

    /**
     * 验证邀请码，验证通过会返回邀请函对象，否则抛出异常，附带错误信息
     * 验证后，邀请码会立即失效
     * 邀请函会包含邀请函附带信息，例如这封邀请函可以用来注册什么角色
     * @param invitationCode 邀请码
     * @return 邀请函
     */
    Invitation validateInvitation(String invitationCode);
}
