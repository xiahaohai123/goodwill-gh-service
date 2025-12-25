CREATE TABLE IF NOT EXISTS distributor_external_info
(
    id            UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    external_id   int8        NOT NULL UNIQUE,        -- 外部系统唯一ID
    external_name TEXT        NOT NULL,               -- 外部系统名称
    external_code TEXT,                               -- 外部编号
    synced_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- 什么时候同步来的
    UNIQUE (external_id)
);

CREATE TABLE IF NOT EXISTS distributor_profile
(
    id                       UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    user_id                  UUID        NOT NULL REFERENCES tbl_user (id) ON DELETE CASCADE,
    external_distributor_id  UUID        NOT NULL REFERENCES distributor_external_info (id) ON DELETE RESTRICT,
    bound_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    available_sales_cal_from TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- 可用从什么时间开始计算，这个时间点之后在金蝶云上的订单都会被统计进可用销售量
    -- 一个用户在同一时间，只能有一个 ACTIVE 绑定
    CONSTRAINT uq_user_active_binding
        UNIQUE (user_id)
            DEFERRABLE INITIALLY IMMEDIATE
);

CREATE TABLE IF NOT EXISTS distributor_profile_history
(
    id                      UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    distributor_profile_id  UUID,
    user_id                 UUID        NOT NULL,
    external_distributor_id UUID        NOT NULL,

    action                  TEXT        NOT NULL,
    -- BIND / SUSPEND / REBIND / SYSTEM_ROLLBACK

    operated_by             UUID, -- 管理员ID（可为空，系统操作）
    operated_at             TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    reason                  TEXT
);

-- 赋予 manager 查询经销商和修改经销商信息的权限
UPDATE permission_group
SET permissions = (SELECT jsonb_agg(DISTINCT elem)
                   FROM jsonb_array_elements(permissions || '[
                     "DISTRIBUTOR_QUERY",
                     "DISTRIBUTOR_MODIFY",
                     "TILER_QUERY",
                     "TILER_MODIFY"
                   ]'::jsonb) AS t(elem))
WHERE name = 'MANAGER';
