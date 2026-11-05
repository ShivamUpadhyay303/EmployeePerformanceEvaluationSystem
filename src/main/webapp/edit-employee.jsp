
<%@ page import="com.epe.employeeperformanceevaluationsystem.model.Employee" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.util.HtmlUtil" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.util.CsrfUtil" %>
<%@ page import="jakarta.servlet.http.HttpServletResponse" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String role = (String) session.getAttribute("role");

    if (!"MANAGER".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }

    Employee employee = (Employee) request.getAttribute("employee");

    if (employee == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found.");
        return;
    }

    // Generate or retrieve the CSRF security token.
    String csrfToken = CsrfUtil.getOrCreateToken(session);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Employee - EPES</title>

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
            max-width: 850px;
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
            padding: 35px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.05);
        }

        .card h3 {
            font-size: 21px;
            margin-bottom: 25px;
        }

        .employee-id {
            background: #f3f6fb;
            padding: 14px;
            border-radius: 7px;
            margin-bottom: 25px;
            color: #64748b;
        }

        .form-group {
            margin-bottom: 22px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 9px;
            font-size: 14px;
        }

        input {
            width: 100%;
            padding: 14px;
            border: 1px solid #d5deea;
            border-radius: 7px;
            font-size: 15px;
        }

        input:focus {
            outline: none;
            border-color: #2363c5;
        }

        .buttons {
            display: flex;
            gap: 12px;
            margin-top: 28px;
        }

        .button {
            display: inline-block;
            padding: 13px 24px;
            border: none;
            border-radius: 7px;
            font-size: 15px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
        }

        .save-button {
            background: #2363c5;
            color: white;
        }

        .save-button:hover {
            background: #194e9f;
        }

        .cancel-button {
            background: #e8edf4;
            color: #34445a;
        }

        .cancel-button:hover {
            background: #d9e1ec;
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
                padding: 22px;
            }

            .buttons {
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

<div class="topbar">
    <h2>EPES - Employee Management</h2>

    <a class="back-btn"
       href="<%= request.getContextPath() %>/employees">
        Back to Employee Directory
    </a>
</div>

<div class="container">

    <div class="heading">
        <h1>Edit Employee</h1>
        <p>Update employee information and save the changes.</p>
    </div>

    <div class="card">

        <h3>Employee Details</h3>

        <div class="employee-id">
            Employee ID:
            <strong><%= employee.getEmployeeId() %></strong>
        </div>

        <form action="<%= request.getContextPath() %>/employees"
              method="post">

            <!-- CSRF security token -->
            <input type="hidden"
                   name="csrfToken"
                   value="<%= HtmlUtil.escape(csrfToken) %>">

            <input type="hidden" name="action" value="update">

            <input type="hidden"
                   name="employeeId"
                   value="<%= employee.getEmployeeId() %>">

            <div class="form-group">
                <label for="name">Employee Name</label>

                <input type="text"
                       id="name"
                       name="name"
                       value="<%= HtmlUtil.escape(employee.getName()) %>"
                       minlength="2"
                       maxlength="100"
                       autocomplete="off"
                       required>
            </div>

            <div class="form-group">
                <label for="department">Department</label>

                <input type="text"
                       id="department"
                       name="department"
                       value="<%= HtmlUtil.escape(employee.getDepartment()) %>"
                       minlength="2"
                       maxlength="100"
                       required>
            </div>

            <div class="form-group">
                <label for="jobTitle">Job Title</label>

                <input type="text"
                       id="jobTitle"
                       name="jobTitle"
                       value="<%= HtmlUtil.escape(employee.getJobTitle()) %>"
                       minlength="2"
                       maxlength="100"
                       required>
            </div>

            <div class="buttons">

                <button type="submit" class="button save-button">
                    Save Changes
                </button>

                <a href="<%= request.getContextPath() %>/employees"
                   class="button cancel-button">
                    Cancel
                </a>

            </div>

        </form>

    </div>

</div>

<footer>
    Employee Performance Evaluation System (EPES)
</footer>

</body>
</html>
