package ru.samsebemehanik.catalog.dto.auth;

import ru.samsebemehanik.catalog.domain.user.RoleType;
import ru.samsebemehanik.catalog.domain.user.User;

public class UserResponse {

    private final Long id;
    private final String login;
    private final RoleType role;

    public UserResponse(Long id, String login, RoleType role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getLogin(), user.getRole());
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public RoleType getRole() {
        return role;
    }
}
