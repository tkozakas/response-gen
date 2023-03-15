CREATE TABLE messages
(
    id              integer   DEFAULT nextval('messages'::regclass) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    user_message    TEXT      NOT NULL,
    chatbot_message TEXT      NOT NULL
);

SELECT * FROM messages;