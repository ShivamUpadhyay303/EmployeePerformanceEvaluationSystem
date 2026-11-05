
package com.epe.employeeperformanceevaluationsystem.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Get the existing session without creating a new one.
        HttpSession session = request.getSession(false);

        // Invalidate the session if it exists.
        if (session != null) {
            session.invalidate();
        }

        // Prevent browser caching after logout.
        response.setHeader(
                "Cache-Control",
                "no-store, no-cache, must-revalidate"
        );

        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Redirect to the login page.
        response.sendRedirect(request.getContextPath() + "/");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Logout must happen through POST, not GET.
        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Please use the logout button."
        );
    }
}
