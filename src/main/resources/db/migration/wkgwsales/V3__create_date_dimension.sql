CREATE TABLE date_dimension
(
    date        DATE PRIMARY KEY,
    year        INT         NOT NULL,
    month       INT         NOT NULL,
    day         INT         NOT NULL,
    quarter     INT         NOT NULL,
    week        INT         NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,
    is_weekend  BOOLEAN     NOT NULL
);
