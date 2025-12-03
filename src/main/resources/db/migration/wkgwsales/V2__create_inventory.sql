CREATE TABLE IF NOT EXISTS public.inventory
(
    id                  UUID PRIMARY KEY,
    ban_status          TEXT,
    data_status         TEXT,
    inventory           FLOAT8,
    inventory_available FLOAT8,
    inventory_unit      TEXT,
    material_properties TEXT,
    model               TEXT,
    name                TEXT,
    organization        TEXT,
    sku_code            TEXT NOT NULL UNIQUE
);