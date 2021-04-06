CREATE TABLE users_info (
    id serial CONSTRAINT support_types_pkey PRIMARY KEY,
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id varchar(64) NOT NULL UNIQUE,
    username varchar(128) NOT NULL,
    email varchar(256) NOT NULL,
    first_name varchar(128) NOT NULL,
    last_name varchar(128) NOT NULL
);
