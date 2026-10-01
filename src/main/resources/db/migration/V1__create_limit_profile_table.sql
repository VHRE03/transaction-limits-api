CREATE TABLE limit_profiles
(
    id          UUID           NOT NULL,
    tier_name   VARCHAR(50)    NOT NULL,
    daily_max   NUMERIC(18, 4) NOT NULL,
    monthly_max NUMERIC(18, 4) NOT NULL,
    per_op_max  NUMERIC(18, 4) NOT NULL,
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP NULL,
    is_deleted  BOOLEAN        NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_limit_profiles PRIMARY KEY (id),
    CONSTRAINT uq_limit_profiles_tier_name UNIQUE (tier_name)
);
