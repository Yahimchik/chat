package org.chat.chat_service.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chat_service.service.WebsocketSessionService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final WebsocketSessionService websocketSessionService;
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleSessionSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();
        Principal userPrincipal = accessor.getUser();

        if (destination != null && destination.startsWith("/topic/room/")) {
            String roomId = destination.substring("/topic/room/".length());
            websocketSessionService.registerSessionInRoom(sessionId, roomId, userPrincipal);

            Map<String, Object> eventData = Map.of(
                    "type", "USER_JOINED"
            );
            messagingTemplate.convertAndSend(destination, eventData);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        Map<String, Object> sessionData = websocketSessionService.unregisterSession(sessionId);

        if (sessionData != null) {
            UUID userId = (UUID) sessionData.get("userId");
            String roomId = (String) sessionData.get("roomId");

            Map<String, Object> eventData = Map.of("type", "USER_LEFT", "userId", userId);
            messagingTemplate.convertAndSend("/topic/room/" + roomId, eventData);
        }
    }
}