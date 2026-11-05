
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.model.Goal" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.model.Employee" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.util.HtmlUtil" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.util.CsrfUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String role = (String) session.getAttribute("role");

    if (!"MANAGER".equals(role) && !"EMPLOYEE".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }

    String dashboard = "MANAGER".equals(role)
            ? "manager-dashboard.jsp"
            : "employee-dashboard.jsp";

    List<Goal> goals = (List<Goal>) request.getAttribute("goals");
    List<Employee> employees = (List<Employee>) request.getAttribute("employees");

    if (goals == null) {
        goals = new ArrayList<>();
    }

    if (employees == null) {
        employees = new ArrayList<>();
    }

    boolean isManager = "MANAGER".equals(role);
    int columnCount = isManager ? 5 : 4;

    // Generate or retrieve the CSRF security token.
    String csrfToken = CsrfUtil.getOrCreateToken(session);
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Goal Management - EPES</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            background: #f3f6fb;
            color: #1b3558;
        }

        .topbar {
            background: #192f50;
            color: white;
            padding: 23px 35px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .topbar h2 {
            font-size: 25px;
        }

        .back-btn {
            background: white;
            color: #192f50;
            padding: 12px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: bold;
        }

        .back-btn:hover {
            background: #e5edf8;
        }

        .container {
            max-width: 1200px;
            margin: 45px auto;
            padding: 0 25px;
        }

        .heading {
            margin-bottom: 30px;
        }

        .heading h1 {
            font-size: 32px;
            margin-bottom: 12px;
        }

        .heading p {
            color: #77869b;
        }

        .card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.05);
            margin-bottom: 25px;
        }

        .card h3 {
            font-size: 21px;
            margin-bottom: 25px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 9px;
        }

        input, select, textarea {
            width: 100%;
            padding: 13px;
            border: 1px solid #d5deea;
            border-radius: 7px;
            font-size: 15px;
            background: white;
        }

        textarea {
            min-height: 100px;
            resize: vertical;
        }

        input:focus, select:focus, textarea:focus {
            outline: none;
            border-color: #2363c5;
        }

        button {
            background: #2363c5;
            color: white;
            padding: 12px 22px;
            border: none;
            border-radius: 7px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover:not(:disabled) {
            background: #194e9f;
        }

        button:disabled {
            background: #94a3b8;
            cursor: not-allowed;
        }

        .table-wrapper {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 650px;
        }

        th {
            background: #192f50;
            color: white;
            padding: 17px;
            text-align: left;
        }

        td {
            padding: 17px;
            border-bottom: 1px solid #e1e6ed;
        }

        tr:hover td {
            background: #f8faff;
        }

        .empty {
            text-align: center;
            color: #8290a4;
            padding: 25px;
        }

        .status {
            color: #2363c5;
            font-weight: bold;
        }

        .update-form {
            min-width: 150px;
        }

        .update-form select {
            margin-bottom: 10px;
        }

        .empty-notice {
            color: #8290a4;
            margin-bottom: 18px;
        }

        footer {
            text-align: center;
            color: #8290a4;
            padding: 30px 15px;
        }

        @media (max-width: 650px) {
            .topbar {
                padding: 20px;
                gap: 15px;
                flex-wrap: wrap;
            }

            .topbar h2 {
                font-size: 21px;
            }

            .card {
                padding: 20px;
            }
        }
    </style>
</head>

<body>

<div class="topbar">
    <h2>EPES - Goal Management</h2>

    <a class="back-btn"
       href="<%= request.getContextPath() %>/<%= dashboard %>">
        Back to Dashboard
    </a>
</div>

<div class="container">

    <div class="heading">
        <h1><%= isManager ? "Goal Management" : "My Goals" %></h1>
        <p>Assign goals, track progress and manage employee development.</p>
    </div>

    <% if (isManager) { %>

        <div class="card">

            <h3>Assign New Goal</h3>

            <% if (employees.isEmpty()) { %>
                <p class="empty-notice">
                    No employees available. Add an employee first.
                </p>
            <% } %>

            <form action="<%= request.getContextPath() %>/goals"
                  method="post">

                <!-- CSRF security token -->
                <input type="hidden"
                       name="csrfToken"
                       value="<%= HtmlUtil.escape(csrfToken) %>">

                <div class="form-group">

                    <label for="employeeId">Select Employee</label>

                    <select name="employeeId"
                            id="employeeId"
                            required
                            <%= employees.isEmpty() ? "disabled" : "" %>>

                        <option value="">Select an employee</option>

                        <% for (Employee employee : employees) { %>

                            <option value="<%= employee.getEmployeeId() %>">
                                <%= HtmlUtil.escape(employee.getName()) %>
                            </option>

                        <% } %>

                    </select>

                </div>

                <div class="form-group">

                    <label for="description">Goal Description</label>

                    <textarea name="description"
                              id="description"
                              minlength="2"
                              maxlength="2000"
                              placeholder="Enter goal details"
                              required></textarea>

                </div>

                <div class="form-group">

                    <label for="targetDate">Target Date (Optional)</label>

                    <input type="date"
                           name="targetDate"
                           id="targetDate">

                </div>

                <button type="submit"
                        <%= employees.isEmpty() ? "disabled" : "" %>>
                    Assign Goal
                </button>

            </form>

        </div>

    <% } %>

    <div class="card">

        <h3><%= isManager ? "All Employee Goals" : "My Assigned Goals" %></h3>

        <div class="table-wrapper">

            <table>

                <thead>

                <tr>

                    <% if (isManager) { %>
                        <th>Goal ID</th>
                        <th>Employee ID</th>
                    <% } %>

                    <th>Description</th>
                    <th>Target Date</th>
                    <th>Status</th>

                    <% if (!isManager) { %>
                        <th>Update Status</th>
                    <% } %>

                </tr>

                </thead>

                <tbody>

                <% if (!goals.isEmpty()) {

                    for (Goal goal : goals) { %>

                    <tr>

                        <% if (isManager) { %>

                            <td>
                                <%= goal.getGoalId() %>
                            </td>

                            <td>
                                <%= goal.getEmployeeId() %>
                            </td>

                        <% } %>

                        <td>
                            <%= HtmlUtil.escape(goal.getDescription()) %>
                        </td>

                        <td>
                            <%= goal.getTargetDate() == null
                                    ? "Not specified"
                                    : goal.getTargetDate() %>
                        </td>

                        <td class="status">
                            <%= HtmlUtil.escape(goal.getStatus()) %>
                        </td>

                        <% if (!isManager) { %>

                            <td>

                                <form action="<%= request.getContextPath() %>/goals"
                                      method="post"
                                      class="update-form">

                                    <!-- CSRF security token -->
                                    <input type="hidden"
                                           name="csrfToken"
                                           value="<%= HtmlUtil.escape(csrfToken) %>">

                                    <input type="hidden"
                                           name="goalId"
                                           value="<%= goal.getGoalId() %>">

                                    <select name="status" required>

                                        <option value="Pending"
                                            <%= "Pending".equals(goal.getStatus())
                                                    ? "selected" : "" %>>
                                            Pending
                                        </option>

                                        <option value="In Progress"
                                            <%= "In Progress".equals(goal.getStatus())
                                                    ? "selected" : "" %>>
                                            In Progress
                                        </option>

                                        <option value="Completed"
                                            <%= "Completed".equals(goal.getStatus())
                                                    ? "selected" : "" %>>
                                            Completed
                                        </option>

                                    </select>

                                    <button type="submit">
                                        Update
                                    </button>

                                </form>

                            </td>

                        <% } %>

                    </tr>

                <%  }

                } else { %>

                    <tr>
                        <td colspan="<%= columnCount %>" class="empty">
                            No goals available.
                        </td>
                    </tr>

                <% } %>

                </tbody>

            </table>

        </div>

    </div>

</div>

<footer>
    Employee Performance Evaluation System (EPES)
</footer>

</body>
</html>
