package com.simple.taxi.user.service;

import com.simple.taxi.user.model.entities.UserApartment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserApartmentService {

    Flux<UserApartment> findByUserId(UUID userId);

    Flux<UserApartment> findByApartmentId(UUID apartmentId);

    Mono<UserApartment> findActiveByUserId(UUID userId);

    Mono<Boolean> isUserInApartment(UUID userId, UUID apartmentId);

    Mono<Void> deactivateByUserId(UUID userId);

    Flux<UserApartment> findActiveUsersByApartmentId(UUID apartmentId);

    Mono<UserApartment> joinApartment(UUID userId, UUID apartmentId);

    Mono<Void> leaveApartment(UUID userId, UUID apartmentId);
}
