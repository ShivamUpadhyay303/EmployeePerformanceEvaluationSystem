
package com.epe.employeeperformanceevaluationsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/epes_db";

    private static final String USER = "root";

    private static final String PASSWORD =
            System.getenv("EPES_DB_PASSWORD");

    // Private constructor prevents unnecessary object creation.
    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {

        // Check whether the database password is configured.
        if (PASSWORD == null || PASSWORD.isBlank()) {

            throw new SQLException(
                    "Database password is missing. "
                            + "Please configure the EPES_DB_PASSWORD environment variable."
            );
        }

        try {

            // Load the MySQL JDBC driver.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection.
            return DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                    "MySQL JDBC driver was not found. "
                            + "Please check the MySQL Connector/J dependency.",
                    e
            );
        }
    }
}
