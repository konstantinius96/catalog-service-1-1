package ru.samsebemehanik.catalog.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
 public class User {
 
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
     private UUID id;

    @Column(nullable = false, unique = true, length = 120)
     private String login;

    @JsonIgnore
    @Column(nullable = false, length = 255)
     private String passwordHash;

    @Column(name = "registration_date", nullable = false)
     private LocalDateTime registrationDate;
  
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
     private RoleType role;

    protected User() {
    }

    public User(String login, String passwordHash, RoleType role) {
             this(login, passwordHash, LocalDateTime.now(), role);
    }

    public User(String login, String passwordHash, LocalDateTime registrationDate, RoleType role) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.registrationDate = registrationDate;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public RoleType getRole() {
        return role;
    }

 }
