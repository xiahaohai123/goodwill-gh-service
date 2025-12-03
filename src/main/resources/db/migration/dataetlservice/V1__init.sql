-- 创建 schema
CREATE SCHEMA IF NOT EXISTS public AUTHORIZATION pg_database_owner;

-- permission_group 表
CREATE TABLE IF NOT EXISTS  permission_group
(
    id          UUID PRIMARY KEY,
    name        TEXT    NOT NULL UNIQUE,
    permissions JSONB   NOT NULL,
    build_in    BOOLEAN NOT NULL DEFAULT FALSE
);

-- tbl_user 表
CREATE TABLE IF NOT EXISTS  tbl_user
(
    id           UUID PRIMARY KEY,
    username     TEXT NOT NULL UNIQUE,
    display_name TEXT NOT NULL,
    password     TEXT NOT NULL,
    build_in    BOOLEAN NOT NULL DEFAULT FALSE
);

-- 用户与权限组关系表
CREATE TABLE IF NOT EXISTS  user_permission_group
(
    user_id  UUID NOT NULL REFERENCES tbl_user (id) ON DELETE CASCADE,
    group_id UUID NOT NULL REFERENCES permission_group (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, group_id)
);

