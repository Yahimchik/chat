package com.simple.taxi.user.service;

import com.simple.taxi.user.model.entities.Apartment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ApartmentService {

    Mono<Apartment> create(Apartment apartment);

    Mono<Apartment> findById(UUID id);

    Flux<Apartment> findAll();

    Mono<Void> delete(UUID id);

    Flux<Apartment> findByAddressId(UUID addressId);

    Mono<Apartment> update(Apartment apartment);

}
