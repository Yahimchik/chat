package com.simple.taxi.user.controller;

import com.simple.taxi.user.model.entities.Address;
import com.simple.taxi.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;

    @PostMapping
    public Mono<Address> create(@RequestBody Address address) {
        return service.create(address);
    }

    @PutMapping("/{id}")
    public Mono<Address> update(@PathVariable UUID id, @RequestBody Address address) {
        address.setId(id);
        return service.update(address);
    }

    @GetMapping("/{id}")
    public Mono<Address> findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    public Flux<Address> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable UUID id) {
        return service.delete(id);
    }

    @GetMapping("/city/{city}")
    public Flux<Address> findByCity(@PathVariable String city) {
        return service.findByCity(city);
    }

    @GetMapping("/street")
    public Flux<Address> findByStreet(@RequestParam String city, @RequestParam String street) {
        return service.findByStreet(city, street);
    }
}
