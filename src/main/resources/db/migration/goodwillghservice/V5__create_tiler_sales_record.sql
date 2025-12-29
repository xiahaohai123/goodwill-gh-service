CREATE TABLE IF NOT EXISTS tiler_sales_record
(
    id             UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    -- 业务维度
    product_color   TEXT,                 -- 花色
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
    ON tiler_sales_record (product_color, sale_time DESC);

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


CREATE TABLE IF NOT EXISTS k3_sale_order
(
    id               UUID PRIMARY KEY,
    bill_no          TEXT                     NOT NULL,
    customer_id      int8                     NOT NULL,
    customer_number  TEXT,
    customer_name    TEXT,
    create_date      TIMESTAMP WITH TIME ZONE NOT NULL,
    document_status  TEXT                     NOT NULL,
    material_number  TEXT                     NOT NULL,
    unit             TEXT                     NOT NULL,
    quantity         INTEGER                  NOT NULL,
    verify_status    TEXT                     NOT NULL,
    verify_date      TIMESTAMP WITH TIME ZONE,
    close_status     TEXT                     NOT NULL,
    close_date       TIMESTAMP WITH TIME ZONE,
    last_modify_date TIMESTAMP WITH TIME ZONE NOT NULL,
    synced_at        TIMESTAMPTZ              NOT NULL DEFAULT NOW()
);


CREATE TABLE IF NOT EXISTS sale_available_snapshot
(
    id             UUID PRIMARY KEY,
    distributor_id UUID                     NOT NULL,
    color          TEXT                     NOT NULL,
    available      INTEGER                  NOT NULL,
    based_on       TIMESTAMP WITH TIME ZONE NOT NULL, -- 基于什么关闭时间打的快照
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
)