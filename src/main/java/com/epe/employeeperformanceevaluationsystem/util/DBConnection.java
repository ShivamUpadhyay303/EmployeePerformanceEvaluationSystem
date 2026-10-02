package com.epe.employeeperformanceevaluationsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/epes_db";

    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("EPES_DB_PASSWORD");



    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the EPES database.
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver was not found.", e);
        }
    }
}
