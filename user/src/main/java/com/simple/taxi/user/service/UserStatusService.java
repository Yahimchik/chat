package com.simple.taxi.user.service;

import com.simple.taxi.user.model.entities.UserStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserStatusService {

    Mono<UserStatus> setOnline(UUID userId);

    Mono<UserStatus> setOffline(UUID userId);

    Mono<UserStatus> getStatus(UUID userId);

    Flux<UserStatus> getOnlineUsers();

    Flux<UserStatus> getOfflineUsers();

    Mono<Void> deleteStatus(UUID userId);
}