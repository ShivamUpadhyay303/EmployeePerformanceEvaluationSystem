
package com.epe.employeeperformanceevaluationsystem.servlet;

import com.epe.employeeperformanceevaluationsystem.model.Feedback;
import com.epe.employeeperformanceevaluationsystem.dao.FeedbackDAO;
import com.epe.employeeperformanceevaluationsystem.dao.EmployeeDAO;
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

@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet {

    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    // Display feedback.
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

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

                // Manager can view all feedback and employees.
                List<Feedback> feedbackList =
                        feedbackDAO.getAllFeedback();

                List<Employee> employees =
                        employeeDAO.getAllEmployees();

                request.setAttribute("feedbackList", feedbackList);
                request.setAttribute("employees", employees);

            } else if ("EMPLOYEE".equals(role)) {

                // Employee can view only their own feedback.
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

                List<Feedback> feedbackList =
                        feedbackDAO.getFeedbackByEmployee(employeeId);

                request.setAttribute("feedbackList", feedbackList);

            } else {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Access denied."
                );

                return;
            }

            // Forward to JSP inside WEB-INF.
            request.getRequestDispatcher("/WEB-INF/views/feedback.jsp")
                    .forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to retrieve feedback.",
                    e
            );
        }
    }

    // Manager submits feedback.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        // Only managers can submit feedback.
        if (session == null
                || !"MANAGER".equals(session.getAttribute("role"))) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only managers can submit feedback."
            );

            return;
        }

        try {

            String employeeIdValue =
                    request.getParameter("employeeId");

            String ratingValue =
                    request.getParameter("rating");

            String comments =
                    request.getParameter("comments");

            // Check required fields.
            if (employeeIdValue == null
                    || ratingValue == null
                    || comments == null) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Required feedback information is missing."
                );

                return;
            }

            int employeeId = Integer.parseInt(employeeIdValue);
            int rating = Integer.parseInt(ratingValue);

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

            // Validate comments.
            if (comments.length() < 2
                    || comments.length() > 2000) {

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

            // Create feedback object.
            Feedback feedback = new Feedback(
                    0,
                    employeeId,
                    rating,
                    comments
            );

            // Save feedback and verify the result.
            boolean saved = feedbackDAO.addFeedback(feedback);

            if (!saved) {

                throw new ServletException(
                        "Feedback could not be saved."
                );
            }

            // Redirect after successful insertion.
            response.sendRedirect(
                    request.getContextPath() + "/feedback?success=saved"
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid employee ID or rating."
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to save feedback.",
                    e
            );
        }
    }
}
