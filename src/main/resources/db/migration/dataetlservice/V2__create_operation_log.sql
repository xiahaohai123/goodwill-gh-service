-- 执行日志
CREATE TABLE IF NOT EXISTS operation_log
(
    id                 UUID PRIMARY KEY,
    operation_name     TEXT                     NOT NULL,
    operation_type     TEXT                     NOT NULL,
    executor           TEXT                     NOT NULL,
    executed_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    status             TEXT                     NOT NULL,
    detail             JSONB,
    result_data        JSONB,
    error_msg          TEXT,
    duration_ms        int8,
    created_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);