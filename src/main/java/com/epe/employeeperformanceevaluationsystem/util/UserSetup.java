
package com.epe.employeeperformanceevaluationsystem.util;

import com.epe.employeeperformanceevaluationsystem.dao.UserDAO;
import com.epe.employeeperformanceevaluationsystem.model.User;

import java.io.Console;
import java.sql.SQLException;
import java.util.Scanner;

public class UserSetup {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();
        Scanner sc = new Scanner(System.in);

        try {

            System.out.println("===== EPES USER SETUP =====");

            // Read email.
            System.out.print("Enter email: ");
            String email = sc.nextLine().trim();

            // Read password without displaying it.
            String password;

            Console console = System.console();

            if (console != null) {

                char[] passwordChars =
                        console.readPassword("Enter password: ");

                password = new String(passwordChars);

                java.util.Arrays.fill(passwordChars, '\0');

            } else {

                // Fallback for IDE consoles that do not support Console.
                System.out.println(
                        "Note: Password input may be visible in this console."
                );

                System.out.print("Enter password: ");
                password = sc.nextLine();
            }

            // Read role.
            System.out.print("Enter role (MANAGER/EMPLOYEE): ");
            String role = sc.nextLine().trim().toUpperCase();

            Integer employeeId = null;

            // Validate email.
            if (email.isEmpty()
                    || email.length() > 150
                    || !email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )) {

                System.out.println("Please enter a valid email address.");
                return;
            }

            // Validate password.
            if (password.length() < 8 || password.length() > 128) {

                System.out.println(
                        "Password must contain between 8 and 128 characters."
                );

                return;
            }

            // Validate role.
            if (!role.equals("MANAGER")
                    && !role.equals("EMPLOYEE")) {

                System.out.println("Invalid role.");
                return;
            }

            // Employee account must have an employee ID.
            if (role.equals("EMPLOYEE")) {

                System.out.print("Enter employee ID: ");

                employeeId = Integer.parseInt(sc.nextLine().trim());

                if (employeeId <= 0) {

                    System.out.println(
                            "Employee ID must be a positive number."
                    );

                    return;
                }
            }

            // Check whether email already exists.
            if (userDAO.findByEmail(email) != null) {

                System.out.println(
                        "An account with this email already exists."
                );

                return;
            }

            // Hash password before saving it.
            String hashedPassword =
                    PasswordUtil.hashPassword(password);

            User user = new User(
                    email,
                    hashedPassword,
                    role,
                    employeeId
            );

            // Save account in database.
            boolean added = userDAO.addUser(user);

            if (added) {

                System.out.println(
                        "Account created successfully!"
                );

            } else {

                System.out.println(
                        "Account could not be created."
                );
            }

        } catch (NumberFormatException e) {

            System.out.println(
                    "Employee ID must be a valid number."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database error while creating account."
            );

            e.printStackTrace();

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Account validation failed: " + e.getMessage()
            );
        }
    }
}
