package com.simple.taxi.user.repository;

import com.simple.taxi.user.model.entities.OutboxEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutboxEventRepository extends ReactiveCrudRepository<OutboxEvent, UUID> {
}
