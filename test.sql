CREATE TABLE messages
(
    id      SERIAL PRIMARY KEY,
    role    VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL
);
