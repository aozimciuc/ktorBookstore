-- liquibase formatted sql

-- changeset bookstore:1
CREATE TABLE public.events (
    id varchar(20) NOT NULL,
    value varchar(50) NOT NULL,
    description text NULL,
    CONSTRAINT events_id_unique UNIQUE (id),
    CONSTRAINT events_pkey PRIMARY KEY (id)
);

-- rollback DROP TABLE public.events;