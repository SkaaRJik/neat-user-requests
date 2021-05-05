ALTER TABLE experiments
    ADD COLUMN status int;

ALTER TABLE projects
    ADD COLUMN fk_last_active_experiment_id bigint,
    ADD CONSTRAINT last_active_experiment_id_fk FOREIGN KEY (fk_last_active_experiment_id) references experiments