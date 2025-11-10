package com.simple.taxi.user.service;

import com.simple.taxi.user.model.entities.Address;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AddressService {
    Mono<Address> create(Address address);

    Mono<Address> update(Address address);

    Mono<Address> findById(UUID id);

    Flux<Address> findAll();

    Mono<Void> delete(UUID id);

    Flux<Address> findByCity(String city);

    Flux<Address> findByStreet(String city, String street);
}
