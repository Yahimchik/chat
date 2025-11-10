package com.simple.taxi.user.service.impl;

import com.simple.taxi.user.model.entities.Address;
import com.simple.taxi.user.model.enums.AggregateType;
import com.simple.taxi.user.repository.AddressRepository;
import com.simple.taxi.user.service.AddressService;
import com.simple.taxi.user.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

import static com.simple.taxi.user.model.enums.AggregateType.ADDRESS;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;
    private final OutboxService outboxService;

    @Override
    public Mono<Address> create(Address address) {
        address.setCreatedAt(Instant.now());
        address.setUpdatedAt(Instant.now());
        return repository.save(address)
                .flatMap(saved -> outboxService.publishEvent(
                        ADDRESS,
                        saved.getId(),
                        "ADDRESS_CREATED",
                        saved
                ).thenReturn(saved));

    }

    @Override
    public Mono<Address> update(Address address) {
        address.setUpdatedAt(Instant.now());
        return repository.save(address);
    }

    @Override
    public Mono<Address> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Address> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .flatMap(address -> {
                    address.setIsDeleted(true);
                    address.setDeletedAt(Instant.now());
                    return repository.save(address);
                })
                .then();
    }

    @Override
    public Flux<Address> findByCity(String city) {
        return repository.findByCity(city);
    }

    @Override
    public Flux<Address> findByStreet(String city, String street) {
        return repository.findByCityAndStreet(city, street);
    }
}
