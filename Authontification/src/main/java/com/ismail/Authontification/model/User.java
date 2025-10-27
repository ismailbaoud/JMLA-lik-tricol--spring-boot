package com.ismail.Authontification.model;

import com.ismail.Authontification.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity(name = "users")
public class User {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated
    private Role role;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
