package com.epe.employeeperformanceevaluationsystem.util;

public final class HtmlUtil {

    // Prevent object creation
    private HtmlUtil() {
    }

    // Escape special HTML characters
    public static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}