
<%@ page import="java.util.List" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.model.Employee" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.util.HtmlUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String role = (String) session.getAttribute("role");

    if (!"MANAGER".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }

    List<Employee> employees =
            (List<Employee>) request.getAttribute("employees");

    String success = request.getParameter("success");
    String message = null;

    String csrfToken =
            com.epe.employeeperformanceevaluationsystem.util.CsrfUtil
                    .getOrCreateToken(session);

    if ("added".equals(success)) {
        message = "Employee added successfully.";
    } else if ("updated".equals(success)) {
        message = "Employee updated successfully.";
    } else if ("deleted".equals(success)) {
        message = "Employee deleted successfully.";
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Management - EPES</title>

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
            max-width: 1250px;
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

        .card-heading {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            margin-bottom: 25px;
        }

        .card-heading h3 {
            font-size: 21px;
        }

        .employee-count {
            background: #eaf2ff;
            color: #2363c5;
            padding: 9px 15px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: bold;
        }

        .success-message {
            background: #e5f6ec;
            color: #197344;
            padding: 14px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 9px;
            font-size: 14px;
        }

        input {
            width: 100%;
            padding: 13px;
            border: 1px solid #d5deea;
            border-radius: 7px;
            font-size: 15px;
        }

        input:focus {
            outline: none;
            border-color: #2363c5;
        }

        .add-button {
            background: #2363c5;
            color: white;
            padding: 13px 24px;
            border: none;
            border-radius: 7px;
            font-size: 15px;
            font-weight: bold;
            cursor: pointer;
        }

        .add-button:hover {
            background: #194e9f;
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
            font-size: 14px;
        }

        td {
            padding: 18px 17px;
            border-bottom: 1px solid #e1e6ed;
            font-size: 14px;
        }

        tbody tr:hover {
            background: #f8faff;
        }

        .employee-id {
            color: #64748b;
            font-weight: bold;
        }

        .employee-name {
            color: #1b3558;
            font-weight: bold;
        }

        .department {
            background: #eaf2ff;
            color: #2363c5;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }

        .action-buttons {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .action-button {
            padding: 8px 13px;
            border: none;
            border-radius: 6px;
            font-size: 13px;
            font-weight: bold;
            text-decoration: none;
            cursor: pointer;
        }

        .edit-button {
            background: #eaf2ff;
            color: #2363c5;
        }

        .edit-button:hover {
            background: #d6e6ff;
        }

        .delete-button {
            background: #fee2e2;
            color: #b91c1c;
        }

        .delete-button:hover {
            background: #fecaca;
        }

        .empty-message {
            text-align: center;
            padding: 30px;
            color: #8290a4;
        }

        footer {
            text-align: center;
            color: #8290a4;
            padding: 30px 15px;
        }

        @media (max-width: 800px) {
            .form-grid {
                grid-template-columns: 1fr;
                gap: 0;
            }
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

            .card-heading {
                align-items: flex-start;
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

<div class="topbar">
    <h2>EPES - Employee Management</h2>

    <a class="back-btn"
       href="<%= request.getContextPath() %>/manager-dashboard.jsp">
        Back to Dashboard
    </a>
</div>

<div class="container">

    <div class="heading">
        <h1>Employee Management</h1>
        <p>Add, view, update and manage employee information.</p>
    </div>

    <% if (message != null) { %>
        <div class="success-message" role="status">
            <%= message %>
        </div>
    <% } %>

    <div class="card">

        <div class="card-heading">
            <h3>Employee Directory</h3>

            <span class="employee-count">
                <%= employees == null ? 0 : employees.size() %> Employees
            </span>
        </div>

        <div class="card"
             style="background:#f8faff; box-shadow:none; border:1px solid #e5eaf2;">

            <h3 style="margin-bottom:22px;">Add New Employee</h3>

            <form action="<%= request.getContextPath() %>/employees"
                  method="post">

                <!-- CSRF SECURITY TOKEN -->
                <input type="hidden"
                       name="csrfToken"
                       value="<%= csrfToken %>">

                <input type="hidden" name="action" value="add">

                <div class="form-grid">

                    <div class="form-group">
                        <label for="name">Employee Name</label>

                        <input type="text"
                               id="name"
                               name="name"
                               placeholder="Enter employee name"
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
                               placeholder="Enter department"
                               minlength="2"
                               maxlength="100"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="jobTitle">Job Title</label>

                        <input type="text"
                               id="jobTitle"
                               name="jobTitle"
                               placeholder="Enter job title"
                               minlength="2"
                               maxlength="100"
                               required>
                    </div>

                </div>

                <button type="submit" class="add-button">
                    Add Employee
                </button>

            </form>

        </div>

        <div class="table-wrapper">

            <table>
                <thead>
                <tr>
                    <th>Employee ID</th>
                    <th>Employee Name</th>
                    <th>Department</th>
                    <th>Job Title</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody>

                <% if (employees != null && !employees.isEmpty()) {
                    for (Employee employee : employees) { %>

                    <tr>
                        <td class="employee-id">
                            <%= employee.getEmployeeId() %>
                        </td>

                        <td class="employee-name">
                            <%= HtmlUtil.escape(employee.getName()) %>
                        </td>

                        <td>
                            <span class="department">
                                <%= HtmlUtil.escape(employee.getDepartment()) %>
                            </span>
                        </td>

                        <td>
                            <%= HtmlUtil.escape(employee.getJobTitle()) %>
                        </td>

                        <td>
                            <div class="action-buttons">

                                <a class="action-button edit-button"
                                   href="<%= request.getContextPath() %>/employees?id=<%= employee.getEmployeeId() %>">
                                    Edit
                                </a>

                                <form action="<%= request.getContextPath() %>/employees"
                                      method="post"
                                      onsubmit="return confirm('Are you sure you want to delete this employee?');">

                                    <!-- CSRF SECURITY TOKEN -->
                                    <input type="hidden"
                                           name="csrfToken"
                                           value="<%= csrfToken %>">

                                    <input type="hidden"
                                           name="action"
                                           value="delete">

                                    <input type="hidden"
                                           name="employeeId"
                                           value="<%= employee.getEmployeeId() %>">

                                    <button type="submit"
                                            class="action-button delete-button">
                                        Delete
                                    </button>

                                </form>

                            </div>
                        </td>
                    </tr>

                <%  }
                } else { %>

                    <tr>
                        <td colspan="5" class="empty-message">
                            No employees found.
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
