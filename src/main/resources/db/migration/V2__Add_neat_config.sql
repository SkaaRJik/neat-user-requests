CREATE sequence neat_config_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

create table neat_config
(
    id int8 not null,
    normalized_data jsonb not null,
    neat_settings jsonb not null,
    prediction_window_size int2 not null,
    prediction_period int2 not null,
    fk_project_id int8 not null,
    creation_date timestamp not null,
    prediction_error float8,
    training_error float8,
    test_error float8,
    PRIMARY KEY (id)
);

ALTER TABLE if EXISTS neat_config add CONSTRAINT project_neat_config_fk FOREIGN KEY (fk_project_id) references projects;

