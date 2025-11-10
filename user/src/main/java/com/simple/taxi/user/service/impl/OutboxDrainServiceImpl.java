package com.simple.taxi.user.service.impl;

import com.simple.taxi.user.model.entities.OutboxEvent;
import com.simple.taxi.user.model.enums.Status;
import com.simple.taxi.user.service.OutboxDrainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static com.simple.taxi.user.model.enums.Status.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxDrainServiceImpl implements OutboxDrainService {

    private final R2dbcEntityTemplate template;

    @Scheduled(fixedDelay = 10000)
    public void requeueStuckEvents() {
        template.update(OutboxEvent.class)
                .matching(Query.query(
                        Criteria.where("status").is("NEW")
                                .and("processed_at").isNull()
                                .and("created_at").lessThan(Instant.now().minusSeconds(60))
                ))
                .apply(Update.update("processed_at", Instant.now()))
                .doOnSuccess(v -> log.info("♻️ Requeued stuck outbox events"))
                .subscribe();
    }
}