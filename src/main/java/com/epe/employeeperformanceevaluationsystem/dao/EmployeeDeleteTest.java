package com.epe.employeeperformanceevaluationsystem.dao;

import com.epe.employeeperformanceevaluationsystem.model.Employee;

import java.sql.SQLException;
import java.util.List;

public class EmployeeDeleteTest {

    public static void main(String[] args) {

        EmployeeDAO employeeDAO = new EmployeeDAO();

        try {
            // Adds a temporary employee for testing deletion.
            employeeDAO.addEmployee(
                    "DeleteTestEmployee", "Testing", "Temporary"
            );

            // Retrieves all employees to find the temporary employee's ID.
            List<Employee> employees = employeeDAO.getAllEmployees();

            int temporaryEmployeeId = -1;

            for (Employee employee : employees) {
                if (employee.getName().equals("DeleteTestEmployee")) {
                    temporaryEmployeeId = employee.getEmployeeId();
                    break;
                }
            }

            // Deletes the temporary employee using their ID.
            if (temporaryEmployeeId != -1) {
                boolean deleted = employeeDAO.deleteEmployee(temporaryEmployeeId);

                if (deleted) {
                    System.out.println("Temporary employee deleted successfully!");
                } else {
                    System.out.println("Employee was not deleted.");
                }

                // Checks whether the employee still exists.
                Employee deletedEmployee =
                        employeeDAO.getEmployeeById(temporaryEmployeeId);

                if (deletedEmployee == null) {
                    System.out.println("Deletion verified: Employee not found.");
                } else {
                    System.out.println("Deletion verification failed.");
                }

            } else {
                System.out.println("Temporary employee was not found.");
            }

        } catch (SQLException e) {
            System.out.println("Database operation failed.");
            e.printStackTrace();
        }
    }
}