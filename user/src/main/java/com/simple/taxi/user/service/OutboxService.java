package com.simple.taxi.user.service;

import com.simple.taxi.user.model.enums.AggregateType;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OutboxService {
    Mono<Void> publishEvent(AggregateType aggregateType, UUID aggregateId, String eventType, Object payload);
}
