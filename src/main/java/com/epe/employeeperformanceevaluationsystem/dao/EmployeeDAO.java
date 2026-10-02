package com.epe.employeeperformanceevaluationsystem.dao;

import com.epe.employeeperformanceevaluationsystem.model.Employee;
import com.epe.employeeperformanceevaluationsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Adds a new employee record to the database.
    public boolean addEmployee(String name, String department, String jobTitle)
            throws SQLException {

        // SQL query to insert employee details.
        // The ? symbols are placeholders for values provided later.
        String sql = "INSERT INTO employees (name, department, job_title) VALUES (?, ?, ?)";

        // Establishes a database connection and prepares the SQL statement.
        // try-with-resources automatically closes both resources.
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Assigns employee details to the SQL placeholders.
            statement.setString(1, name);
            statement.setString(2, department);
            statement.setString(3, jobTitle);

            // Executes the INSERT query and returns the number of rows inserted.
            int rowsInserted = statement.executeUpdate();

            // Returns true if at least one row was inserted; otherwise, false.
            return rowsInserted > 0;
        }
    }

    // Retrieves all employee records from the database.
    public List<Employee> getAllEmployees() throws SQLException {

        // Creates an empty list to store Employee objects.
        List<Employee> employees = new ArrayList<>();

        // SQL query to retrieve employee details.
        String sql = "SELECT employee_id, name, department, job_title FROM employees";

        // Establishes a database connection and prepares the SELECT statement.
        // try-with-resources automatically closes the connection,
        // statement, and result set when the block finishes.
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Moves through the query results one row at a time.
            while (resultSet.next()) {

                // Reads the current row and creates an Employee object.
                Employee employee = new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("department"),
                        resultSet.getString("job_title")
                );

                // Adds the Employee object to the list.
                employees.add(employee);
            }
        }

        // Returns the list containing all retrieved employees.
        return employees;
    }

    // Retrieves one employee using their employee ID.
    public Employee getEmployeeById(int employeeId) throws SQLException {

        // SQL query to find an employee with the given ID.
        String sql = "SELECT employee_id, name, department, job_title FROM employees WHERE employee_id = ?";

        // Establishes a database connection and prepares the SQL statement.
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Replaces the ? placeholder with the employee ID.
            statement.setInt(1, employeeId);

            // Executes the SELECT query and stores the result.
            try (ResultSet resultSet = statement.executeQuery()) {

                // Checks whether an employee with this ID was found.
                if (resultSet.next()) {

                    // Creates an Employee object using the retrieved row.
                    return new Employee(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("name"),
                            resultSet.getString("department"),
                            resultSet.getString("job_title")
                    );
                }
            }
        }

        // Returns null if no employee with the given ID exists.
        return null;
    }

    // Updates an employee's details using their employee ID.
    public boolean updateEmployee(int employeeId, String name,
                                  String department, String jobTitle)
            throws SQLException {

        // SQL query to update the employee record matching the given ID.
        String sql = "UPDATE employees SET name = ?, department = ?, job_title = ? WHERE employee_id = ?";

        // Establishes a database connection and prepares the SQL statement.
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Assigns the new employee details to the SQL placeholders.
            statement.setString(1, name);
            statement.setString(2, department);
            statement.setString(3, jobTitle);

            // Identifies which employee record should be updated.
            statement.setInt(4, employeeId);

            // Executes the UPDATE query and gets the number of affected rows.
            int rowsUpdated = statement.executeUpdate();

            // Returns true if at least one row was updated; otherwise, false.
            return rowsUpdated > 0;
        }
    }

    // Deletes an employee record using their employee ID.
    public boolean deleteEmployee(int employeeId) throws SQLException {

        // SQL query to delete the employee matching the given ID.
        String sql = "DELETE FROM employees WHERE employee_id = ?";

        // Establishes a database connection and prepares the SQL statement.
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Specifies which employee record should be deleted.
            statement.setInt(1, employeeId);

            // Executes the DELETE query and returns the number of affected rows.
            int rowsDeleted = statement.executeUpdate();

            // Returns true if a row was deleted; otherwise, false.
            return rowsDeleted > 0;
        }
    }
}