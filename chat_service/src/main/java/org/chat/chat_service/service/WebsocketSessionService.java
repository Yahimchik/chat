package org.chat.chat_service.service;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface WebsocketSessionService {

    void registerSessionInRoom(String sessionId, String roomId, Principal principal);

    Map<String, Object> unregisterSession(String sessionId);

    Set<UUID> getOnlineUsers();
}
