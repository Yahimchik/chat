package com.simple.taxi.user.service.impl;

import com.simple.taxi.user.model.entities.UserApartment;
import com.simple.taxi.user.repository.UserApartmentRepository;
import com.simple.taxi.user.service.OutboxService;
import com.simple.taxi.user.service.UserApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

import static com.simple.taxi.user.model.enums.AggregateType.USER_APARTMENT;

@Service
@RequiredArgsConstructor
public class UserApartmentServiceImpl implements UserApartmentService {

    private final UserApartmentRepository repository;
    private final OutboxService outboxService;

    @Override
    public Flux<UserApartment> findByUserId(UUID userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public Flux<UserApartment> findByApartmentId(UUID apartmentId) {
        return repository.findAllByApartmentId(apartmentId);
    }

    @Override
    public Mono<UserApartment> findActiveByUserId(UUID userId) {
        return repository.findByUserIdAndIsActiveTrue(userId);
    }

    @Override
    public Mono<Boolean> isUserInApartment(UUID userId, UUID apartmentId) {
        return repository.existsByUserIdAndApartmentIdAndIsActiveTrue(userId, apartmentId);
    }

    @Override
    public Mono<Void> deactivateByUserId(UUID userId) {
        return repository.findByUserIdAndIsActiveTrue(userId)
                .flatMap(ua -> {
                    ua.setIsActive(false);
                    return repository.save(ua);
                })
                .then();
    }

    @Override
    public Flux<UserApartment> findActiveUsersByApartmentId(UUID apartmentId) {
        return repository.findByApartmentIdAndIsActiveTrue(apartmentId);
    }

    @Override
    public Mono<UserApartment> joinApartment(UUID userId, UUID apartmentId) {
        return repository.existsByUserIdAndApartmentIdAndIsActiveTrue(userId, apartmentId)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalStateException("User already joined this apartment"));
                    }
                    UserApartment entity = UserApartment.builder()
                            .userId(userId)
                            .apartmentId(apartmentId)
                            .joinedAt(Instant.now())
                            .isActive(true)
                            .build();
                    return repository.save(entity)
                            .flatMap(saved -> outboxService.publishEvent(
                                    USER_APARTMENT,
                                    saved.getUserId(),
                                    "USER_JOINED_APARTMENT",
                                    saved
                            ).thenReturn(saved));

                });
    }

    @Override
    public Mono<Void> leaveApartment(UUID userId, UUID apartmentId) {
        return repository.findByUserIdAndIsActiveTrue(userId)
                .flatMap(userApartment -> {
                    if (userApartment.getApartmentId().equals(apartmentId)) {
                        userApartment.setIsActive(false);
                        return repository.save(userApartment).then();
                    }
                    return Mono.empty();
                })
                .then();
    }
}
