-- 创建行政区划表
CREATE TABLE location_nodes
(
    id         BIGSERIAL PRIMARY KEY,
    parent_id  BIGINT,                                 -- 父节点ID，一级区划为 NULL
    name       TEXT    NOT NULL,                       -- 区划名称 (例如: Ashanti, Kasoa)
    p_code     TEXT UNIQUE ,                                   -- 联合国 P-Code (例如: GH0101)，手动数据可为空或自定义
    level      INT     NOT NULL,                       -- 层级: 1-Region, 2-District, 3-Locality
    source     TEXT    NOT NULL         DEFAULT 'HDX', -- 来源: 'HDX' (同步) 或 'MANUAL' (手动)
    is_deleted BOOLEAN NOT NULL         DEFAULT FALSE, -- 逻辑删除标识
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- 约束与索引
    CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES location_nodes (id) ON DELETE CASCADE
);

-- 普通索引：加速级联查询
CREATE INDEX idx_location_parent_id ON location_nodes (parent_id);
CREATE INDEX idx_location_level ON location_nodes (level);