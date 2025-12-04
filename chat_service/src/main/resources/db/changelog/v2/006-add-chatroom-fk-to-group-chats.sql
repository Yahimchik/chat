ALTER TABLE IF EXISTS group_chats
    ADD COLUMN chat_room_id UUID;

ALTER TABLE IF EXISTS group_chats
    ADD CONSTRAINT fk_group_chats_chat_room FOREIGN KEY (chat_room_id) REFERENCES chat_rooms (id) ON DELETE SET NULL;

ALTER TABLE IF EXISTS group_chats
    ADD CONSTRAINT uq_group_chats_chat_room_id UNIQUE (chat_room_id);