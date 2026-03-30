package ru.samsebemehanik.catalog.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9._-]+$")
    private String login;

    @NotBlank
    @Size(min = 6, max = 64)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{6,64}$")
    private String password;

    public RegisterRequest() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
