
package com.epe.employeeperformanceevaluationsystem.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.MessageDigest;
import java.security.SecureRandom;

import java.util.Arrays;
import java.util.Base64;

public class PasswordUtil {

    private static final int ITERATIONS = 600000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    private static final SecureRandom RANDOM = new SecureRandom();

    // Generate a secure password hash.
    public static String hashPassword(String password) {

        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null.");
        }

        char[] passwordChars = password.toCharArray();
        byte[] salt = new byte[SALT_LENGTH];
        byte[] hash = null;

        PBEKeySpec spec = null;

        try {

            // Generate a random salt.
            RANDOM.nextBytes(salt);

            // Create password hashing specification.
            spec = new PBEKeySpec(
                    passwordChars,
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            // Generate password hash.
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            hash = factory.generateSecret(spec).getEncoded();

            // Convert salt and hash into Base64 strings.
            return "pbkdf2$" + ITERATIONS + "$"
                    + Base64.getEncoder().encodeToString(salt) + "$"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Password hashing failed.",
                    e
            );

        } finally {

            // Clear sensitive data.
            Arrays.fill(passwordChars, '\0');

            if (spec != null) {
                spec.clearPassword();
            }

            if (hash != null) {
                Arrays.fill(hash, (byte) 0);
            }
        }
    }

    // Verify a password against its stored hash.
    public static boolean verifyPassword(
            String password,
            String storedHash) {

        if (password == null || storedHash == null) {
            return false;
        }

        char[] passwordChars = password.toCharArray();
        byte[] actualHash = null;

        PBEKeySpec spec = null;

        try {

            // Split the stored hash into its components.
            String[] parts = storedHash.split("\\$", -1);

            if (parts.length != 4 || !"pbkdf2".equals(parts[0])) {
                return false;
            }

            // Read iteration count.
            int iterations = Integer.parseInt(parts[1]);

            if (iterations < 100000 || iterations > 1000000) {
                return false;
            }

            // Decode stored salt and hash.
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);

            // Validate salt and hash lengths.
            if (salt.length != SALT_LENGTH
                    || expectedHash.length != KEY_LENGTH / 8) {

                return false;
            }

            // Create password hashing specification.
            spec = new PBEKeySpec(
                    passwordChars,
                    salt,
                    iterations,
                    KEY_LENGTH
            );

            // Generate hash from entered password.
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            actualHash = factory.generateSecret(spec).getEncoded();

            // Compare hashes securely.
            return MessageDigest.isEqual(expectedHash, actualHash);

        } catch (Exception e) {

            // Invalid hash format or verification failure.
            return false;

        } finally {

            // Clear sensitive data.
            Arrays.fill(passwordChars, '\0');

            if (spec != null) {
                spec.clearPassword();
            }

            if (actualHash != null) {
                Arrays.fill(actualHash, (byte) 0);
            }
        }
    }
}
