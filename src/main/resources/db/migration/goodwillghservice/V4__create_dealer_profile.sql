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
    id                      UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    user_id                 UUID        NOT NULL UNIQUE REFERENCES tbl_user (id) ON DELETE CASCADE,
    external_distributor_id UUID        NOT NULL REFERENCES distributor_external_info (id) ON DELETE RESTRICT,
    bound_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
