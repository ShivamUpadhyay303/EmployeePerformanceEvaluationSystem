
package com.epe.employeeperformanceevaluationsystem.util;

import jakarta.servlet.http.HttpSession;

import java.security.SecureRandom;
import java.util.Base64;

public final class CsrfUtil {

    private static final String TOKEN_ATTRIBUTE = "csrfToken";

    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfUtil() {
    }

    public static String getOrCreateToken(HttpSession session) {

        String token = (String) session.getAttribute(TOKEN_ATTRIBUTE);

        if (token == null) {

            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);

            token = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(bytes);

            session.setAttribute(TOKEN_ATTRIBUTE, token);
        }

        return token;
    }

    public static boolean isValidToken(
            HttpSession session,
            String submittedToken) {

        if (session == null || submittedToken == null) {
            return false;
        }

        Object storedObject =
                session.getAttribute(TOKEN_ATTRIBUTE);

        if (!(storedObject instanceof String)) {
            return false;
        }

        String storedToken = (String) storedObject;

        return java.security.MessageDigest.isEqual(
                storedToken.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                submittedToken.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );
    }
}
