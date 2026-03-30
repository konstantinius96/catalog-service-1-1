package ru.samsebemehanik.catalog.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    public LoginRequest() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
