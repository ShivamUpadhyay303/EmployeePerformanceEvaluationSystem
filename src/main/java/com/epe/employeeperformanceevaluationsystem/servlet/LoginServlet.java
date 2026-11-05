
package com.epe.employeeperformanceevaluationsystem.servlet;

import com.epe.employeeperformanceevaluationsystem.dao.UserDAO;
import com.epe.employeeperformanceevaluationsystem.model.User;
import com.epe.employeeperformanceevaluationsystem.util.PasswordUtil;
import com.epe.employeeperformanceevaluationsystem.util.CsrfUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String selectedRole = request.getParameter("role");

        // Validate required fields.
        if (email == null || password == null || selectedRole == null
                || email.trim().isEmpty() || password.isEmpty()) {

            redirectToLogin(request, response);
            return;
        }

        email = email.trim();

        // Validate email length.
        if (email.length() > 150) {
            redirectToLogin(request, response);
            return;
        }

        // Accept only permitted account types.
        if (!"EMPLOYEE".equals(selectedRole)
                && !"MANAGER".equals(selectedRole)) {

            redirectToLogin(request, response);
            return;
        }

        try {

            UserDAO userDAO = new UserDAO();

            User user = userDAO.findByEmail(email);

            // Verify password.
            if (user == null
                    || !PasswordUtil.verifyPassword(
                    password,
                    user.getPasswordHash())) {

                redirectToLogin(request, response);
                return;
            }

            // Verify selected role.
            if (!selectedRole.equals(user.getRole())) {

                redirectToLogin(request, response);
                return;
            }

            // Verify Employee ID.
            if ("EMPLOYEE".equals(user.getRole())) {

                Integer employeeId = user.getEmployeeId();

                if (employeeId == null || employeeId <= 0) {
                    redirectToLogin(request, response);
                    return;
                }
            }

            // Retrieve employee name.
            String employeeName = null;

            if ("EMPLOYEE".equals(user.getRole())) {

                employeeName = userDAO.getEmployeeName(
                        user.getEmployeeId()
                );

                if (employeeName == null || employeeName.trim().isEmpty()) {

                    redirectToLogin(request, response);
                    return;
                }
            }

            // Invalidate old session to prevent session fixation.
            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Create a fresh authenticated session.
            HttpSession session = request.getSession(true);

            // Generate a fresh CSRF token for the new session.
            CsrfUtil.getOrCreateToken(session);

            // Store authenticated user information.
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("role", user.getRole());
            session.setAttribute("employeeId", user.getEmployeeId());

            // Store employee name in session.
            if ("EMPLOYEE".equals(user.getRole())) {
                session.setAttribute("employeeName", employeeName);
            }

            // Redirect according to account type.
            if ("MANAGER".equals(user.getRole())) {

                response.sendRedirect(
                        request.getContextPath() + "/manager-dashboard.jsp"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath() + "/employee-dashboard.jsp"
                );
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to process login.",
                    e
            );
        }
    }

    private void redirectToLogin(HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath() + "/?error=invalid"
        );
    }
}
