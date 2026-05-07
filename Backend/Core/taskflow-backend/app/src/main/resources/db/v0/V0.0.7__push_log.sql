CREATE TABLE push_log (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(20) NOT NULL,
    entity_id BIGINT NOT NULL,
    sent_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (entity_type, entity_id)
);