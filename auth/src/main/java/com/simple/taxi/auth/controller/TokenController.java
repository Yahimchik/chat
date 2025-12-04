package com.simple.taxi.auth.controller;

import com.simple.taxi.auth.config.argument_resolver.DeviceInfo;
import com.simple.taxi.auth.config.argument_resolver.LoggedInUserId;
import com.simple.taxi.auth.model.dto.RefreshTokenDTO;
import com.simple.taxi.auth.model.dto.RefreshTokenRequest;
import com.simple.taxi.auth.model.dto.TokenResponse;
import com.simple.taxi.auth.model.dto.ValidateResponse;
import com.simple.taxi.auth.service.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.simple.taxi.auth.config.constant.UrlConstants.*;

@RestController
@RequestMapping(TOKEN_CONTROLLER)
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping(ACTIVE_TOKEN)
    @SecurityRequirement(name = SECURITY_REQUIREMENT)
    public ResponseEntity<List<RefreshTokenDTO>> getActiveTokens(@LoggedInUserId UUID userId) {
        return ResponseEntity.ok(tokenService.getActiveTokens(userId));
    }

    @GetMapping(CHECK_TOKEN)
    public ResponseEntity<ValidateResponse> checkToken(@RequestParam String token) {
        ValidateResponse result = tokenService.checkToken(token);
        if (!result.valid()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        return ResponseEntity.ok(result);
    }

    @PostMapping(RECREATE_TOKEN)
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest request, @DeviceInfo String deviceInfo) {
        return ResponseEntity.ok(tokenService.refreshToken(request, deviceInfo));
    }
}