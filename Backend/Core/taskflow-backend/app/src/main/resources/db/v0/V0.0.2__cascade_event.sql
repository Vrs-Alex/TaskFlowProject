-- Удаляем существующий constraint
ALTER TABLE event DROP CONSTRAINT IF EXISTS fk_event_item;
ALTER TABLE event DROP CONSTRAINT IF EXISTS event_id_fkey;

-- Создаем заново с CASCADE
ALTER TABLE event
    ADD CONSTRAINT event_id_fkey
        FOREIGN KEY (id) REFERENCES item (id)
            ON DELETE CASCADE;