
package com.epe.employeeperformanceevaluationsystem.dao;

import com.epe.employeeperformanceevaluationsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EvaluationDAO {

    // Saves an employee performance evaluation.
    public boolean addEvaluation(int employeeId, String criteria,
                                 int rating, String comments)
            throws SQLException {

        String sql = "INSERT INTO evaluations "
                + "(employee_id, criteria, rating, comments) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employeeId);
            statement.setString(2, criteria);
            statement.setInt(3, rating);
            statement.setString(4, comments);

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;
        }
    }
}
