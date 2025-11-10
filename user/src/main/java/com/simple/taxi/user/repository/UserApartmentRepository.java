package com.simple.taxi.user.repository;

import com.simple.taxi.user.model.entities.UserApartment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserApartmentRepository extends ReactiveCrudRepository<UserApartment, UUID> {
    Flux<UserApartment> findAllByUserId(UUID userId);

    Flux<UserApartment> findAllByApartmentId(UUID apartmentId);

    Flux<UserApartment> findByUserId(UUID userId);

    Flux<UserApartment> findByApartmentId(UUID apartmentId);

    Mono<UserApartment> findByUserIdAndIsActiveTrue(UUID userId);

    Mono<Boolean> existsByUserIdAndApartmentIdAndIsActiveTrue(UUID userId, UUID apartmentId);

    Flux<UserApartment> findByApartmentIdAndIsActiveTrue(UUID apartmentId);
}
