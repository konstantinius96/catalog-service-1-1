package ru.samsebemehanik.catalog.dto.auth;

import ru.samsebemehanik.catalog.domain.user.RoleType;
import ru.samsebemehanik.catalog.domain.user.User;
import java.util.UUID;

public class UserResponse {

    private final UUID id;
    private final String login;
    private final RoleType role;

    public UserResponse(UUID id, String login, RoleType role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getLogin(), user.getRole());
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public RoleType getRole() {
        return role;
    }
}
