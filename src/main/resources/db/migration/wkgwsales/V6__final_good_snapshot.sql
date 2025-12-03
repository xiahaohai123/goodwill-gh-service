CREATE TABLE IF NOT EXISTS final_good_snapshot
(
    id                  UUID PRIMARY KEY,
    material_code       TEXT                     NOT NULL,
    name                TEXT                     NOT NULL,
    model               TEXT                     NOT NULL,
    inventory_unit      TEXT                     NOT NULL,
    inventory_available DOUBLE PRECISION         NOT NULL,
    inventory           DOUBLE PRECISION         NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
