package com.simple.taxi.user.controller;

import com.simple.taxi.user.model.entities.UserStatus;
import com.simple.taxi.user.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-status")
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService service;

    @PostMapping("/{userId}/online")
    public Mono<UserStatus> setOnline(@PathVariable UUID userId) {
        return service.setOnline(userId);
    }

    @PostMapping("/{userId}/offline")
    public Mono<UserStatus> setOffline(@PathVariable UUID userId) {
        return service.setOffline(userId);
    }

    @GetMapping("/{userId}")
    public Mono<UserStatus> getStatus(@PathVariable UUID userId) {
        return service.getStatus(userId);
    }

    @GetMapping("/online")
    public Flux<UserStatus> getOnlineUsers() {
        return service.getOnlineUsers();
    }

    @GetMapping("/offline")
    public Flux<UserStatus> getOfflineUsers() {
        return service.getOfflineUsers();
    }

    @DeleteMapping("/{userId}")
    public Mono<Void> delete(@PathVariable UUID userId) {
        return service.deleteStatus(userId);
    }
}
