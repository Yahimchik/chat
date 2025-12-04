package org.chat.chat_service.service;

import java.util.Set;
import java.util.UUID;

public interface WebsocketSessionService {

    Set<UUID> getOnlineUsers();
}
