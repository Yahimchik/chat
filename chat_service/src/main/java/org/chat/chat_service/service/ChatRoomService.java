package org.chat.chat_service.service;

import java.util.Set;
import java.util.UUID;

public interface ChatRoomService {

    Set<UUID> getRoomParticipants(UUID roomId);
}
