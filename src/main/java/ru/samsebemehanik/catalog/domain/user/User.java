package ru.samsebemehanik.catalog.domain.user;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "users")
 public class User {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Column(nullable = false, unique = true, length = 120)
     private String login;

    @Column(nullable = false, length = 255)
     private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
     private RoleType role;

    @Version
    private Long version;

    protected User() {
    }

    public User(String login, String passwordHash, RoleType role) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public RoleType getRole() {
        return role;
    }

    public Long getVersion() {
        return version;
    }
 }
