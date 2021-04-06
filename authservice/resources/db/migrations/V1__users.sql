CREATE TABLE users (
    id serial CONSTRAINT support_types_pkey PRIMARY KEY,
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id varchar(64) NOT NULL UNIQUE,
    username varchar(128) NOT NULL UNIQUE,
    password varchar(128) NOT NULL
);
