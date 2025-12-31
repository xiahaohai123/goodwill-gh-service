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
    /** 經銷商数据更新 */
    DISTRIBUTOR_MODIFY,
    /** 调试权限，有一些调试型接口 */
    DEBUG,
    /** 贴砖工查询 */
    TILER_QUERY,
    /** 贴砖工数据更新 */
    TILER_MODIFY,
    /** 经销商用户自查权限，例如查询自己的 profile，自己通过贴砖工卖出了多少产品，以及自己可以卖出的产品还有哪些 */
    DISTRIBUTOR_SELF_QUERY,
    /** 经销商用户对贴砖工销量的修改权限 */
    TILER_SALES_MODIFY,
    /** 贴砖工用户自查权限，包括查询自己的积分，自己的积分记录等 */
    TILER_SELF_QUERY,
}
