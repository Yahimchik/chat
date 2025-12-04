CREATE TABLE IF NOT EXISTS chat_room_members
(
    id        UUID PRIMARY KEY,
    room_id   UUID                     NOT NULL,
    user_id   UUID                     NOT NULL,
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_chat_room_members_chat_room FOREIGN KEY (room_id) REFERENCES chat_rooms (id) ON DELETE CASCADE,
    CONSTRAINT uq_chat_room_member UNIQUE (room_id, user_id)
);