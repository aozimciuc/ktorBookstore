-- liquibase formatted sql

-- changeset bookstore:7
INSERT INTO users (age, "role", username) VALUES (30, 'guest', 'john');
INSERT INTO users (age, "role", username) VALUES (25, 'user', 'jane');
INSERT INTO users (age, "role", username) VALUES (25, 'user', 'alexander');
INSERT INTO users (age, "role", username) VALUES (25, 'user', 'dumitru');
INSERT INTO users (age, "role", username) VALUES (25, 'user', 'iurie');
INSERT INTO users (age, "role", username) VALUES (40, 'admin', 'joe');


-- rollback DELETE FROM users WHERE username IN ('john', 'jane', 'joe');