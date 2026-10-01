package com.vhre.transactionlimitsengine.infrastructure.security.dev;

import com.vhre.transactionlimitsengine.infrastructure.security.service.JwtService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dev-only stand-in for the auth microservice that issues tokens in real
 * deployments; exists only under the dev/local profiles.
 */
@RestController
@RequestMapping("/api/v1/dev/auth")
@Profile({"dev", "local"})
public class DevTokenController {

    private final JwtService jwtService;

    public DevTokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /** Record instead of Map.of: deterministic JSON field order (see Jackson config). */
    public record DevTokenResponse(String userId, String accessToken, String tokenType) {
    }

    @GetMapping("/generate-token/{userId}")
    public DevTokenResponse generateToken(@PathVariable String userId) {
        return new DevTokenResponse(userId, jwtService.generateDevToken(userId), "Bearer");
    }
}
