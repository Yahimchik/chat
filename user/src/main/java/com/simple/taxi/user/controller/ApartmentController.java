package com.simple.taxi.user.controller;

import com.simple.taxi.user.model.entities.Apartment;
import com.simple.taxi.user.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService service;

    @PostMapping
    public Mono<Apartment> create(@RequestBody Apartment apartment) {
        return service.create(apartment);
    }

    @PutMapping("/{id}")
    public Mono<Apartment> update(@PathVariable UUID id, @RequestBody Apartment apartment) {
        apartment.setId(id);
        return service.update(apartment);
    }

    @GetMapping("/{id}")
    public Mono<Apartment> findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    public Flux<Apartment> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable UUID id) {
        return service.delete(id);
    }

    @GetMapping("/address/{addressId}")
    public Flux<Apartment> findByAddress(@PathVariable UUID addressId) {
        return service.findByAddressId(addressId);
    }
}
