package com.epe.employeeperformanceevaluationsystem.dao;

import com.epe.employeeperformanceevaluationsystem.model.PerformanceReport;
import com.epe.employeeperformanceevaluationsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceReportDAO {

    // Get performance reports of all employees (Manager)
    public List<PerformanceReport> getAllReports() throws SQLException {

        List<PerformanceReport> reports = new ArrayList<>();

        String sql = "SELECT e.employee_id, e.name, " +
                "COUNT(ev.evaluation_id) AS total_evaluations, " +
                "COALESCE(AVG(ev.rating), 0) AS average_rating " +
                "FROM employees e " +
                "LEFT JOIN evaluations ev ON e.employee_id = ev.employee_id " +
                "GROUP BY e.employee_id, e.name " +
                "ORDER BY e.employee_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                PerformanceReport report = new PerformanceReport(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getInt("total_evaluations"),
                        rs.getDouble("average_rating")
                );

                reports.add(report);
            }
        }

        return reports;
    }

    // Get performance report of a particular employee
    public PerformanceReport getReportByEmployee(int employeeId) throws SQLException {

        String sql = "SELECT e.employee_id, e.name, " +
                "COUNT(ev.evaluation_id) AS total_evaluations, " +
                "COALESCE(AVG(ev.rating), 0) AS average_rating " +
                "FROM employees e " +
                "LEFT JOIN evaluations ev ON e.employee_id = ev.employee_id " +
                "WHERE e.employee_id = ? " +
                "GROUP BY e.employee_id, e.name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employeeId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new PerformanceReport(
                            rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getInt("total_evaluations"),
                            rs.getDouble("average_rating")
                    );
                }
            }
        }

        return null;
    }
}