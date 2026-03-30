package ru.samsebemehanik.catalog.security;

import java.security.Principal;
import ru.samsebemehanik.catalog.domain.user.RoleType;

public class AuthUserPrincipal implements Principal {

    private final Long id;
    private final String login;
    private final RoleType role;

    public AuthUserPrincipal(Long id, String login, RoleType role) {
        this.id = id;
        this.login = login;
        this.role = role;
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

    @Override
    public String getName() {
        return login;
    }
}
