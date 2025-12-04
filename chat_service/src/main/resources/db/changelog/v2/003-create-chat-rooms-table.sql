CREATE TABLE IF NOT EXISTS chat_rooms
(
    id         UUID PRIMARY KEY,
    type       VARCHAR(50)              NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);