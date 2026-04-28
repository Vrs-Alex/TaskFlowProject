CREATE UNIQUE INDEX unique_active_tag
    ON tag (user_id, name)
    WHERE is_deleted = FALSE;

CREATE UNIQUE INDEX unique_active_area
    ON area (user_id, name)
    WHERE is_deleted = FALSE;
