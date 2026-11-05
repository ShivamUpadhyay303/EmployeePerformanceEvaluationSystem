
package com.epe.employeeperformanceevaluationsystem.servlet;

import com.epe.employeeperformanceevaluationsystem.dao.EmployeeDAO;
import com.epe.employeeperformanceevaluationsystem.dao.EvaluationDAO;
import com.epe.employeeperformanceevaluationsystem.model.Employee;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/evaluations")
public class EvaluationServlet extends HttpServlet {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final EvaluationDAO evaluationDAO = new EvaluationDAO();

    // Check whether the current user is a Manager.
    private boolean isManager(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        return session != null
                && "MANAGER".equals(session.getAttribute("role"));
    }

    // Display employees for evaluation.
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Only Managers can access evaluations.
        if (!isManager(request)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only managers can access evaluations."
            );

            return;
        }

        try {

            List<Employee> employees = employeeDAO.getAllEmployees();

            request.setAttribute("employees", employees);

            request.getRequestDispatcher("/evaluations.jsp")
                    .forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load employees for evaluation.",
                    e
            );
        }
    }

    // Save a performance evaluation.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Only Managers can submit evaluations.
        if (!isManager(request)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only managers can submit evaluations."
            );

            return;
        }

        try {

            String employeeIdValue =
                    request.getParameter("employeeId");

            String ratingValue =
                    request.getParameter("rating");

            String criteria =
                    request.getParameter("criteria");

            String comments =
                    request.getParameter("comments");

            // Check required fields.
            if (employeeIdValue == null
                    || ratingValue == null
                    || criteria == null
                    || comments == null) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Required evaluation information is missing."
                );

                return;
            }

            // Convert input values.
            int employeeId = Integer.parseInt(employeeIdValue);
            int rating = Integer.parseInt(ratingValue);

            criteria = criteria.trim();
            comments = comments.trim();

            // Validate employee ID.
            if (employeeId <= 0) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid employee ID."
                );

                return;
            }

            // Validate rating.
            if (rating < 1 || rating > 5) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Rating must be between 1 and 5."
                );

                return;
            }

            // Validate criteria.
            if (criteria.isEmpty() || criteria.length() > 100) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Evaluation criteria must contain 1 to 100 characters."
                );

                return;
            }

            // Validate comments.
            if (comments.length() < 2 || comments.length() > 2000) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Comments must contain 2 to 2000 characters."
                );

                return;
            }

            // Verify that the employee exists.
            Employee employee =
                    employeeDAO.getEmployeeById(employeeId);

            if (employee == null) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Employee not found."
                );

                return;
            }

            // Save evaluation.
            boolean saved = evaluationDAO.addEvaluation(
                    employeeId,
                    criteria,
                    rating,
                    comments
            );

            if (saved) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/evaluations?success=saved"
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Evaluation could not be saved."
                );
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid employee ID or rating."
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to save evaluation.",
                    e
            );
        }
    }
}
