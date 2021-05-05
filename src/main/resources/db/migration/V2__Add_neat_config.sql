CREATE sequence neat_config_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

create table neat_config
(
    id bigint not null,
    normalized_data jsonb not null,
    neat_settings jsonb not null,
    prediction_window_size int not null,
    prediction_period int not null,
    fk_project_id bigint not null,
    creation_date timestamp not null,
    prediction_error double precision,
    training_error double precision,
    test_error double precision,
    PRIMARY KEY (id)
);

ALTER TABLE if EXISTS neat_config add CONSTRAINT project_neat_config_fk FOREIGN KEY (fk_project_id) references projects;

