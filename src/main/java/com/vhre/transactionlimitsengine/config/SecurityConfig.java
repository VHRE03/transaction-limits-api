package com.vhre.transactionlimitsengine.config;

import com.vhre.transactionlimitsengine.infrastructure.security.filter.JwtAuthenticationFilter;
import com.vhre.transactionlimitsengine.infrastructure.security.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base security configuration: stateless REST API with CSRF disabled.
 *
 * <p>Every endpoint requires a valid Bearer JWT (validated by
 * {@code JwtAuthenticationFilter}) except the Swagger UI, the OpenAPI docs
 * and the dev-only token issuer, which is public only while a dev/local
 * profile is active. The JWT settings live in {@code application.yml} under
 * {@code app.security.jwt}.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Endpoints reachable without authentication: API documentation plus Boot's
     * error dispatch (denying /error rewrites every 404/500 as a bare 401).
     */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/error",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    /** Dev-only token issuer path; its controller exists only under dev/local profiles. */
    private static final String DEV_TOKEN_BASE_PATH = "/api/v1/dev/auth/**";

    private final Environment environment;
    private final JwtService jwtService;

    public SecurityConfig(Environment environment, JwtService jwtService) {
        this.environment = environment;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicMatchers()).permitAll()
                        .anyRequest().authenticated())
                // Without an entry point configured, Security answers 403 instead of 401
                .exceptionHandling(handling -> handling.authenticationEntryPoint(
                        (request, response, exception) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                .addFilterBefore(new JwtAuthenticationFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private String[] publicMatchers() {
        List<String> matchers = new ArrayList<>(Arrays.asList(PUBLIC_ENDPOINTS));
        // Fail closed: the issuer is public only where its @Profile-registered controller exists
        if (isDevOrLocalProfile()) {
            matchers.add(DEV_TOKEN_BASE_PATH);
        }
        return matchers.toArray(String[]::new);
    }

    private boolean isDevOrLocalProfile() {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        return activeProfiles.contains("dev") || activeProfiles.contains("local");
    }
}
