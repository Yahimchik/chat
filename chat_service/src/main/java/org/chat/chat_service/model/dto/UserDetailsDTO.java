package org.chat.chat_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private UUID id;
    private String fullName;
    private String avatarUrl;
}