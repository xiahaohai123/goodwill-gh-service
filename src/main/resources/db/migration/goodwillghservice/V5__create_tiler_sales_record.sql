CREATE TABLE IF NOT EXISTS tiler_sales_record
(
    id             UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    -- 业务维度
    product_code   TEXT,                 -- 花色
    distributor_id UUID        NOT NULL, -- 经销商 / 门店 / 用户
    tiler_id       UUID        NOT NULL,

    -- 数量与金额
    quantity       INTEGER     NOT NULL CHECK (quantity > 0),

    record_type    TEXT        NOT NULL, -- SALE 销售 / CANCEL 撤销 / ADJUSTMENT 调整

    -- 时间维度（非常关键）
    sale_time      TIMESTAMPTZ NOT NULL, -- 实际成交时间
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_tiler_sales_tiler_time
    ON tiler_sales_record (tiler_id, sale_time DESC);
CREATE INDEX IF NOT EXISTS idx_tiler_sales_distributor_time
    ON tiler_sales_record (distributor_id, sale_time DESC);
CREATE INDEX IF NOT EXISTS idx_tiler_sales_product_time
    ON tiler_sales_record (product_code, sale_time DESC);

-- 赋予 distributor 自我查询和记录销量的权限
UPDATE permission_group
SET permissions = (SELECT jsonb_agg(DISTINCT elem)
                   FROM jsonb_array_elements(permissions || '[
                     "DISTRIBUTOR_SELF_QUERY",
                     "TILER_SALES_MODIFY",
                     "DISTRIBUTOR_SALES_LIMITED_QUERY"
                   ]'::jsonb) AS t(elem))
WHERE name = 'DISTRIBUTOR';

-- 产品信息
CREATE TABLE IF NOT EXISTS tile
(
    id           UUID PRIMARY KEY,
    code         TEXT   NOT NULL UNIQUE,
    name         TEXT   NOT NULL,
    model        TEXT   NOT NULL,
    stock_unit   TEXT   NOT NULL,
    weight_gross FLOAT8 NOT NULL,
    color        TEXT   NULL
);
