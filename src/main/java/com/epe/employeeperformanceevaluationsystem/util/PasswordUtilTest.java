package com.epe.employeeperformanceevaluationsystem.util;

public class PasswordUtilTest {

    public static void main(String[] args) {

        String password = "Test@123";

        // Generate password hash
        String hashedPassword = PasswordUtil.hashPassword(password);

        // Test correct password
        boolean correct = PasswordUtil.verifyPassword(
                password, hashedPassword);

        // Test incorrect password
        boolean incorrect = PasswordUtil.verifyPassword(
                "Wrong@123", hashedPassword);

        System.out.println("Correct password: " + correct);
        System.out.println("Wrong password: " + incorrect);
    }
}
