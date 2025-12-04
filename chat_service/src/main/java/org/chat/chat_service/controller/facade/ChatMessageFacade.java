package org.chat.chat_service.controller.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chat_service.model.dto.ChatMessageDTO;
import org.chat.chat_service.model.dto.NotificationChatDto;
import org.chat.chat_service.service.ChatRoomService;
import org.chat.chat_service.service.WebsocketSessionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageFacade {

    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebsocketSessionService websocketSessionService;

    public void notifyRoomParticipants(UUID roomId, UUID senderId, ChatMessageDTO message) {
        try {
            Set<UUID> participantIds = chatRoomService.getRoomParticipants(roomId);

            Set<UUID> onlineUserIds = websocketSessionService.getOnlineUsers();

            Set<UUID> offlineParticipants = participantIds.stream()
                    .filter(participantId ->
                            !participantId.equals(senderId) &&
                                    !onlineUserIds.contains(participantId)
                    )
                    .collect(Collectors.toSet());

            for (UUID offlineUserId : offlineParticipants) {
                NotificationChatDto notification = NotificationChatDto.builder()
                        .type("NEW_MESSAGE")
                        .title("Новое сообщение в чате")
                        .message(String.format("Новое сообщение в комнате: %s",
                                message.getContent().length() > 50 ?
                                        message.getContent().substring(0, 50) + "..." :
                                        message.getContent()))
                        .relatedEntityId(roomId)
                        .relatedEntityType("CHAT_ROOM")
                        .timestamp(LocalDateTime.now())
                        .build();

                messagingTemplate.convertAndSendToUser(
                        offlineUserId.toString(),
                        "/queue/notifications",
                        notification
                );

                log.debug("Sent offline notification to user {} for room {}", offlineUserId, roomId);
            }

            log.info("Notified {} offline participants in room {}", offlineParticipants.size(), roomId);

        } catch (Exception e) {
            log.error("Failed to notify room participants for room {}: {}", roomId, e.getMessage());
        }
    }
}
