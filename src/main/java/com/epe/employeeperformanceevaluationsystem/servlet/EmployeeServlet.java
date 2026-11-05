
package com.epe.employeeperformanceevaluationsystem.servlet;

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

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    // Check whether the current user is a Manager.
    private boolean isManager(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && "MANAGER".equals(session.getAttribute("role"));
    }

    // Display employees or open employee edit page.
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isManager(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only managers can manage employees."
            );
            return;
        }

        String idParameter = request.getParameter("id");

        try {
            if (idParameter != null) {

                int employeeId = Integer.parseInt(idParameter);

                if (employeeId <= 0) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Employee ID must be positive."
                    );
                    return;
                }

                Employee employee =
                        employeeDAO.getEmployeeById(employeeId);

                if (employee == null) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Employee not found."
                    );
                    return;
                }

                request.setAttribute("employee", employee);

                request.getRequestDispatcher("/edit-employee.jsp")
                        .forward(request, response);

            } else {

                List<Employee> employees =
                        employeeDAO.getAllEmployees();

                request.setAttribute("employees", employees);

                request.getRequestDispatcher("/employees.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid employee ID."
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to retrieve employee data.",
                    e
            );
        }
    }

    // Add, update or delete an employee.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!isManager(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only managers can manage employees."
            );
            return;
        }

        String action = request.getParameter("action");

        try {

            // Delete employee.
            if ("delete".equals(action)) {

                String employeeIdValue =
                        request.getParameter("employeeId");

                if (employeeIdValue == null) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Employee ID is required."
                    );
                    return;
                }

                int employeeId = Integer.parseInt(employeeIdValue);

                if (employeeId <= 0) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid employee ID."
                    );
                    return;
                }

                if (employeeDAO.getEmployeeById(employeeId) == null) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Employee not found."
                    );
                    return;
                }

                boolean deleted =
                        employeeDAO.deleteEmployee(employeeId);

                if (!deleted) {
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Employee could not be deleted."
                    );
                    return;
                }

                response.sendRedirect(
                        request.getContextPath()
                                + "/employees?success=deleted"
                );
                return;
            }

            // Accept only add and update actions.
            if (!"add".equals(action) && !"update".equals(action)) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid employee action."
                );
                return;
            }

            String name = request.getParameter("name");
            String department = request.getParameter("department");
            String jobTitle = request.getParameter("jobTitle");

            // Check required fields.
            if (name == null || department == null || jobTitle == null) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "All employee fields are required."
                );
                return;
            }

            name = name.trim();
            department = department.trim();
            jobTitle = jobTitle.trim();

            if (name.isEmpty()
                    || department.isEmpty()
                    || jobTitle.isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "All employee fields are required."
                );
                return;
            }

            // Validate employee name.
            if (name.length() < 2
                    || name.length() > 100
                    || !name.matches("[\\p{L}]+([ '\\-][\\p{L}]+)*")) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Enter a valid employee name (2-100 characters)."
                );
                return;
            }

            // Validate department.
            // Allows letters, spaces and common department-name symbols.
            // Does not allow numbers.
            if (department.length() < 2
                    || department.length() > 100
                    || !department.matches(
                    "(?=.*\\p{L})[\\p{L} &()'/+.\\-]{2,100}"
            )) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Enter a valid department using letters only."
                );
                return;
            }

            // Validate job title.
            // Supports titles such as C++ Developer.
            if (jobTitle.length() < 2
                    || jobTitle.length() > 100
                    || !jobTitle.matches(
                    "(?=.*[\\p{L}\\p{N}])[\\p{L}\\p{N} &+()./#'\\-]{2,100}"
            )) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Enter a valid job title (2-100 characters)."
                );
                return;
            }

            // Update existing employee.
            if ("update".equals(action)) {

                String employeeIdValue =
                        request.getParameter("employeeId");

                if (employeeIdValue == null) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Employee ID is required."
                    );
                    return;
                }

                int employeeId = Integer.parseInt(employeeIdValue);

                if (employeeId <= 0) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid employee ID."
                    );
                    return;
                }

                if (employeeDAO.getEmployeeById(employeeId) == null) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Employee not found."
                    );
                    return;
                }

                boolean updated = employeeDAO.updateEmployee(
                        employeeId,
                        name,
                        department,
                        jobTitle
                );

                if (!updated) {
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Employee could not be updated."
                    );
                    return;
                }

                response.sendRedirect(
                        request.getContextPath()
                                + "/employees?success=updated"
                );

            } else {

                // Add new employee.
                boolean added = employeeDAO.addEmployee(
                        name,
                        department,
                        jobTitle
                );

                if (!added) {
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Employee could not be added."
                    );
                    return;
                }

                response.sendRedirect(
                        request.getContextPath()
                                + "/employees?success=added"
                );
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid employee ID."
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to save employee data.",
                    e
            );
        }
    }
}
