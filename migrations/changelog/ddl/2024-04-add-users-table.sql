-- liquibase formatted sql

-- changeset bookstore:5
CREATE TABLE public.users (
	username varchar(20) NOT NULL,
	"role" varchar(20) NOT NULL,
	age int4 NOT NULL,
	CONSTRAINT users_pkey PRIMARY KEY (username),
	CONSTRAINT fk_users_role__role FOREIGN KEY ("role") REFERENCES public.roles("role") ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- rollback DROP TABLE public.users;