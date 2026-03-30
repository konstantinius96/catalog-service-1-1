package ru.samsebemehanik.catalog.service.auth;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.samsebemehanik.catalog.domain.user.RoleType;
import ru.samsebemehanik.catalog.domain.user.User;
import ru.samsebemehanik.catalog.dto.auth.AuthResponse;
import ru.samsebemehanik.catalog.dto.auth.LoginRequest;
import ru.samsebemehanik.catalog.dto.auth.RegisterRequest;
import ru.samsebemehanik.catalog.dto.auth.TokenResponse;
import ru.samsebemehanik.catalog.dto.auth.UserResponse;
import ru.samsebemehanik.catalog.repository.UserRepository;
import ru.samsebemehanik.catalog.security.AuthUserPrincipal;
import ru.samsebemehanik.catalog.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final long refreshTokenTtlSeconds;
    private final boolean refreshCookieSecure;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${auth.jwt.refresh-token-ttl-seconds:1209600}") long refreshTokenTtlSeconds,
            @Value("${auth.refresh-cookie.secure:true}") boolean refreshCookieSecure
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
        this.refreshCookieSecure = refreshCookieSecure;
    }

    public AuthResult register(RegisterRequest request) {
        userRepository.findByLogin(request.getLogin()).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login already exists");
        });

        User user = userRepository.save(new User(
                request.getLogin(),
                passwordEncoder.encode(request.getPassword()),
                RoleType.USER
        ));

        return buildAuthResult(user);
    }

    public AuthResult login(LoginRequest request) {
        User user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return buildAuthResult(user);
    }

    public TokenResult refresh(Cookie[] cookies) {
        String refreshToken = extractRefreshToken(cookies)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token not found"));

        AuthUserPrincipal principal;
        try {
            principal = jwtService.extractPrincipal(refreshToken, "refresh");
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        User user = userRepository.findByLogin(principal.getLogin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        String newAccessToken = jwtService.createAccessToken(user.getId(), user.getLogin(), user.getRole());
        return new TokenResult(new TokenResponse(newAccessToken), buildRefreshCookie(refreshToken));
    }

    public ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(0)
                .build();
    }

    private AuthResult buildAuthResult(User user) {
        String accessToken = jwtService.createAccessToken(user.getId(), user.getLogin(), user.getRole());
        String refreshToken = jwtService.createRefreshToken(user.getId(), user.getLogin(), user.getRole());

        return new AuthResult(
                new AuthResponse(accessToken, UserResponse.from(user)),
                buildRefreshCookie(refreshToken)
        );
    }

    private ResponseCookie buildRefreshCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(refreshTokenTtlSeconds)
                .build();
    }

    private Optional<String> extractRefreshToken(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> REFRESH_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public record AuthResult(AuthResponse response, ResponseCookie refreshCookie) {}

    public record TokenResult(TokenResponse response, ResponseCookie refreshCookie) {}
}
