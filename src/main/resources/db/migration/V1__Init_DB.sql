CREATE sequence user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE usr (
    id bigint NOT NULL,
    active boolean NOT NULL,
    avatar varchar(255),
    creation_date timestamp NOT NULL,
    email varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    password_update_date timestamp NOT NULL,
    password varchar(255) NOT NULL,
    username varchar(255) NOT NULL,
    UNIQUE (email, username),
    PRIMARY KEY (id)
);

CREATE TABLE user_role (
    user_id bigint NOT NULL,
    roles varchar(255)
);

ALTER TABLE if EXISTS user_role add CONSTRAINT user_role_fk FOREIGN KEY (user_id) references usr;

CREATE sequence project_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE projects (
    id bigint NOT NULL,
    created_date timestamp NOT NULL,
    name varchar(255) NOT NULL,
    status int NOT NULL,
    updated_date timestamp NOT NULL,
    data jsonb not null,
    fk_user_id bigint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE if EXISTS projects add CONSTRAINT user_project_fk FOREIGN KEY (fk_user_id) references usr;

CREATE sequence auth_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE auth (
    id bigint NOT NULL,
    expiration_date timestamp NOT NULL,
    previous_refresh_token varchar(255),
    refresh_token varchar(255) NOT NULL,
    fk_user_id bigint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE if EXISTS auth add CONSTRAINT user_auth_fk FOREIGN KEY (fk_user_id) references usr;