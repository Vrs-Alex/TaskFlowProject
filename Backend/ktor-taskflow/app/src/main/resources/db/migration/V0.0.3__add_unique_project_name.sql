ALTER TABLE project
    ADD CONSTRAINT unique_project_name UNIQUE (owner_id, name);