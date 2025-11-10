package com.simple.taxi.user.repository;

import com.simple.taxi.user.model.entities.Apartment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ApartmentRepository extends ReactiveCrudRepository<Apartment, UUID> {
    Flux<Apartment> findByAddressId(UUID addressId);
    Flux<Apartment> findAllByIsDeletedFalse();
}
