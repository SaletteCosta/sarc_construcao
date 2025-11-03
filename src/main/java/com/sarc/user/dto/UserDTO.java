package com.sarc.user.dto;

import com.sarc.domain.Role;

public class UserDTO {
    private String name;
    private String email;
    private String passHash;
    private Role role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassHash() { return passHash; }
    public void setPassHash(String passHash) { this.passHash = passHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
