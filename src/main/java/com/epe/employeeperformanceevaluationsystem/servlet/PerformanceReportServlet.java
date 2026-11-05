
package com.epe.employeeperformanceevaluationsystem.servlet;

import com.epe.employeeperformanceevaluationsystem.dao.PerformanceReportDAO;
import com.epe.employeeperformanceevaluationsystem.model.PerformanceReport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/reports")
public class PerformanceReportServlet extends HttpServlet {

    private final PerformanceReportDAO reportDAO =
            new PerformanceReportDAO();

    // Display performance reports.
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether the user is logged in.
        if (session == null) {

            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Object roleObject = session.getAttribute("role");

        if (!(roleObject instanceof String)) {

            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String role = (String) roleObject;

        try {

            if ("MANAGER".equals(role)) {

                // Manager can view reports of all employees.
                List<PerformanceReport> reports =
                        reportDAO.getAllReports();

                request.setAttribute("reports", reports);

            } else if ("EMPLOYEE".equals(role)) {

                // Employee can view only their own report.
                Object employeeIdObject =
                        session.getAttribute("employeeId");

                if (!(employeeIdObject instanceof Integer)
                        || (Integer) employeeIdObject <= 0) {

                    response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "Invalid employee account."
                    );

                    return;
                }

                int employeeId = (Integer) employeeIdObject;

                PerformanceReport report =
                        reportDAO.getReportByEmployee(employeeId);

                request.setAttribute("report", report);

            } else {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Access denied."
                );

                return;
            }

            // Forward to the performance reports JSP.
            request.getRequestDispatcher("/performance-reports.jsp")
                    .forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to retrieve performance reports.",
                    e
            );
        }
    }
}
