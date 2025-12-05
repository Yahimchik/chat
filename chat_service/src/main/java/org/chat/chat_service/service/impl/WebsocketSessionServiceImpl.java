package org.chat.chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chat_service.service.WebsocketSessionService;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebsocketSessionServiceImpl implements WebsocketSessionService {

    private final Map<String, Map<String, Object>> sessionDataMap = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> onlineUserSessionsCount = new ConcurrentHashMap<>();
    private final SimpUserRegistry simpUserRegistry;

    @Override
    public void registerSessionInRoom(String sessionId, String roomId, Principal principal) {
        if (principal == null) {
            log.warn("Attempted to register session {} with an invalid principal.", sessionId);
            return;
        }

        UUID userId = UUID.fromString(principal.getName());

        sessionDataMap.put(sessionId, Map.of("userId", userId, "roomId", roomId));
        onlineUserSessionsCount.compute(userId, (key, count) -> {
            if (count == null) {
                log.info("User {} is now ONLINE.", userId);
                return 1;
            }
            return count + 1;
        });

        log.debug("Session {} registered for user {}. Total sessions for user: {}",
                sessionId, userId, onlineUserSessionsCount.get(userId));
    }

    @Override
    public Map<String, Object> unregisterSession(String sessionId) {
        Map<String, Object> sessionData = sessionDataMap.remove(sessionId);

        if (sessionData != null) {
            UUID userId = (UUID) sessionData.get("userId");
            if (userId != null) {
                onlineUserSessionsCount.computeIfPresent(userId, (key, count) -> {
                    if (count <= 1) {
                        log.info("User {} is now OFFLINE (last session disconnected).", userId);
                        return null;
                    }
                    return count - 1;
                });
            }
            log.debug("Session {} for user {} was unregistered.", sessionId, userId);
        }
        return sessionData;
    }

    public Set<UUID> getOnlineUsers() {
        return Collections.unmodifiableSet(onlineUserSessionsCount.keySet());
    }
}