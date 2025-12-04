package com.simple.taxi.auth.model.dto;

import java.util.List;

public record ValidateResponse(
        boolean valid,
        String userId,
        List<String> roles
) {
}