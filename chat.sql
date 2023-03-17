CREATE TABLE conversations
(
    id              SERIAL PRIMARY KEY,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    user_message    TEXT      NOT NULL,
    chatbot_message TEXT      NOT NULL
);

SELECT * FROM conversations;

INSERT INTO conversations (user_message, chatbot_message)
VALUES ('', ' ');

TRUNCATE TABLE conversations;