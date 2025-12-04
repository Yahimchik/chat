package org.chat.chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chat_service.service.WebsocketSessionService;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebsocketSessionServiceImpl implements WebsocketSessionService {


    private final SimpUserRegistry simpUserRegistry;

    public Set<UUID> getOnlineUsers() {
        Set<UUID> onlineUsers = new HashSet<>();

        for (SimpUser user : simpUserRegistry.getUsers()) {
            try {
                UUID userId = UUID.fromString(user.getName());
                onlineUsers.add(userId);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid user ID in session: {}", user.getName());
            }
        }

        return onlineUsers;
    }
}