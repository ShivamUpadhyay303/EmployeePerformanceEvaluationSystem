
package com.epe.employeeperformanceevaluationsystem.model;

import java.sql.Timestamp;

public class User {

    private int userId;
    private String email;
    private String passwordHash;
    private String role;
    private Integer employeeId;
    private Timestamp createdAt;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String email, String passwordHash,
                String role, Integer employeeId) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.employeeId = employeeId;
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

