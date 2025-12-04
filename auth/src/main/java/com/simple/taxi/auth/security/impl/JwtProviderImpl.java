package com.simple.taxi.auth.security.impl;

import com.simple.taxi.auth.model.dto.ValidateResponse;
import com.simple.taxi.auth.security.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.*;

@Component
public class JwtProviderImpl implements JwtProvider {

    private final PrivateKey accessPrivateKey;
    private final PublicKey accessPublicKey;
    private final PrivateKey refreshPrivateKey;
    private final PublicKey refreshPublicKey;

    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProviderImpl(
            @Value("${spring.jwt.access.private}") String accessPrivatePath,
            @Value("${spring.jwt.access.public}") String accessPublicPath,
            @Value("${spring.jwt.refresh.private}") String refreshPrivatePath,
            @Value("${spring.jwt.refresh.public}") String refreshPublicPath,
            @Value("${spring.jwt.access.expiration}") long accessTokenExpiration,
            @Value("${spring.jwt.refresh.expiration}") long refreshTokenExpiration
    ) throws Exception {
        this.accessPrivateKey = loadPrivateKey(accessPrivatePath);
        this.accessPublicKey = loadPublicKey(accessPublicPath);
        this.refreshPrivateKey = loadPrivateKey(refreshPrivatePath);
        this.refreshPublicKey = loadPublicKey(refreshPublicPath);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    private PrivateKey loadPrivateKey(String path) throws Exception {
        String key = Files.readString(Paths.get(path))
                .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        String key = Files.readString(Paths.get(path))
                .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    @Override
    public String generateAccessToken(UUID userId, List<String> roles) {
        Instant now = Instant.now();
        Map<String, Object> claims = Map.of("roles", roles);
        return Jwts.builder()
                .setSubject(userId.toString())
                .addClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(accessTokenExpiration)))
                .signWith(accessPrivateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(UUID userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(refreshTokenExpiration)))
                .signWith(refreshPrivateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public UUID getUserIdFromToken(String token, boolean isAccessToken) {
        Claims claims = parseClaims(token, isAccessToken);
        return UUID.fromString(claims.getSubject());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromAccessToken(String token) {
        Claims claims = parseClaims(token, true);
        return claims.get("roles", List.class);
    }

    @Override
    public ValidateResponse validateToken(String token, boolean isAccessToken) {
        try {
            Claims claims = parseClaims(token, isAccessToken);
            String userId = claims.getSubject();
            if (userId == null || userId.isBlank() || claims.getExpiration().before(Date.from(Instant.now()))) {
                return new ValidateResponse(false, null, null);
            }
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);
            return new ValidateResponse(true, userId, roles);
        } catch (JwtException | IllegalArgumentException e) {
            return new ValidateResponse(false, null, null);
        }
    }

    private Claims parseClaims(String token, boolean isAccessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(isAccessToken ? accessPublicKey : refreshPublicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}