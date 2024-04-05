-- liquibase formatted sql

-- changeset bookstore:6
INSERT INTO roles (description, "role") VALUES ('Administrator', 'admin');
INSERT INTO roles (description, "role") VALUES ('Regular user', 'user');
INSERT INTO roles (description, "role") VALUES ('Guest', 'guest');

-- rollback DELETE FROM roles WHERE "role" IN ('admin', 'user', 'guest');