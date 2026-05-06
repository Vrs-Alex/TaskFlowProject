CREATE TABLE task(
    id BIGINT REFERENCES item(id) PRIMARY KEY,
    due_date DATE NOT NULL,
    recurrence_type VARCHAR(20), -- DAILY, WEEKLY, MONTHLY, YEARLY
    recurrence_interval SMALLINT NOT NULL DEFAULT 1,
    recurrence_days SMALLINT,  -- только для WEEKLY: bitmask: Mon=1, Tue=2, Wed=4, Thu=8, Fri=16, Sat=32, Sun=64
    recurrence_end_date DATE,
    recurrence_count INT
);


CREATE TABLE task_log (
    id BIGSERIAL PRIMARY KEY,
    client_id UUID NOT NULL UNIQUE,
    task_id BIGINT REFERENCES task(id) ON DELETE CASCADE,
    client_task_id UUID NOT NULL,

    date DATE NOT NULL,
    completed_at TIMESTAMPTZ,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    version INT NOT NULL DEFAULT 1,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX task_log_active_unique
    ON task_log(task_id, date)
    WHERE is_deleted = FALSE;
