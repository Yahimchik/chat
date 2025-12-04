package org.chat.chat_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chat_service.controller.facade.ChatMessageFacade;
import org.chat.chat_service.model.dto.ChatMessageDTO;
import org.chat.chat_service.model.dto.MessageSendRequest;
import org.chat.chat_service.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    public static final String PATH_TOPIC_ROOM = "/topic/room/";

    private final ChatService service;
    private final ChatMessageFacade chatMessageFacade;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}/sendMessage")
    public void sendMessage(
            @DestinationVariable UUID roomId,
             Principal principal,
            @Payload MessageSendRequest request
    ) {
        UUID currentUserId = UUID.fromString(principal.getName());
        request.setRoomId(roomId);

        log.info("Received message to room {}: {}", roomId, request.getContent());

        ChatMessageDTO messageDto = service.sendMessage(request, currentUserId);

        messagingTemplate.convertAndSend(PATH_TOPIC_ROOM + roomId, messageDto);

        chatMessageFacade.notifyRoomParticipants(roomId, currentUserId, messageDto);
    }

    @MessageMapping("/chat/{roomId}/typing")
    public void userTyping(@DestinationVariable UUID roomId,
                            Principal principal
    ) {
        UUID currentUserId = UUID.fromString(principal.getName());
        String userName = "User " + currentUserId;
        log.debug("User {} ({}) is typing in room {}", userName, currentUserId, roomId);

        Map<String, String> typingEvent = Map.of(
                "userId", currentUserId.toString(),
                "userName", userName
        );

        messagingTemplate.convertAndSend(PATH_TOPIC_ROOM + roomId + "/typing", typingEvent);
    }
}
