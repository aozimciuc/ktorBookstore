-- liquibase formatted sql

-- changeset bookstore:3
INSERT INTO public.events (id, value, description) VALUES ('HOME1', '/html', 'View home page');
INSERT INTO public.events (id, value, description) VALUES ('LOGIN1', '/login', 'Login page');
INSERT INTO public.events (id, value, description) VALUES ('LOGOUT1', '/logout', 'Logout page');
INSERT INTO public.events (id, value, description) VALUES ('REGISTER1', '/register', 'Register page');

-- rollback DELETE FROM public.events WHERE id IN ('HOME1', 'LOGIN1', 'LOGOUT1', 'REGISTER1');