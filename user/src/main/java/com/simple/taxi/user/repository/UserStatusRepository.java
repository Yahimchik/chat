package com.simple.taxi.user.repository;

import com.simple.taxi.user.model.entities.UserStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserStatusRepository extends ReactiveCrudRepository<UserStatus, UUID> {
    Mono<UserStatus> findByUserId(UUID userId);
    Flux<UserStatus> findByIsOnlineTrue();
    Flux<UserStatus> findByIsOnlineFalse();
}