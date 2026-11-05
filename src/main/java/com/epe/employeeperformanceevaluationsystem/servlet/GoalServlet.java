
package com.epe.employeeperformanceevaluationsystem.servlet;

import com.epe.employeeperformanceevaluationsystem.dao.GoalDAO;
import com.epe.employeeperformanceevaluationsystem.dao.EmployeeDAO;
import com.epe.employeeperformanceevaluationsystem.model.Goal;
import com.epe.employeeperformanceevaluationsystem.model.Employee;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.util.List;

@WebServlet("/goals")
public class GoalServlet extends HttpServlet {

    private final GoalDAO goalDAO = new GoalDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    // Display goals.
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

                // Manager can view all goals and employees.
                List<Goal> goals = goalDAO.getAllGoals();
                List<Employee> employees = employeeDAO.getAllEmployees();

                request.setAttribute("goals", goals);
                request.setAttribute("employees", employees);

            } else if ("EMPLOYEE".equals(role)) {

                // Employee can view only their own goals.
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

                List<Goal> goals =
                        goalDAO.getGoalsByEmployee(employeeId);

                request.setAttribute("goals", goals);

            } else {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Access denied."
                );

                return;
            }

            // Forward to JSP inside WEB-INF.
            request.getRequestDispatcher("/WEB-INF/views/goals.jsp")
                    .forward(request, response);

        } catch (SQLException e) {

            throw new ServletException("Unable to retrieve goals.", e);
        }
    }

    // Add a goal or update its status.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

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

                // Manager assigns a new goal.

                String employeeIdValue =
                        request.getParameter("employeeId");

                String description =
                        request.getParameter("description");

                String dateValue =
                        request.getParameter("targetDate");

                if (employeeIdValue == null
                        || description == null) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Required goal information is missing."
                    );

                    return;
                }

                int employeeId =
                        Integer.parseInt(employeeIdValue);

                description = description.trim();

                // Validate employee ID.
                if (employeeId <= 0) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid employee ID."
                    );

                    return;
                }

                // Validate goal description.
                if (description.length() < 2
                        || description.length() > 2000) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Goal description must contain 2 to 2000 characters."
                    );

                    return;
                }

                // Check whether employee exists.
                Employee employee =
                        employeeDAO.getEmployeeById(employeeId);

                if (employee == null) {

                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Employee not found."
                    );

                    return;
                }

                // Target date is optional.
                LocalDate targetDate = null;

                if (dateValue != null && !dateValue.isBlank()) {
                    targetDate = LocalDate.parse(dateValue);
                }

                Goal goal = new Goal(
                        0,
                        employeeId,
                        description,
                        targetDate,
                        "Pending"
                );

                goalDAO.addGoal(goal);

            } else if ("EMPLOYEE".equals(role)) {

                // Employee updates the status of their own goal.

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

                String goalIdValue =
                        request.getParameter("goalId");

                String status =
                        request.getParameter("status");

                if (goalIdValue == null || status == null) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Goal information is missing."
                    );

                    return;
                }

                int goalId = Integer.parseInt(goalIdValue);

                // Validate goal ID.
                if (goalId <= 0) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid goal ID."
                    );

                    return;
                }

                // Accept only permitted goal statuses.
                if (!"Pending".equals(status)
                        && !"In Progress".equals(status)
                        && !"Completed".equals(status)) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid goal status."
                    );

                    return;
                }

                // Check whether the goal belongs to this employee.
                List<Goal> employeeGoals =
                        goalDAO.getGoalsByEmployee(employeeId);

                boolean belongsToEmployee =
                        employeeGoals.stream()
                                .anyMatch(goal ->
                                        goal.getGoalId() == goalId
                                );

                if (!belongsToEmployee) {

                    response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "You cannot update another employee's goal."
                    );

                    return;
                }

                // Ownership is also checked at the DAO/database level.
                goalDAO.updateGoalStatus(
                        goalId,
                        employeeId,
                        status
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Access denied."
                );

                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/goals"
            );

        } catch (SQLException e) {

            throw new ServletException("Unable to save goal.", e);

        } catch (NumberFormatException | DateTimeException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid goal information."
            );
        }
    }
}
