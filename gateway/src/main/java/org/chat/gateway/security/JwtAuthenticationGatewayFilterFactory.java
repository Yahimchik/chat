package org.chat.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

//    private static final List<String> PUBLIC_ENDPOINTS = List.of(
//            "/api/v1/auth/**",
//            "/api/v1/users/register",
//            "/api/v1/passwords/**",
//            "/api/v1/otp/**",
//            "/api/v1/activation/**",
//            "/api/v1/tokens/check-token"
//    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${jwt.access.public}")
    private String accessPublicKeyPath;

    private Key publicKey;

    @PostConstruct
    public void init() throws Exception {
        this.publicKey = loadPublicKey(accessPublicKeyPath);
        log.info("Loaded JWT public key from {}", accessPublicKeyPath);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();

            log.debug("JwtAuthentication filter called for path: {}", path);

//            if (PUBLIC_ENDPOINTS.stream().anyMatch(p -> pathMatcher.match(p, path))) {
//                return chain.filter(exchange);
//            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            if (token.isBlank()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(publicKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String userId = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);

                var mutatedRequest = exchange.getRequest()
                        .mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Roles", String.join(",", roles))
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (Exception e) {
                log.error("JWT verification failed", e);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        String key = Files.readString(Path.of(path))
                .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
