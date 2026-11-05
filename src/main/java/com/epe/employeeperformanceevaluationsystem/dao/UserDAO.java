
package com.epe.employeeperformanceevaluationsystem.dao;

import com.epe.employeeperformanceevaluationsystem.model.User;
import com.epe.employeeperformanceevaluationsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class UserDAO {

    // Find a user using their email address.
    public User findByEmail(String email) throws SQLException {

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT user_id, email, password_hash, role, "
                + "employee_id, created_at "
                + "FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = new User();

                    user.setUserId(rs.getInt("user_id"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRole(rs.getString("role"));

                    // Handle nullable employee_id.
                    int employeeId = rs.getInt("employee_id");

                    if (rs.wasNull()) {
                        user.setEmployeeId(null);
                    } else {
                        user.setEmployeeId(employeeId);
                    }

                    user.setCreatedAt(rs.getTimestamp("created_at"));

                    return user;
                }
            }
        }

        return null;
    }

    // Get employee name using employee ID.
    public String getEmployeeName(int employeeId) throws SQLException {

        String sql = "SELECT name FROM employees WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }

        return null;
    }

    // Insert a new user into the database.
    public boolean addUser(User user) throws SQLException {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        String email = user.getEmail();
        String passwordHash = user.getPasswordHash();
        String role = user.getRole();
        Integer employeeId = user.getEmployeeId();

        // Validate email.
        if (email == null || email.trim().isEmpty()
                || email.trim().length() > 150) {

            throw new IllegalArgumentException("Invalid email address.");
        }

        // Validate password hash.
        if (passwordHash == null || passwordHash.trim().isEmpty()
                || passwordHash.length() > 255) {

            throw new IllegalArgumentException("Invalid password hash.");
        }

        // Validate role.
        if (!"EMPLOYEE".equals(role) && !"MANAGER".equals(role)) {

            throw new IllegalArgumentException("Invalid user role.");
        }

        // Validate employee ID.
        if ("EMPLOYEE".equals(role)
                && (employeeId == null || employeeId <= 0)) {

            throw new IllegalArgumentException(
                    "Employee account must have a valid employee ID.");
        }

        if ("MANAGER".equals(role) && employeeId != null) {

            throw new IllegalArgumentException(
                    "Manager account should not have an employee ID.");
        }

        String sql = "INSERT INTO users "
                + "(email, password_hash, role, employee_id) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim());
            ps.setString(2, passwordHash);
            ps.setString(3, role);

            if (employeeId == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, employeeId);
            }

            return ps.executeUpdate() > 0;
        }
    }
}
