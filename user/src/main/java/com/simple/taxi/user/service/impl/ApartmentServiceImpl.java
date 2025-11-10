package com.simple.taxi.user.service.impl;

import com.simple.taxi.user.model.entities.Apartment;
import com.simple.taxi.user.model.enums.AggregateType;
import com.simple.taxi.user.repository.ApartmentRepository;
import com.simple.taxi.user.service.ApartmentService;
import com.simple.taxi.user.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

import static com.simple.taxi.user.model.enums.AggregateType.APARTMENT;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository repository;
    private final OutboxService outboxService;

    @Override
    public Mono<Apartment> create(Apartment apartment) {
        apartment.setCreatedAt(Instant.now());
        apartment.setUpdatedAt(Instant.now());
        return repository.save(apartment)
                .flatMap(saved -> outboxService.publishEvent(
                        APARTMENT,
                        saved.getId(),
                        "APARTMENT_CREATED",
                        saved
                ).thenReturn(saved));

    }

    @Override
    public Mono<Apartment> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Apartment> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .flatMap(apartment -> {
                    apartment.setIsDeleted(true);
                    apartment.setDeletedAt(Instant.now());
                    return repository.save(apartment);
                })
                .then();
    }

    @Override
    public Flux<Apartment> findByAddressId(UUID addressId) {
        return repository.findByAddressId(addressId);
    }

    @Override
    public Mono<Apartment> update(Apartment apartment) {
        apartment.setUpdatedAt(Instant.now());
        return repository.save(apartment);
    }
}
