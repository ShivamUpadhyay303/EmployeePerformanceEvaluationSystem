
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.model.PerformanceReport" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.util.HtmlUtil" %>
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

    List<PerformanceReport> reports =
            (List<PerformanceReport>) request.getAttribute("reports");

    PerformanceReport employeeReport =
            (PerformanceReport) request.getAttribute("report");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Performance Reports - EPES</title>

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

        .report-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.05);
            margin-bottom: 25px;
        }

        .report-card h3 {
            font-size: 21px;
            margin-bottom: 25px;
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

        .rating {
            font-weight: bold;
            color: #2363c5;
        }

        .empty {
            text-align: center;
            color: #8290a4;
            padding: 25px;
        }

        .summary {
            display: grid;
            grid-template-columns: repeat(3, minmax(200px, 1fr));
            gap: 22px;
        }

        .summary-box {
            background: #f3f6fb;
            padding: 25px;
            border-radius: 12px;
            overflow-wrap: anywhere;
        }

        .summary-box p {
            color: #77869b;
            margin-bottom: 12px;
        }

        .summary-box h2 {
            font-size: 30px;
            color: #2363c5;
        }

        footer {
            text-align: center;
            color: #8290a4;
            padding: 30px 15px;
        }

        @media (max-width: 750px) {
            .summary {
                grid-template-columns: 1fr;
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

            .report-card {
                padding: 20px;
            }
        }
    </style>
</head>

<body>

<div class="topbar">
    <h2>EPES - Performance Reports</h2>

    <a class="back-btn"
       href="<%= request.getContextPath() %>/<%= dashboard %>">
        Back to Dashboard
    </a>
</div>

<div class="container">

    <div class="heading">
        <h1>Performance Reports</h1>
        <p>View employee performance evaluation results and rating summaries.</p>
    </div>

    <% if (isManager) { %>

        <div class="report-card">

            <h3>All Employee Performance Reports</h3>

            <div class="table-wrapper">

                <table>

                    <thead>
                    <tr>
                        <th>Employee Name</th>
                        <th>Total Evaluations</th>
                        <th>Average Rating</th>
                    </tr>
                    </thead>

                    <tbody>

                    <% if (reports != null && !reports.isEmpty()) {

                        for (PerformanceReport reportItem : reports) { %>

                        <tr>

                            <td>
                                <%= HtmlUtil.escape(reportItem.getEmployeeName()) %>
                            </td>

                            <td>
                                <%= reportItem.getTotalEvaluations() %>
                            </td>

                            <td class="rating">
                                <%= String.format(
                                        Locale.ROOT,
                                        "%.2f",
                                        reportItem.getAverageRating()
                                ) %> / 5
                            </td>

                        </tr>

                    <%  }

                    } else { %>

                        <tr>
                            <td colspan="3" class="empty">
                                No performance reports available.
                            </td>
                        </tr>

                    <% } %>

                    </tbody>

                </table>

            </div>

        </div>

    <% } else { %>

        <div class="report-card">

            <h3>My Performance Summary</h3>

            <% if (employeeReport != null) { %>

                <div class="summary">

                    <div class="summary-box">

                        <p>Employee Name</p>

                        <h2>
                            <%= HtmlUtil.escape(employeeReport.getEmployeeName()) %>
                        </h2>

                    </div>

                    <div class="summary-box">

                        <p>Total Evaluations</p>

                        <h2>
                            <%= employeeReport.getTotalEvaluations() %>
                        </h2>

                    </div>

                    <div class="summary-box">

                        <p>Average Rating</p>

                        <h2>
                            <%= String.format(
                                    Locale.ROOT,
                                    "%.2f",
                                    employeeReport.getAverageRating()
                            ) %> / 5
                        </h2>

                    </div>

                </div>

            <% } else { %>

                <p class="empty">
                    Your performance report is not available yet.
                </p>

            <% } %>

        </div>

    <% } %>

</div>

<footer>
    Employee Performance Evaluation System (EPES)
</footer>

</body>
</html>
