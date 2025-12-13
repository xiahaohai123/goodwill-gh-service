package com.wangkang.goodwillghservice.dao.goodwillghservice.security.model;

public enum Permission {
    /** 超级管理员相关所有权限 */
    ADMIN,
    /** 用户自查询 */
    USER_SELF_QUERY,
    /** 用户自修改，手机号等 */
    USER_SELF_MODIFY,
    /** 邀请内部管理 */
    INVITE_MANAGER,
    /** 邀请经销商 */
    INVITE_DISTRIBUTOR,
    /** 邀请贴砖工 */
    INVITE_TILER,
    /** 提交购买记录 */
    PURCHASE_RECORD_SUBMIT,
    /** 经销商查询 */
    DISTRIBUTOR_QUERY,
    /** 經銷商數據更新 */
    DISTRIBUTOR_MODIFY,
    /** 调试权限，有一些调试型接口 */
    DEBUG;
}
