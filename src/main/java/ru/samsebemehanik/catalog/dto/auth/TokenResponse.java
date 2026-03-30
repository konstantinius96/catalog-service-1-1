package ru.samsebemehanik.catalog.dto.auth;

public class TokenResponse {

    private final String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
