package com.epe.employeeperformanceevaluationsystem.dao;

import java.sql.SQLException;

public class EmployeeDAOTest {

    public static void main(String[] args) {

        EmployeeDAO employeeDAO = new EmployeeDAO();

        try {
            boolean result = employeeDAO.addEmployee(
                    "Rahul",
                    "IT",
                    "Software Developer"
            );

            if (result) {
                System.out.println("Employee added successfully!");
            } else {
                System.out.println("Employee was not added.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding employee.");
            e.printStackTrace();
        }
    }
}