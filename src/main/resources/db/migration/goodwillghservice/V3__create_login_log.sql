-- 登录日志
CREATE TABLE IF NOT EXISTS login_log
(
    id                 UUID PRIMARY KEY,
    user_id            UUID NOT NULL,
    username           TEXT NOT NULL,
    display_name       TEXT NOT NULL,
    ip_address         TEXT,
    user_agent         TEXT,
    status             TEXT NOT NULL,
    failure_reason     TEXT,
    occur_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);