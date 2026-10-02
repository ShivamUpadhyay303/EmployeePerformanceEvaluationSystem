<%@ page import="com.epe.employeeperformanceevaluationsystem.model.Employee" %>

<%
    Employee employee = (Employee) request.getAttribute("employee");

    if (employee == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found.");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Employee</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f7fb;
            margin: 0;
            padding: 30px;
        }

        .edit-container {
            max-width: 650px;
            margin: 40px auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        }

        h2 {
            color: #243b53;
            margin-top: 0;
        }

        .form-group {
            margin-bottom: 18px;
        }

        label {
            display: block;
            margin-bottom: 7px;
            font-weight: bold;
            color: #334e68;
        }

        input {
            width: 100%;
            box-sizing: border-box;
            padding: 12px;
            border: 1px solid #cbd5e1;
            border-radius: 6px;
            font-size: 15px;
        }

        input:focus {
            outline: none;
            border-color: #2563eb;
        }

        .button {
            display: inline-block;
            padding: 12px 20px;
            border: none;
            border-radius: 6px;
            background: #2563eb;
            color: white;
            font-size: 15px;
            cursor: pointer;
            text-decoration: none;
        }

        .button:hover {
            background: #1d4ed8;
        }

        .cancel {
            background: #64748b;
            margin-left: 8px;
        }

        .cancel:hover {
            background: #475569;
        }

        @media (max-width: 600px) {
            body {
                padding: 15px;
            }

            .edit-container {
                margin: 20px auto;
                padding: 20px;
            }
        }
    </style>
</head>

<body>
<div class="edit-container">
    <h2>Edit Employee</h2>

    <form action="<%= request.getContextPath() %>/employees" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="employeeId" value="<%= employee.getEmployeeId() %>">

        <div class="form-group">
            <label for="name">Employee Name</label>
            <input type="text" id="name" name="name"
                   value="<%= employee.getName() %>" required>
        </div>

        <div class="form-group">
            <label for="department">Department</label>
            <input type="text" id="department" name="department"
                   value="<%= employee.getDepartment() %>" required>
        </div>

        <div class="form-group">
            <label for="jobTitle">Job Title</label>
            <input type="text" id="jobTitle" name="jobTitle"
                   value="<%= employee.getJobTitle() %>" required>
        </div>

        <button type="submit" class="button">Save Changes</button>
        <a href="<%= request.getContextPath() %>/employees" class="button cancel">
            Cancel
        </a>
    </form>
</div>
</body>
</html>