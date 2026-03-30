package ru.samsebemehanik.catalog.security;

import java.security.Principal;
import java.util.UUID;
import ru.samsebemehanik.catalog.domain.user.RoleType;

public class AuthUserPrincipal implements Principal {

    private final UUID id;
    private final String login;
    private final RoleType role;

    public AuthUserPrincipal(UUID id, String login, RoleType role) {
        this.id = id;
        this.login = login;
        this.role = role;
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

    @Override
    public String getName() {
        return login;
    }
}
