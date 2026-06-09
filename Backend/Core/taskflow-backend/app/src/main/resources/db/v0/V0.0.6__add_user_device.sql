CREATE TABLE user_device (
    id         BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    platform   VARCHAR(32)  NOT NULL,
    token      TEXT         NOT NULL,
    device_id  VARCHAR(255) NOT NULL,
    device_name TEXT,
    push_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_user_device_user_id ON user_device(user_id);