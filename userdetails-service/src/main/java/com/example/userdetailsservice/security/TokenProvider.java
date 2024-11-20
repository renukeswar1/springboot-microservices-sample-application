package com.example.userdetailsservice.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    @Value("${app.jwt.secret}")
    public String secret;

    @Value("${app.jwt.expiration.minutes}")
    private Long expiration;

    public String generateToken(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = customUserDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signInKey = secret.getBytes();

        return Jwts.builder()
                .header().add("typ", "JWT")
                .and()
                .signWith(Keys.hmacShaKeyFor(signInKey), Jwts.SIG.HS512)
                .expiration(Date.from(ZonedDateTime.now().plusMinutes(expiration).toInstant()))
                .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .id(UUID.randomUUID().toString())
                .issuer(TOKEN_ISSUER)
                .audience().add(TOKEN_AUDIENCE)
                .and()
                .subject(customUserDetails.getUsername())
                .claim("roles", roles)
                .claim("name", customUserDetails.getName())
                .claim("preferred_username", customUserDetails.getUsername())
                .claim("email", customUserDetails.getEmail())
                .compact();

    }

    public static final String TOKEN_ISSUER = "order-api";
    public static final String TOKEN_AUDIENCE = "order-app";
}


