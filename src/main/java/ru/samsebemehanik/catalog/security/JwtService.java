package ru.samsebemehanik.catalog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.samsebemehanik.catalog.domain.user.RoleType;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessTokenTtlSeconds;
    private final long refreshTokenTtlSeconds;

    public JwtService(
            @Value("${auth.jwt.secret}") String secret,
            @Value("${auth.jwt.access-token-ttl-seconds:600}") long accessTokenTtlSeconds,
            @Value("${auth.jwt.refresh-token-ttl-seconds:1209600}") long refreshTokenTtlSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenTtlSeconds = accessTokenTtlSeconds;
        this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
    }

    public String createAccessToken(Long userId, String login, RoleType role) {
        return createToken(userId, login, role, "access", accessTokenTtlSeconds);
    }

    public String createRefreshToken(Long userId, String login, RoleType role) {
        return createToken(userId, login, role, "refresh", refreshTokenTtlSeconds);
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public AuthUserPrincipal extractPrincipal(String token, String expectedType) {
        Claims claims = parse(token);
        String tokenType = claims.get("type", String.class);
        if (!expectedType.equals(tokenType)) {
            throw new IllegalArgumentException("Invalid token type");
        }

        Long userId = claims.get("uid", Long.class);
        String login = claims.getSubject();
        String role = claims.get("role", String.class);
        return new AuthUserPrincipal(userId, login, RoleType.valueOf(role));
    }

    private String createToken(Long userId, String login, RoleType role, String type, long ttlSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(login)
                .claim("uid", userId)
                .claim("role", role.name())
                .claim("type", type)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(key)
                .compact();
    }
}
