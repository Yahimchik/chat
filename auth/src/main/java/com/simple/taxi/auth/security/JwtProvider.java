package com.simple.taxi.auth.security;

import com.simple.taxi.auth.model.dto.ValidateResponse;

import java.util.List;
import java.util.UUID;

public interface JwtProvider {

    String generateAccessToken(UUID userId, List<String> roles);

    String generateRefreshToken(UUID userId);

    UUID getUserIdFromToken(String token, boolean isAccessToken);

    List<String> getRolesFromAccessToken(String token);

    ValidateResponse validateToken(String token, boolean isAccessToken);
}