
package com.epe.employeeperformanceevaluationsystem.servlet;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/employees",
        "/evaluations",
        "/goals",
        "/feedback",
        "/reports",

        "/employee-dashboard.jsp",
        "/manager-dashboard.jsp",

        "/employees.jsp",
        "/edit-employee.jsp",
        "/evaluations.jsp",
        "/goals.jsp",
        "/feedback.jsp",
        "/performance-reports.jsp"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Prevent protected pages from being stored in browser cache.
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        // Get the existing session without creating a new one.
        HttpSession session = req.getSession(false);

        // Check whether the user is logged in.
        if (session == null) {
            res.sendRedirect(req.getContextPath() + "/");
            return;
        }

        Object roleObject = session.getAttribute("role");

        if (!(roleObject instanceof String)) {
            res.sendRedirect(req.getContextPath() + "/");
            return;
        }

        String role = (String) roleObject;

        // Only valid account roles are accepted.
        if (!"MANAGER".equals(role) && !"EMPLOYEE".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // Employees must have a valid employee ID in their session.
        if ("EMPLOYEE".equals(role)) {

            Object employeeIdObject = session.getAttribute("employeeId");

            if (!(employeeIdObject instanceof Integer)
                    || (Integer) employeeIdObject <= 0) {

                session.invalidate();

                res.sendRedirect(req.getContextPath() + "/");
                return;
            }
        }

        String path = req.getServletPath();

        // Only managers can access employee management.
        if (("/employees".equals(path)
                || "/employees.jsp".equals(path)
                || "/edit-employee.jsp".equals(path))
                && !"MANAGER".equals(role)) {

            res.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only managers can manage employees."
            );

            return;
        }

        // Only managers can access performance evaluations.
        if (("/evaluations".equals(path)
                || "/evaluations.jsp".equals(path))
                && !"MANAGER".equals(role)) {

            res.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only managers can evaluate employees."
            );

            return;
        }

        // Only managers can access the manager dashboard.
        if ("/manager-dashboard.jsp".equals(path)
                && !"MANAGER".equals(role)) {

            res.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );

            return;
        }

        // Only employees can access the employee dashboard.
        if ("/employee-dashboard.jsp".equals(path)
                && !"EMPLOYEE".equals(role)) {

            res.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );

            return;
        }

        // Both valid roles can access goals, feedback and reports.
        if (("/goals".equals(path)
                || "/goals.jsp".equals(path)
                || "/feedback".equals(path)
                || "/feedback.jsp".equals(path)
                || "/reports".equals(path)
                || "/performance-reports.jsp".equals(path))
                && !("MANAGER".equals(role) || "EMPLOYEE".equals(role))) {

            res.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );

            return;
        }

        // User is authenticated and authorized.
        chain.doFilter(request, response);
    }
}
