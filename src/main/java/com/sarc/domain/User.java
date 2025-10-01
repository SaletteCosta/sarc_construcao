package com.sarc.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "app_user", indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String passHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassHash() { return passHash; }
    public void setPassHash(String passHash) { this.passHash = passHash; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
