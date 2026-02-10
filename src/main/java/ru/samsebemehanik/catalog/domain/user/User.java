package ru.samsebemehanik.catalog.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
 public class User {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Column(nullable = false, unique = true, length = 120)
     private String login;

    @JsonProperty("password_hash")
    @Column(nullable = false, length = 255)
     private String passwordHash;

    @JsonProperty("registration_date")
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

    public Long getId() {
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
