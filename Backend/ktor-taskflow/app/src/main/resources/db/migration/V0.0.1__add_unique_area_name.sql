ALTER TABLE area
    ADD CONSTRAINT unique_area_name UNIQUE (owner_id, name);