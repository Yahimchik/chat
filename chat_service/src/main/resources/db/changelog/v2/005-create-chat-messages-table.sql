DROP TABLE IF EXISTS chat_message;
DROP TABLE IF EXISTS chat;

CREATE TABLE IF NOT EXISTS chat_messages
(
    id         UUID PRIMARY KEY,
    room_id    UUID                     NOT NULL,
    sender_id  UUID                     NOT NULL,
    content    TEXT                     NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_chat_messages_chat_room FOREIGN KEY (room_id) REFERENCES chat_rooms (id) ON DELETE CASCADE
);

CREATE INDEX idx_chat_message_room_created ON chat_messages (room_id, created_at DESC);