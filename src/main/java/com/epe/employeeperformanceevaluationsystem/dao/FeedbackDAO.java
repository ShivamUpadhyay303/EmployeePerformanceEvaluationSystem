
package com.epe.employeeperformanceevaluationsystem.dao;

import com.epe.employeeperformanceevaluationsystem.model.Feedback;
import com.epe.employeeperformanceevaluationsystem.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    // Add feedback
    public boolean addFeedback(Feedback feedback) throws SQLException {

        String sql = "INSERT INTO feedback (employee_id, rating, comments) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, feedback.getEmployeeId());
            statement.setInt(2, feedback.getRating());
            statement.setString(3, feedback.getComments());

            return statement.executeUpdate() > 0;
        }
    }

    // Get all feedback with employee names
    public List<Feedback> getAllFeedback() throws SQLException {

        List<Feedback> feedbackList = new ArrayList<>();

        String sql = """
                SELECT f.feedback_id, f.employee_id,
                       f.rating, f.comments, e.name
                FROM feedback f
                JOIN employees e ON f.employee_id = e.employee_id
                ORDER BY f.feedback_id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Feedback feedback = new Feedback(
                        result.getInt("feedback_id"),
                        result.getInt("employee_id"),
                        result.getInt("rating"),
                        result.getString("comments"),
                        result.getString("name")
                );

                feedbackList.add(feedback);
            }
        }

        return feedbackList;
    }

    // Get feedback for a particular employee
    public List<Feedback> getFeedbackByEmployee(int employeeId)
            throws SQLException {

        List<Feedback> feedbackList = new ArrayList<>();

        String sql = """
                SELECT f.feedback_id, f.employee_id,
                       f.rating, f.comments, e.name
                FROM feedback f
                JOIN employees e ON f.employee_id = e.employee_id
                WHERE f.employee_id = ?
                ORDER BY f.feedback_id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employeeId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Feedback feedback = new Feedback(
                            result.getInt("feedback_id"),
                            result.getInt("employee_id"),
                            result.getInt("rating"),
                            result.getString("comments"),
                            result.getString("name")
                    );

                    feedbackList.add(feedback);
                }
            }
        }

        return feedbackList;
    }
}
