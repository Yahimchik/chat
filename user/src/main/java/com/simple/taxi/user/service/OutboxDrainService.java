package com.simple.taxi.user.service;

public interface OutboxDrainService {
    void requeueStuckEvents();
}
