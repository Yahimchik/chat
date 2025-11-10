package com.simple.taxi.user.controller;

import com.simple.taxi.user.model.entities.UserApartment;
import com.simple.taxi.user.service.UserApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-apartments")
@RequiredArgsConstructor
public class UserApartmentController {

    private final UserApartmentService service;

    @GetMapping("/user/{userId}")
    public Flux<UserApartment> findByUser(@PathVariable UUID userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/apartment/{apartmentId}")
    public Flux<UserApartment> findByApartment(@PathVariable UUID apartmentId) {
        return service.findByApartmentId(apartmentId);
    }

    @GetMapping("/user/{userId}/active")
    public Mono<UserApartment> findActiveByUser(@PathVariable UUID userId) {
        return service.findActiveByUserId(userId);
    }

    @PostMapping("/join")
    public Mono<UserApartment> join(@RequestParam UUID userId, @RequestParam UUID apartmentId) {
        return service.joinApartment(userId, apartmentId);
    }

    @PostMapping("/leave")
    public Mono<Void> leave(@RequestParam UUID userId, @RequestParam UUID apartmentId) {
        return service.leaveApartment(userId, apartmentId);
    }

    @GetMapping("/apartment/{apartmentId}/active-users")
    public Flux<UserApartment> findActiveUsers(@PathVariable UUID apartmentId) {
        return service.findActiveUsersByApartmentId(apartmentId);
    }

    @GetMapping("/exists")
    public Mono<Boolean> exists(@RequestParam UUID userId, @RequestParam UUID apartmentId) {
        return service.isUserInApartment(userId, apartmentId);
    }
}
