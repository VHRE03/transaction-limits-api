CREATE TABLE user_limit_assignments
(
    id               UUID      NOT NULL,
    user_id          UUID      NOT NULL,
    limit_profile_id UUID      NOT NULL,
    created_at       TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP NULL,
    is_deleted       BOOLEAN   NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_user_limit_assignments PRIMARY KEY (id),
    CONSTRAINT uq_user_limit_assignments_user_id UNIQUE (user_id),
    CONSTRAINT fk_user_limit_assignments_limit_profiles FOREIGN KEY (limit_profile_id) REFERENCES limit_profiles (id)
);

CREATE INDEX ix_user_limit_assignments_limit_profile_id ON user_limit_assignments (limit_profile_id);
