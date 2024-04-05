-- liquibase formatted sql

-- changeset bookstore:4
CREATE TABLE public.roles (
	"role" varchar(20) NOT NULL,
	description varchar(255) NOT NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (role)
);

-- rollback DROP TABLE public.roles;