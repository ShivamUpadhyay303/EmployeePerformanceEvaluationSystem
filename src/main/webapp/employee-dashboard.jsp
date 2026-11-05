
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String role = (String) session.getAttribute("role");

    if (!"EMPLOYEE".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }

    String contextPath = request.getContextPath();

    // Get the logged-in employee's name from the session
    String employeeName = (String) session.getAttribute("employeeName");

    if (employeeName == null || employeeName.trim().isEmpty()) {
        employeeName = "Employee";
    }

    String csrfToken =
        com.epe.employeeperformanceevaluationsystem.util.CsrfUtil
            .getOrCreateToken(session);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Employee Dashboard - EPES</title>

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

        .sidebar {
            width: 250px;
            height: 100vh;
            background: #192f50;
            color: white;
            position: fixed;
            padding: 28px 18px;
        }

        .logo {
            font-size: 27px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .subtitle {
            color: #b9c8dc;
            font-size: 13px;
            margin-bottom: 45px;
        }

        .menu-title {
            color: #91acd0;
            font-size: 12px;
            font-weight: bold;
            margin-bottom: 15px;
        }

        .sidebar a {
            display: block;
            color: white;
            text-decoration: none;
            padding: 17px 14px;
            margin-bottom: 9px;
            border-radius: 8px;
            font-size: 15px;
        }

        .sidebar a:hover,
        .sidebar a.active {
            background: #2b496f;
        }

        .main {
            margin-left: 250px;
        }

        .topbar {
            height: 82px;
            background: white;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 40px;
        }

        .topbar h2 {
            font-size: 22px;
        }

        .account {
            text-align: right;
        }

        .account strong {
            display: block;
        }

        .account span {
            color: #8292a8;
            font-size: 13px;
        }

        .content {
            padding: 42px;
        }

        .welcome {
            margin-bottom: 38px;
        }

        .welcome h1 {
            font-size: 32px;
            margin-bottom: 12px;
        }

        .welcome p {
            color: #77869b;
            font-size: 16px;
        }

        .section-title {
            font-size: 21px;
            margin-bottom: 22px;
        }

        .cards {
            display: grid;
            grid-template-columns: repeat(3, minmax(220px, 1fr));
            gap: 25px;
        }

        .card {
            background: white;
            padding: 28px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.05);
            min-height: 210px;
        }

        .card h3 {
            font-size: 19px;
            margin-bottom: 15px;
        }

        .card p {
            color: #8290a4;
            line-height: 1.6;
            font-size: 14px;
            margin-bottom: 22px;
        }

        .card a {
            color: #2363c5;
            text-decoration: none;
            font-weight: bold;
            font-size: 14px;
        }

        .card a:hover {
            text-decoration: underline;
        }

        .logout {
            color: #ffcccc !important;
            margin-top: 25px;
        }

        .logout-button {
            display: block;
            background: none;
            border: none;
            padding: 17px 14px;
            width: 100%;
            text-align: left;
            font-size: 15px;
            font-weight: normal;
            cursor: pointer;
            font-family: Arial, sans-serif;
        }

        .logout-button:hover {
            background: #2b496f;
        }

        @media (max-width: 900px) {
            .cards {
                grid-template-columns: repeat(2, minmax(200px, 1fr));
            }
        }

        @media (max-width: 650px) {
            .sidebar {
                position: relative;
                width: 100%;
                height: auto;
            }

            .main {
                margin-left: 0;
            }

            .content {
                padding: 25px;
            }

            .cards {
                grid-template-columns: 1fr;
            }

            .topbar {
                padding: 0 20px;
            }
        }
    </style>
</head>

<body>

<div class="sidebar">

    <div class="logo">EPES.</div>
    <div class="subtitle">Employee Performance System</div>

    <div class="menu-title">MAIN MENU</div>

    <a href="<%= contextPath %>/employee-dashboard.jsp" class="active">
        Dashboard
    </a>

    <a href="<%= contextPath %>/reports">
        My Performance
    </a>

    <a href="<%= contextPath %>/goals">
        My Goals
    </a>

    <a href="<%= contextPath %>/feedback">
        My Feedback
    </a>

    <!-- Secure Logout Form -->
    <form action="<%= contextPath %>/logout" method="post">

        <input type="hidden"
               name="csrfToken"
               value="<%= csrfToken %>">

        <button type="submit" class="logout logout-button">
            Logout
        </button>

    </form>

</div>

<div class="main">

    <div class="topbar">

        <h2>Employee Workspace</h2>

        <div class="account">
            <strong><%= employeeName %></strong>
            <span>My Account</span>
        </div>

    </div>

    <div class="content">

        <div class="welcome">

            <h1>Welcome back, <%= employeeName %>!</h1>

            <p>
                Track your performance, manage your goals and review feedback.
            </p>

        </div>

        <h2 class="section-title">My Workspace</h2>

        <div class="cards">

            <div class="card">
                <h3>My Performance</h3>

                <p>
                    View your evaluation results and overall performance rating.
                </p>

                <a href="<%= contextPath %>/reports">
                    View Performance
                </a>
            </div>

            <div class="card">
                <h3>My Goals</h3>

                <p>
                    View assigned goals and update your progress.
                </p>

                <a href="<%= contextPath %>/goals">
                    View My Goals
                </a>
            </div>

            <div class="card">
                <h3>My Feedback</h3>

                <p>
                    Read ratings, comments and feedback provided by your manager.
                </p>

                <a href="<%= contextPath %>/feedback">
                    View My Feedback
                </a>
            </div>

        </div>

    </div>

</div>

</body>
</html>
