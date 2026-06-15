ALTER TABLE task
    ALTER COLUMN due_date DROP NOT NULL;
ALTER TABLE task
    ADD CONSTRAINT date_nullable_check CHECK (recurrence_type IS NULL OR due_date IS NOT NULL);


ALTER TABLE event
    ALTER COLUMN end_date DROP NOT NULL;
ALTER TABLE event
    ADD CONSTRAINT end_date_nullable_check CHECK (end_date IS NULL OR end_date >= start_date);


