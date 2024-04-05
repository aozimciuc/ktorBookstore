-- liquibase formatted sql

-- changeset bookstore:2
CREATE TABLE public.log_events (
	"uuid" uuid NOT NULL,
	"timestamp" timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	event_id varchar(20) NOT NULL,
	severity varchar(20) DEFAULT 'UNKNOWN'::character varying NOT NULL,
	msg text NULL,
	CONSTRAINT log_events_pkey PRIMARY KEY (uuid),
	CONSTRAINT fk_log_events_event_id__id FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- rollback DROP TABLE public.log_events;