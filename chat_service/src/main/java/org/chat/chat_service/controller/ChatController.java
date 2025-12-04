package org.chat.chat_service.controller;

import lombok.RequiredArgsConstructor;
import org.chat.chat_service.model.dto.ChatMessageDTO;
import org.chat.chat_service.service.ChatService;
import org.chat.chat_service.service.GroupChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static org.chat.chat_service.constant.RestApiPaths.CHAT_API;


@RestController
@RequiredArgsConstructor
@RequestMapping(CHAT_API)
public class ChatController {

    private final ChatService chatService;
    private final GroupChatService groupChatService;

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<Page<ChatMessageDTO>> getChatMessages(
            @PathVariable UUID roomId,
            @RequestParam UUID senderId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ChatMessageDTO> messages = chatService.getMessagesForRoom(roomId, senderId, pageable);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/private/{recipientId}")
    public ResponseEntity<Map<String, UUID>> findOrCreatePrivateChat(
            @PathVariable UUID recipientId,
            @RequestHeader("X-User-Id") UUID senderId
    ) {
        UUID roomId = chatService.findOrCreatePrivateRoom(senderId, recipientId);
        return ResponseEntity.ok(Map.of("roomId", roomId));
    }
}