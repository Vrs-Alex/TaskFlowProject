-- ==========================================
-- 1. СПРАВОЧНИКИ
-- ==========================================

-- Статус проекта
CREATE TABLE project_status (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

-- Тип сущности: task, event, habit, goal
CREATE TABLE item_type (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

-- Статус сущности: todo, in_progress, done, cancelled
CREATE TABLE item_status (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

-- Промежуток повторения: daily, weekly, monthly, yearly, custom
CREATE TABLE recurrence_frequency (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

-- Когда заканчивается повторение: never, after_count, by_date
CREATE TABLE recurrence_end_type (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE
);

-- ==========================================
-- 2. ПОЛЬЗОВАТЕЛИ И АВТОРИЗАЦИЯ
-- ==========================================

CREATE TABLE app_user (
    user_id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    password_hash TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE refresh_token (
    token_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_user(user_id) ON DELETE CASCADE,
    token_hash TEXT NOT NULL UNIQUE,
    device_info VARCHAR(255),
    ip_address VARCHAR(45),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_used_at TIMESTAMP WITH TIME ZONE,
    is_revoked BOOLEAN NOT NULL DEFAULT FALSE
);

-- ==========================================
-- 3. КОНТЕКСТ
-- ==========================================

CREATE TABLE area (
     area_id BIGSERIAL PRIMARY KEY,
     owner_id BIGINT NOT NULL REFERENCES app_user(user_id) ON DELETE CASCADE,
     name VARCHAR(100) NOT NULL,
     color VARCHAR(7) NOT NULL DEFAULT '#FFFFFF',

     client_id UUID UNIQUE NOT NULL,
     created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
     version INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE project (
     project_id BIGSERIAL PRIMARY KEY,
     owner_id BIGINT NOT NULL REFERENCES app_user(user_id) ON DELETE CASCADE,
     area_id BIGINT REFERENCES area(area_id) ON DELETE SET NULL,
     status_id INTEGER REFERENCES project_status(id),
     name VARCHAR(255) NOT NULL,
     description TEXT,
     color VARCHAR(7),
     due_date DATE,

     client_id UUID UNIQUE NOT NULL,
     created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
     version INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE goal_unit (
    unit_id SERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL REFERENCES app_user(user_id) ON DELETE CASCADE,
    code VARCHAR(20) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    name VARCHAR(50) NOT NULL,

    client_id UUID UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version INTEGER NOT NULL DEFAULT 1,

    UNIQUE(owner_id, code)
);

-- ==========================================
-- 4. ПРАВИЛА ПОВТОРЕНИЯ
-- ==========================================

CREATE TABLE recurrence_rule (
     rule_id BIGSERIAL PRIMARY KEY,
     frequency_id INTEGER NOT NULL REFERENCES recurrence_frequency(id),
     interval_value INTEGER NOT NULL DEFAULT 1,
     days_of_week INTEGER[],
     end_type_id INTEGER NOT NULL REFERENCES recurrence_end_type(id),
     end_count INTEGER,
     end_date DATE,

     client_id UUID UNIQUE NOT NULL,
     created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
     version INTEGER NOT NULL DEFAULT 1,

     CONSTRAINT check_end_count CHECK (end_type_id != 2 OR end_count IS NOT NULL),
    CONSTRAINT check_end_date CHECK (end_type_id != 3 OR end_date IS NOT NULL)
);

-- ==========================================
-- 5. ОБЩАЯ ТАБЛИЦА ДЛЯ ВСЕХ СУЩНОСТЕЙ
-- ==========================================

-- Если project_id != null → area берётся из project.area_id
-- Если project_id == null → используется item.area_id
CREATE TABLE item (
     item_id BIGSERIAL PRIMARY KEY,
     owner_id BIGINT NOT NULL REFERENCES app_user(user_id) ON DELETE CASCADE,
     type_id INTEGER NOT NULL REFERENCES item_type(id),
     project_id BIGINT REFERENCES project(project_id) ON DELETE SET NULL,
     area_id BIGINT REFERENCES area(area_id) ON DELETE SET NULL,
     status_id INTEGER REFERENCES item_status(id),
     recurrence_id BIGINT REFERENCES recurrence_rule(rule_id) ON DELETE SET NULL,
     parent_id BIGINT REFERENCES item(item_id) ON DELETE CASCADE,

     title VARCHAR(255) NOT NULL,
     description TEXT,
     priority SMALLINT NOT NULL DEFAULT 0,

     client_id UUID UNIQUE NOT NULL,
     created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
     version INTEGER NOT NULL DEFAULT 1,

     CONSTRAINT check_title_not_empty CHECK (LENGTH(TRIM(title)) > 0)
);

-- ==========================================
-- 6. РАСШИРЕНИЯ ДЛЯ ЗАДАЧ
-- ==========================================

CREATE TABLE task (
     item_id BIGINT PRIMARY KEY REFERENCES item(item_id) ON DELETE CASCADE,
     due_date DATE,
     completed_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE task_log (
    log_id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL REFERENCES task(item_id) ON DELETE CASCADE,
    log_date DATE NOT NULL,
    note TEXT,
    completed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    client_id UUID UNIQUE NOT NULL,

    UNIQUE(task_id, log_date)
);

-- ==========================================
-- 7. РАСШИРЕНИЯ ДЛЯ СОБЫТИЙ
-- ==========================================

CREATE TABLE event (
    item_id BIGINT PRIMARY KEY REFERENCES item(item_id) ON DELETE CASCADE,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    is_all_day BOOLEAN NOT NULL DEFAULT FALSE,
    location VARCHAR(255),

    CONSTRAINT check_event_times CHECK (end_time > start_time)
);

-- ==========================================
-- 8. РАСШИРЕНИЯ ДЛЯ ПРИВЫЧЕК
-- ==========================================

CREATE TABLE habit (
    item_id BIGINT PRIMARY KEY REFERENCES item(item_id) ON DELETE CASCADE,
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT check_habit_dates CHECK (end_date IS NULL OR end_date >= start_date)
);

CREATE TABLE habit_log (
    log_id BIGSERIAL PRIMARY KEY,
    habit_id BIGINT NOT NULL REFERENCES habit(item_id) ON DELETE CASCADE,
    log_date DATE NOT NULL,
    note TEXT,
    completed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    client_id UUID UNIQUE NOT NULL,

    UNIQUE(habit_id, log_date)
);

-- ==========================================
-- 9. РАСШИРЕНИЯ ДЛЯ ЦЕЛЕЙ
-- ==========================================

CREATE TABLE goal (
     item_id BIGINT PRIMARY KEY REFERENCES item(item_id) ON DELETE CASCADE,
     target_value NUMERIC(12,2) NOT NULL,
     current_value NUMERIC(12,2) NOT NULL DEFAULT 0,
     unit_id INTEGER REFERENCES goal_unit(unit_id),
     start_date DATE,
     deadline DATE,
     completed_at TIMESTAMP WITH TIME ZONE,

     CONSTRAINT check_goal_dates CHECK (deadline IS NULL OR start_date IS NULL OR deadline >= start_date),
     CONSTRAINT check_target_positive CHECK (target_value > 0)
);

CREATE TABLE goal_log (
    log_id BIGSERIAL PRIMARY KEY,
    goal_id BIGINT NOT NULL REFERENCES goal(item_id) ON DELETE CASCADE,
    value_change NUMERIC(12,2) NOT NULL,
    note TEXT,
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    client_id UUID UNIQUE NOT NULL
);

-- ==========================================
-- 10. ТЕГИ
-- ==========================================

CREATE TABLE tag (
    tag_id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL REFERENCES app_user(user_id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    color VARCHAR(7) NOT NULL DEFAULT '#808080',

    client_id UUID UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version INTEGER NOT NULL DEFAULT 1,

    UNIQUE(owner_id, name)
);

CREATE TABLE item_tag (
    item_id BIGINT NOT NULL REFERENCES item(item_id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tag(tag_id) ON DELETE CASCADE,
    PRIMARY KEY (item_id, tag_id)
);

-- ==========================================
-- 11. ВЛОЖЕНИЯ
-- ==========================================

CREATE TABLE attachment (
     attachment_id BIGSERIAL PRIMARY KEY,
     item_id BIGINT NOT NULL REFERENCES item(item_id) ON DELETE CASCADE,
     file_name VARCHAR(255) NOT NULL,
     file_path TEXT NOT NULL,
     file_type VARCHAR(100),
     file_size BIGINT,

     client_id UUID UNIQUE NOT NULL,
     created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
     version INTEGER NOT NULL DEFAULT 1,

     CONSTRAINT check_file_size CHECK (file_size IS NULL OR file_size >= 0)
);

-- ==========================================
-- 12. НАПОМИНАНИЯ
-- ==========================================

CREATE TABLE reminder (
    reminder_id BIGSERIAL PRIMARY KEY,
    item_id BIGINT NOT NULL REFERENCES item(item_id) ON DELETE CASCADE,
    remind_at TIMESTAMP WITH TIME ZONE NOT NULL,
    is_sent BOOLEAN NOT NULL DEFAULT FALSE,

    client_id UUID UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version INTEGER NOT NULL DEFAULT 1
);

-- ==========================================
-- 13. ИНДЕКСЫ ДЛЯ ПРОИЗВОДИТЕЛЬНОСТИ
-- ==========================================

-- Индексы для синхронизации
CREATE INDEX idx_item_owner_updated ON item(owner_id, updated_at) WHERE is_deleted = FALSE;
CREATE INDEX idx_item_client_id ON item(client_id) WHERE client_id IS NOT NULL;
CREATE INDEX idx_project_owner_updated ON project(owner_id, updated_at) WHERE is_deleted = FALSE;
CREATE INDEX idx_area_owner_updated ON area(owner_id, updated_at) WHERE is_deleted = FALSE;
CREATE INDEX idx_tag_owner_updated ON tag(owner_id, updated_at) WHERE is_deleted = FALSE;
CREATE INDEX idx_goal_unit_owner_updated ON goal_unit(owner_id, updated_at) WHERE is_deleted = FALSE;
CREATE INDEX idx_recurrence_updated ON recurrence_rule(updated_at) WHERE is_deleted = FALSE;
CREATE INDEX idx_attachment_owner_updated ON attachment(item_id, updated_at) WHERE is_deleted = FALSE;
CREATE INDEX idx_reminder_owner_updated ON reminder(item_id, updated_at) WHERE is_deleted = FALSE;

-- Индексы для поиска и фильтрации
CREATE INDEX idx_item_owner_type ON item(owner_id, type_id) WHERE is_deleted = FALSE;
CREATE INDEX idx_item_project ON item(project_id) WHERE project_id IS NOT NULL AND is_deleted = FALSE;
CREATE INDEX idx_item_area ON item(area_id) WHERE area_id IS NOT NULL AND is_deleted = FALSE;
CREATE INDEX idx_item_status ON item(status_id) WHERE status_id IS NOT NULL AND is_deleted = FALSE;
CREATE INDEX idx_item_parent ON item(parent_id) WHERE parent_id IS NOT NULL;
CREATE INDEX idx_task_due_date ON task(due_date) WHERE due_date IS NOT NULL AND completed_at IS NULL;
CREATE INDEX idx_goal_deadline ON goal(deadline) WHERE deadline IS NOT NULL AND completed_at IS NULL;
CREATE INDEX idx_event_start_time ON event(start_time);
CREATE INDEX idx_habit_active ON habit(item_id) WHERE is_active = TRUE;
CREATE INDEX idx_reminder_time ON reminder(remind_at) WHERE is_sent = FALSE AND is_deleted = FALSE;

-- ==========================================
-- 14. ТРИГГЕРЫ ДЛЯ АВТООБНОВЛЕНИЯ
-- ==========================================

CREATE OR REPLACE FUNCTION update_updated_at_and_version()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_item_metadata BEFORE UPDATE ON item
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_and_version();

CREATE TRIGGER update_project_metadata BEFORE UPDATE ON project
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_and_version();

CREATE TRIGGER update_area_metadata BEFORE UPDATE ON area
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_and_version();

CREATE TRIGGER update_tag_metadata BEFORE UPDATE ON tag
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_and_version();

CREATE TRIGGER update_goal_unit_metadata BEFORE UPDATE ON goal_unit
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_and_version();

CREATE TRIGGER update_recurrence_metadata BEFORE UPDATE ON recurrence_rule
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_and_version();

CREATE TRIGGER update_attachment_metadata BEFORE UPDATE ON attachment
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_and_version();

CREATE TRIGGER update_reminder_metadata BEFORE UPDATE ON reminder
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_and_version();

-- ==========================================
-- 15. НАЧАЛЬНЫЕ ДАННЫЕ
-- ==========================================

INSERT INTO project_status (code) VALUES
    ('ACTIVE'),
    ('COMPLETED'),
    ('ARCHIVED');

INSERT INTO item_type (code) VALUES
     ('TASK'),
     ('EVENT'),
     ('HABIT'),
     ('GOAL');

INSERT INTO item_status (code) VALUES
     ('TODO'),
     ('IN_PROGRESS'),
     ('DONE'),
     ('CANCELLED');

INSERT INTO recurrence_frequency (code) VALUES
     ('DAILY'),
     ('WEEKLY'),
     ('MONTHLY'),
     ('YEARLY'),
     ('CUSTOM');

INSERT INTO recurrence_end_type (code) VALUES
    ('NEVER'),
    ('AFTER_COUNT'),
    ('BY_DATE');