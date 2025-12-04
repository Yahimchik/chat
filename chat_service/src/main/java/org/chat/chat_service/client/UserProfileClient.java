package org.chat.chat_service.client;

import lombok.RequiredArgsConstructor;
import org.chat.chat_service.model.dto.UserProfileDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileClient {

    private final WebClient userServiceWebClient;

    public Flux<UserProfileDTO> getUserProfiles(Set<UUID> ids) {
        return userServiceWebClient.post()
                .uri("/api/v1/user-profiles/batch")
                .bodyValue(ids)
                .retrieve()
                .bodyToFlux(UserProfileDTO.class);
    }

    public Map<UUID, UserProfileDTO> getProfilesMap(Set<UUID> ids) {
        return getUserProfiles(ids)
                .collectMap(UserProfileDTO::id)
                .block();
    }
}
