-- 创建 schema
CREATE SCHEMA IF NOT EXISTS public AUTHORIZATION pg_database_owner;

-- permission_group 表（角色）
CREATE TABLE IF NOT EXISTS permission_group
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        TEXT NOT NULL UNIQUE,
    permissions JSONB NOT NULL DEFAULT '[]'::jsonb,
    build_in    BOOLEAN NOT NULL DEFAULT FALSE
    );

-- tbl_user 表（手机号作为账号）
CREATE TABLE IF NOT EXISTS tbl_user
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phone_number TEXT NOT NULL,
    area_code    TEXT NOT NULL,
    display_name TEXT NOT NULL,
    password     TEXT NOT NULL,
    build_in     BOOLEAN NOT NULL DEFAULT FALSE,

    inviter_id      UUID,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    banned_until    TIMESTAMPTZ,
    banned_reason   TEXT,

    deleted         BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ,
    deleted_reason  TEXT,

    UNIQUE(phone_number, area_code)
);

-- 用户与权限组关系表
CREATE TABLE IF NOT EXISTS  user_permission_group
(
    user_id  UUID NOT NULL REFERENCES tbl_user (id) ON DELETE CASCADE,
    group_id UUID NOT NULL REFERENCES permission_group (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, group_id)
);

-- 初始化角色（权限先为空）
INSERT INTO permission_group (name, permissions, build_in)
VALUES
    ('ADMIN', '[]'::jsonb, TRUE),
    ('USER', '[]'::jsonb, TRUE),
    ('TILER', '[]'::jsonb, TRUE),
    ('DISTRIBUTOR', '[]'::jsonb, TRUE),
    ('MANAGER', '[]'::jsonb, TRUE)
    ON CONFLICT (name) DO NOTHING;


-- USER：自查自改
UPDATE permission_group
SET permissions = '["USER_SELF_QUERY","USER_SELF_MODIFY"]'::jsonb
WHERE name = 'USER';

-- DISTRIBUTOR：邀请贴砖工 + 提交购买记录
UPDATE permission_group
SET permissions = '["INVITE_TILER", "PURCHASE_RECORD_SUBMIT"]'::jsonb
WHERE name = 'DISTRIBUTOR';

-- MANAGER：邀请经销商和贴砖工
UPDATE permission_group
SET permissions = '[
  "INVITE_DISTRIBUTOR",
  "INVITE_TILER"
]'::jsonb
WHERE name = 'MANAGER';
