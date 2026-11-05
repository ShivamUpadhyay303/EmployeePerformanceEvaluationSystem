
package com.epe.employeeperformanceevaluationsystem.dao;

import com.epe.employeeperformanceevaluationsystem.model.Goal;
import com.epe.employeeperformanceevaluationsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Types;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {

    // Add a new goal.
    public boolean addGoal(Goal goal) throws SQLException {

        String sql = "INSERT INTO goals "
                + "(employee_id, description, target_date, status) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, goal.getEmployeeId());
            statement.setString(2, goal.getDescription());

            if (goal.getTargetDate() != null) {

                statement.setDate(
                        3,
                        Date.valueOf(goal.getTargetDate())
                );

            } else {

                statement.setNull(3, Types.DATE);
            }

            statement.setString(4, goal.getStatus());

            return statement.executeUpdate() > 0;
        }
    }

    // Get all goals.
    public List<Goal> getAllGoals() throws SQLException {

        List<Goal> goals = new ArrayList<>();

        String sql = "SELECT goal_id, employee_id, description, "
                + "target_date, status FROM goals";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Date date = result.getDate("target_date");

                LocalDate targetDate =
                        (date != null) ? date.toLocalDate() : null;

                Goal goal = new Goal(
                        result.getInt("goal_id"),
                        result.getInt("employee_id"),
                        result.getString("description"),
                        targetDate,
                        result.getString("status")
                );

                goals.add(goal);
            }
        }

        return goals;
    }

    // Get goals belonging to a particular employee.
    public List<Goal> getGoalsByEmployee(int employeeId)
            throws SQLException {

        List<Goal> goals = new ArrayList<>();

        String sql = "SELECT goal_id, employee_id, description, "
                + "target_date, status FROM goals WHERE employee_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employeeId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Date date = result.getDate("target_date");

                    LocalDate targetDate =
                            (date != null) ? date.toLocalDate() : null;

                    Goal goal = new Goal(
                            result.getInt("goal_id"),
                            result.getInt("employee_id"),
                            result.getString("description"),
                            targetDate,
                            result.getString("status")
                    );

                    goals.add(goal);
                }
            }
        }

        return goals;
    }

    // Update goal status only when the goal belongs to the employee.
    public boolean updateGoalStatus(
            int goalId,
            int employeeId,
            String status) throws SQLException {

        String sql = "UPDATE goals SET status = ? "
                + "WHERE goal_id = ? AND employee_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, goalId);
            statement.setInt(3, employeeId);

            return statement.executeUpdate() > 0;
        }
    }
}
