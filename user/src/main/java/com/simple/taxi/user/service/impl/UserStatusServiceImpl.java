package com.simple.taxi.user.service.impl;

import com.simple.taxi.user.model.entities.UserStatus;
import com.simple.taxi.user.model.enums.AggregateType;
import com.simple.taxi.user.repository.UserStatusRepository;
import com.simple.taxi.user.service.OutboxService;
import com.simple.taxi.user.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

import static com.simple.taxi.user.model.enums.AggregateType.USER_STATUS;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository repository;
    private final OutboxService outboxService;

    @Override
    public Mono<UserStatus> setOnline(UUID userId) {
        return repository.findById(userId)
                .flatMap(status -> {
                    status.setIsOnline(true);
                    status.setLastSeenAt(Instant.now());
                    return repository.save(status)
                            .flatMap(saved -> outboxService.publishEvent(
                                    USER_STATUS,
                                    saved.getUserId(),
                                    "USER_ONLINE",
                                    saved
                            ).thenReturn(saved));
                });
    }

    @Override
    public Mono<UserStatus> setOffline(UUID userId) {
        return repository.findById(userId)
                .flatMap(status -> {
                    status.setIsOnline(false);
                    status.setLastSeenAt(Instant.now());
                    return repository.save(status)
                            .flatMap(saved -> outboxService.publishEvent(
                                    USER_STATUS,
                                    saved.getUserId(),
                                    "USER_OFFLINE",
                                    saved
                            ).thenReturn(saved));
                });
    }

    @Override
    public Mono<UserStatus> getStatus(UUID userId) {
        return repository.findById(userId);
    }

    @Override
    public Flux<UserStatus> getOnlineUsers() {
        return repository.findByIsOnlineTrue();
    }

    @Override
    public Flux<UserStatus> getOfflineUsers() {
        return repository.findByIsOnlineFalse();
    }

    @Override
    public Mono<Void> deleteStatus(UUID userId) {
        return repository.deleteById(userId);
    }
}
