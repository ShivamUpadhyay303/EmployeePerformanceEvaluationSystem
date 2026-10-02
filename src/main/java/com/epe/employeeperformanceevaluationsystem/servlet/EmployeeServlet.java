package com.epe.employeeperformanceevaluationsystem.servlet;

import com.epe.employeeperformanceevaluationsystem.dao.EmployeeDAO;
import com.epe.employeeperformanceevaluationsystem.model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmployeeDAO employeeDAO = new EmployeeDAO();
        String idParameter = request.getParameter("id");

        try {
            if (idParameter != null) {
                int employeeId = Integer.parseInt(idParameter);
                Employee employee = employeeDAO.getEmployeeById(employeeId);

                if (employee == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Employee not found.");
                    return;
                }

                request.setAttribute("employee", employee);
                request.getRequestDispatcher("/edit-employee.jsp")
                        .forward(request, response);

            } else {
                List<Employee> employees = employeeDAO.getAllEmployees();
                request.setAttribute("employees", employees);
                request.getRequestDispatcher("/employees.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid employee ID.");
        } catch (SQLException e) {
            throw new ServletException("Unable to retrieve employee data.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        EmployeeDAO employeeDAO = new EmployeeDAO();

        try {
            // Delete an employee
            if ("delete".equals(action)) {
                int employeeId = Integer.parseInt(request.getParameter("employeeId"));

                employeeDAO.deleteEmployee(employeeId);

                response.sendRedirect(
                        request.getContextPath() + "/employees?success=deleted"
                );
                return;
            }

            // Read employee details for adding or updating
            String name = request.getParameter("name");
            String department = request.getParameter("department");
            String jobTitle = request.getParameter("jobTitle");

            // Validate required fields
            if (name == null || department == null || jobTitle == null
                    || name.trim().isEmpty()
                    || department.trim().isEmpty()
                    || jobTitle.trim().isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "All employee fields are required."
                );
                return;
            }

            // Update an existing employee
            if ("update".equals(action)) {
                int employeeId = Integer.parseInt(request.getParameter("employeeId"));

                employeeDAO.updateEmployee(
                        employeeId,
                        name.trim(),
                        department.trim(),
                        jobTitle.trim()
                );

                response.sendRedirect(
                        request.getContextPath() + "/employees?success=updated"
                );
                return;

            } else {
                // Add a new employee
                employeeDAO.addEmployee(
                        name.trim(),
                        department.trim(),
                        jobTitle.trim()
                );

                response.sendRedirect(
                        request.getContextPath() + "/employees?success=added"
                );
                return;
            }

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid employee ID."
            );

        } catch (SQLException e) {
            throw new ServletException("Unable to save employee data.", e);
        }
    }
}