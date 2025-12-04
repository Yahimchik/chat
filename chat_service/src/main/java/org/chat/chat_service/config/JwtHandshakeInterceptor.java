package org.chat.chat_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chat_service.model.entities.StompPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Value("${jwt.access.public}")
    private String accessPublicKeyPath;

    private Key publicKey;

    @PostConstruct
    public void init() throws Exception {
        this.publicKey = loadPublicKey(accessPublicKeyPath);
        log.info("Loaded JWT public key from {}", accessPublicKeyPath);
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        log.info("BeforeHandshake called, URI: {}", request.getURI());
        String query = request.getURI().getQuery();

        if (query == null || !query.startsWith("token=")) {
            return false;
        }

        String token = query.substring("token=".length());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);
        attributes.put("principal", new StompPrincipal(userId, roles));

        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request,
                               @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler,
                               Exception exception) {
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