
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.model.Feedback" %>
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

    boolean isManager = "MANAGER".equals(role);

    String dashboard = isManager
            ? "manager-dashboard.jsp"
            : "employee-dashboard.jsp";

    List<Feedback> feedbackList =
            (List<Feedback>) request.getAttribute("feedbackList");

    List<Employee> employees =
            (List<Employee>) request.getAttribute("employees");

    if (feedbackList == null) {
        feedbackList = new ArrayList<>();
    }

    if (employees == null) {
        employees = new ArrayList<>();
    }

    int columnCount = isManager ? 4 : 2;

    // Generate or retrieve the CSRF security token.
    String csrfToken = CsrfUtil.getOrCreateToken(session);
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Feedback Management - EPES</title>

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
            font-size: 16px;
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
            min-height: 110px;
            resize: vertical;
        }

        input:focus, select:focus, textarea:focus {
            outline: none;
            border-color: #2363c5;
        }

        button {
            background: #2363c5;
            color: white;
            padding: 13px 25px;
            border: none;
            border-radius: 7px;
            font-size: 15px;
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
            min-width: 550px;
        }

        th {
            background: #192f50;
            color: white;
            padding: 17px;
            text-align: left;
        }

        td {
            padding: 18px;
            border-bottom: 1px solid #e1e6ed;
            color: #34445a;
        }

        tr:hover td {
            background: #f8faff;
        }

        .empty {
            text-align: center;
            color: #8290a4;
            padding: 25px;
        }

        .rating {
            color: #2363c5;
            font-weight: bold;
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

            .container {
                margin-top: 30px;
            }

            .card {
                padding: 20px;
            }
        }
    </style>
</head>

<body>

<div class="topbar">
    <h2>EPES - Feedback Management</h2>

    <a class="back-btn"
       href="<%= request.getContextPath() %>/<%= dashboard %>">
        Back to Dashboard
    </a>
</div>

<div class="container">

    <div class="heading">
        <h1>
            <%= isManager ? "Feedback Management" : "My Feedback" %>
        </h1>

        <p>Review employee feedback, ratings and performance guidance.</p>
    </div>

    <% if (isManager) { %>

        <div class="card">

            <h3>Provide Employee Feedback</h3>

            <% if (employees.isEmpty()) { %>
                <p class="empty-notice">
                    No employees available. Add an employee first.
                </p>
            <% } %>

            <form action="<%= request.getContextPath() %>/feedback"
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

                    <label for="rating">Rating (1 to 5)</label>

                    <select name="rating" id="rating" required>

                        <option value="">Select rating</option>
                        <option value="1">1 - Poor</option>
                        <option value="2">2 - Below Average</option>
                        <option value="3">3 - Average</option>
                        <option value="4">4 - Good</option>
                        <option value="5">5 - Excellent</option>

                    </select>

                </div>

                <div class="form-group">

                    <label for="comments">Comments</label>

                    <textarea name="comments"
                              id="comments"
                              minlength="2"
                              maxlength="2000"
                              placeholder="Enter your feedback..."
                              required></textarea>

                </div>

                <button type="submit"
                        <%= employees.isEmpty() ? "disabled" : "" %>>
                    Submit Feedback
                </button>

            </form>

        </div>

    <% } %>

    <div class="card">

        <h3>
            <%= isManager ? "All Employee Feedback" : "My Feedback History" %>
        </h3>

        <div class="table-wrapper">

            <table>

                <thead>

                <tr>

                    <% if (isManager) { %>
                        <th>Feedback ID</th>
                        <th>Employee Name</th>
                    <% } %>

                    <th>Rating</th>
                    <th>Comments</th>

                </tr>

                </thead>

                <tbody>

                <% if (!feedbackList.isEmpty()) {

                    for (Feedback feedback : feedbackList) { %>

                    <tr>

                        <% if (isManager) { %>

                            <td>
                                <%= feedback.getFeedbackId() %>
                            </td>

                            <td>
                                <%= HtmlUtil.escape(feedback.getEmployeeName()) %>
                            </td>

                        <% } %>

                        <td class="rating">
                            <%= feedback.getRating() %> / 5
                        </td>

                        <td>
                            <%= HtmlUtil.escape(feedback.getComments()) %>
                        </td>

                    </tr>

                <%  }

                } else { %>

                    <tr>
                        <td colspan="<%= columnCount %>" class="empty">
                            No feedback available yet.
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
