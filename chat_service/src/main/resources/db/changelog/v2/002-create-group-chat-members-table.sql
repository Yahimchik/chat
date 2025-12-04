CREATE TABLE IF NOT EXISTS group_chat_members
(
    id             UUID PRIMARY KEY,
    group_chat_id  UUID                     NOT NULL,
    user_id        UUID                     NOT NULL,
    role           VARCHAR(50)              NOT NULL,
    is_club_member BOOLEAN                  NOT NULL DEFAULT FALSE,
    joined_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_group_chat_members_group_chat FOREIGN KEY (group_chat_id) REFERENCES group_chats (id) ON DELETE CASCADE,
    CONSTRAINT uq_group_chat_member UNIQUE (group_chat_id, user_id)
);

CREATE INDEX idx_group_chat_member_user ON group_chat_members (user_id);