package com.simple.taxi.user.repository;

import com.simple.taxi.user.model.entities.Address;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface AddressRepository extends ReactiveCrudRepository<Address, UUID> {
    Flux<Address> findByCity(String city);
    Flux<Address> findByCityAndStreet(String city, String street);
    Flux<Address> findAllByIsDeletedFalse();
}
