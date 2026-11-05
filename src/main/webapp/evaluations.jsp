
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.model.Employee" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.util.HtmlUtil" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.util.CsrfUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String role = (String) session.getAttribute("role");

    if (!"MANAGER".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }

    List<Employee> employees =
            (List<Employee>) request.getAttribute("employees");

    if (employees == null) {
        employees = new ArrayList<>();
    }

    String success = request.getParameter("success");

    // Generate or retrieve the CSRF security token.
    String csrfToken = CsrfUtil.getOrCreateToken(session);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Performance Evaluations - EPES</title>

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
            max-width: 1000px;
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

        .form-group {
            margin-bottom: 22px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 10px;
        }

        input, select, textarea {
            width: 100%;
            padding: 14px;
            border: 1px solid #d5deea;
            border-radius: 7px;
            font-size: 15px;
            background: white;
        }

        textarea {
            min-height: 120px;
            resize: vertical;
        }

        input:focus, select:focus, textarea:focus {
            outline: none;
            border-color: #2363c5;
        }

        button {
            background: #2363c5;
            color: white;
            padding: 14px 25px;
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

        .success {
            background: #e5f6ec;
            color: #197344;
            padding: 14px;
            border-radius: 7px;
            margin-bottom: 22px;
        }

        .empty {
            color: #8290a4;
            padding: 15px 0;
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
                padding: 22px;
            }
        }
    </style>
</head>

<body>

<div class="topbar">
    <h2>EPES - Performance Evaluations</h2>

    <a class="back-btn"
       href="<%= request.getContextPath() %>/manager-dashboard.jsp">
        Back to Dashboard
    </a>
</div>

<div class="container">

    <div class="heading">
        <h1>Performance Evaluations</h1>
        <p>Evaluate employee performance based on skills and workplace criteria.</p>
    </div>

    <div class="card">

        <h3>Create Employee Evaluation</h3>

        <% if ("saved".equals(success)) { %>
            <div class="success" role="status">
                Performance evaluation submitted successfully.
            </div>
        <% } %>

        <% if (employees.isEmpty()) { %>
            <p class="empty">
                No employees available. Add an employee first.
            </p>
        <% } %>

        <form action="<%= request.getContextPath() %>/evaluations"
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
                <label for="criteria">Evaluation Criteria</label>

                <select name="criteria" id="criteria" required>
                    <option value="">Select evaluation criteria</option>
                    <option value="Communication">Communication</option>
                    <option value="Teamwork">Teamwork</option>
                    <option value="Technical Skills">Technical Skills</option>
                    <option value="Leadership">Leadership</option>
                    <option value="Problem Solving">Problem Solving</option>
                    <option value="Overall Performance">Overall Performance</option>
                </select>
            </div>

            <div class="form-group">
                <label for="rating">Performance Rating</label>

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
                <label for="comments">Evaluation Comments</label>

                <textarea name="comments"
                          id="comments"
                          minlength="2"
                          maxlength="2000"
                          placeholder="Enter performance observations and comments..."
                          required></textarea>
            </div>

            <button type="submit"
                    <%= employees.isEmpty() ? "disabled" : "" %>>
                Submit Evaluation
            </button>

        </form>

    </div>

</div>

<footer>
    Employee Performance Evaluation System (EPES)
</footer>

</body>
</html>
