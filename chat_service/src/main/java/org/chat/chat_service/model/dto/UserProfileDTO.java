package org.chat.chat_service.model.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserProfileDTO(
        UUID id,
        String firstName,
        String lastName,
        String avatarUrl,
        String email,
        String phone,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserProfileDTO unknown(UUID id) {
        return new UserProfileDTO(
                id,
                "Неизвестный", // firstName
                "",            // lastName
                null,          // avatarUrl
                null,          // email
                null,          // phone
                null,          // createdAt
                null           // updatedAt
        );
    }

}
