package com.epe.employeeperformanceevaluationsystem.dao;

import com.epe.employeeperformanceevaluationsystem.model.Employee;

import java.sql.SQLException;
import java.util.List;

public class EmployeeReadTest {

    public static void main(String[] args) {

        // Creates an object of EmployeeDAO to access database operations.
        EmployeeDAO employeeDAO = new EmployeeDAO();

        try {
            // Retrieves all employee records from the database.
            List<Employee> employees = employeeDAO.getAllEmployees();

            // Displays each employee's details.
            for (Employee employee : employees) {
                System.out.println("Employee ID: " + employee.getEmployeeId());
                System.out.println("Name: " + employee.getName());
                System.out.println("Department: " + employee.getDepartment());
                System.out.println("Job Title: " + employee.getJobTitle());
                System.out.println("-------------------------");
            }

            Employee employee = employeeDAO.getEmployeeById(1);

            if (employee != null) {
                System.out.println("Employee found!");
                System.out.println("Name: " + employee.getName());
                System.out.println("Department: " + employee.getDepartment());
                System.out.println("Job Title: " + employee.getJobTitle());
            } else {
                System.out.println("Employee not found.");
            }

            // Updates Rahul's job title using employee ID 1.
            boolean updated = employeeDAO.updateEmployee(
                    1, "Rahul", "IT", "Senior Software Developer"
            );

            // Displays whether the update was successful.
            if (updated) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("Employee was not updated.");
            }

            // Retrieves the updated employee record to verify the change.
            Employee updatedEmployee = employeeDAO.getEmployeeById(1);

            if (updatedEmployee != null) {
                System.out.println("Updated Job Title: " + updatedEmployee.getJobTitle());
            }

        }
        catch (SQLException e) {
            // Displays an error if the database operation fails.
            System.out.println("Error retrieving employees.");
            e.printStackTrace();
        }
    }
}