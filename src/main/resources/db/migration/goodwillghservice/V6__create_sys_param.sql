CREATE TABLE IF NOT EXISTS sys_param
(
    -- 如果习惯用 UUID 做 ID 也没问题，但 param_key 必须唯一
    id          UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    param_key   VARCHAR(100) NOT NULL UNIQUE,
    -- 使用 jsonb 代替 text，既能存简单字符串 "2026-01-01"，也能存复杂对象
    param_value JSONB        NOT NULL,
    -- 建议增加字段类型标识，方便前端或管理端渲染不同的 UI 控件（如开关、输入框、日期选择器）
    value_type  VARCHAR(20)              DEFAULT 'string',
    description TEXT, -- 给管理员看的说明
    update_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 初始化同步基线时间，建议设置为当前时间或一个历史起始点
INSERT INTO sys_param (param_key, param_value, value_type, description)
VALUES ('K3CLOUD_LAST_SYNC_TIME',
           -- 使用 to_jsonb 确保存入的是带双引号的合法 JSON 字符串
        to_jsonb('2025-01-01T00:00:00.000000+00:00'::text),
        'OffsetDateTime',
        '金蝶订单同步基线时间')
ON CONFLICT (param_key)
    DO NOTHING; -- 如果 key 已存在，则什么都不做，保护已有进度