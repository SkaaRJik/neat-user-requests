DROP TABLE if EXISTS neat_config;

DROP SEQUENCE if EXISTS neat_config_id_sequence;

CREATE sequence experiment_result_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

create table experiment_results
(
    id bigint not null,
    model jsonb,
    train_errors jsonb,
    test_errors jsonb,
    prediction_error double precision,
    prediction_result jsonb,
    prediction_report_file text,
    creation_date timestamp NOT NULL,
    PRIMARY KEY (id)
);

CREATE SEQUENCE experiment_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

create table experiments
(
    id bigint not null,
    name text not null,
    normalized_file text,
    normalization_method varchar(255),
    normalization_statistic jsonb,
    neat_settings jsonb,
    columns jsonb,
    prediction_window_size int4,
    prediction_period int4,
    fk_project_id bigint not null,
    creation_date timestamp not null,
    updated_date timestamp not null,
    train_end_index int4,
    test_end_index int4,
    fk_experiment_result_id bigint,
    PRIMARY KEY (id)
);

ALTER TABLE if EXISTS experiments add CONSTRAINT project_experiment_fk FOREIGN KEY (fk_project_id) references projects;
ALTER TABLE if EXISTS experiments add CONSTRAINT experiment_reuslt_fk FOREIGN KEY (fk_experiment_result_id) references experiment_results;


ALTER TABLE projects
    DROP COLUMN data,
    ADD source_file  text,
    ADD verified_file text,
    ADD verification_errors jsonb,
    ADD verification_info jsonb,
    ADD legend jsonb,
    ADD headers jsonb,
    ADD log_is_allowed boolean;