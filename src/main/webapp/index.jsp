
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String error = request.getParameter("error");
    String contextPath = request.getContextPath();

    String csrfToken =
        com.epe.employeeperformanceevaluationsystem.util.CsrfUtil
            .getOrCreateToken(session);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>EPES | Sign In</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: "Segoe UI", Arial, sans-serif;
        }

        body {
            min-height: 100vh;
            background: #f4f6fa;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 25px;
        }

        .login-wrapper {
            width: 100%;
            max-width: 1050px;
            min-height: 620px;
            background: white;
            display: grid;
            grid-template-columns: 1fr 1fr;
            border-radius: 18px;
            overflow: hidden;
            box-shadow: 0 15px 50px rgba(25, 45, 75, 0.12);
        }

        .brand-section {
            background: linear-gradient(145deg, #172b4d, #234b7c);
            color: white;
            padding: 55px 45px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .brand-logo {
            font-size: 31px;
            font-weight: 800;
            letter-spacing: 1px;
        }

        .brand-logo span {
            color: #58a6ff;
        }

        .brand-tagline {
            color: #b8c9df;
            font-size: 12px;
            margin-top: 7px;
            letter-spacing: 1.5px;
        }

        .brand-content {
            margin-top: 50px;
            margin-bottom: 50px;
        }

        .brand-content h1 {
            font-size: 35px;
            line-height: 1.3;
            margin-bottom: 20px;
        }

        .brand-content h1 span {
            color: #72b2ff;
        }

        .brand-content p {
            color: #c3d0e2;
            font-size: 15px;
            line-height: 1.8;
            max-width: 390px;
        }

        .feature-list {
            list-style: none;
            margin-top: 30px;
        }

        .feature-list li {
            color: #e0e9f5;
            font-size: 14px;
            margin-bottom: 17px;
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .feature-icon {
            width: 27px;
            height: 27px;
            background: rgba(255, 255, 255, 0.12);
            border-radius: 7px;
            display: flex;
            justify-content: center;
            align-items: center;
            color: #83c1ff;
        }

        .brand-footer {
            color: #9eb1ca;
            font-size: 12px;
        }

        .login-section {
            padding: 55px 55px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .login-heading {
            margin-bottom: 30px;
        }

        .login-heading h2 {
            font-size: 29px;
            color: #1d3558;
            margin-bottom: 10px;
        }

        .login-heading p {
            color: #8290a3;
            font-size: 14px;
        }

        .error-message {
            background: #fff0f0;
            border: 1px solid #f2b8b8;
            color: #c62828;
            padding: 13px 15px;
            border-radius: 8px;
            font-size: 13px;
            margin-bottom: 20px;
            line-height: 1.5;
        }

        .role-label {
            display: block;
            color: #33445d;
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 11px;
        }

        .role-options {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 12px;
            margin-bottom: 25px;
        }

        .role-options input {
            display: none;
        }

        .role-options label {
            padding: 14px 8px;
            text-align: center;
            border: 1px solid #dce3ed;
            border-radius: 9px;
            color: #52627a;
            font-size: 13px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.2s;
        }

        .role-options label:hover {
            border-color: #376bb3;
        }

        .role-options input:checked + label {
            background: #eaf1ff;
            border: 1.5px solid #3269b7;
            color: #24569a;
        }

        .form-group {
            margin-bottom: 21px;
        }

        .form-group label {
            display: block;
            color: #33445d;
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 9px;
        }

        .input-box {
            position: relative;
        }

        .form-group input {
            width: 100%;
            padding: 14px 15px;
            border: 1px solid #dce3ed;
            border-radius: 9px;
            outline: none;
            font-size: 14px;
            color: #26354a;
            transition: 0.2s;
        }

        .form-group input:focus {
            border-color: #3269b7;
            box-shadow: 0 0 0 3px rgba(50, 105, 183, 0.10);
        }

        .form-group input::placeholder {
            color: #a4adba;
        }

        .password-input {
            padding-right: 75px !important;
        }

        .toggle-password {
            position: absolute;
            right: 13px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: #3269b7;
            font-size: 12px;
            font-weight: 600;
            cursor: pointer;
        }

        .forgot {
            display: block;
            text-align: right;
            color: #8290a3;
            font-size: 12px;
            margin-top: -5px;
            margin-bottom: 25px;
        }

        .login-button {
            width: 100%;
            padding: 15px;
            border: none;
            border-radius: 9px;
            background: #173b70;
            color: white;
            font-size: 15px;
            font-weight: 700;
            cursor: pointer;
            transition: 0.2s;
        }

        .login-button:hover {
            background: #24549a;
            transform: translateY(-1px);
            box-shadow: 0 5px 15px rgba(23, 59, 112, 0.18);
        }

        .secure-note {
            margin-top: 27px;
            text-align: center;
            color: #98a3b2;
            font-size: 12px;
            line-height: 1.6;
        }

        .login-footer {
            margin-top: 35px;
            padding-top: 20px;
            border-top: 1px solid #edf0f5;
            text-align: center;
            color: #9aa5b5;
            font-size: 11px;
        }

        @media (max-width: 800px) {
            .login-wrapper {
                grid-template-columns: 1fr;
                max-width: 500px;
            }

            .brand-section {
                padding: 30px;
            }

            .brand-content {
                margin: 35px 0;
            }

            .brand-content h1 {
                font-size: 27px;
            }

            .feature-list {
                margin-top: 20px;
            }

            .login-section {
                padding: 40px 30px;
            }
        }

        @media (max-width: 400px) {
            body {
                padding: 12px;
            }

            .brand-section,
            .login-section {
                padding: 25px 20px;
            }

            .login-heading h2 {
                font-size: 25px;
            }
        }
    </style>
</head>

<body>

<div class="login-wrapper">

    <section class="brand-section">

        <div>
            <div class="brand-logo">
                EPES<span>.</span>
            </div>

            <div class="brand-tagline">
                EMPLOYEE PERFORMANCE SYSTEM
            </div>
        </div>

        <div class="brand-content">

            <h1>
                Empowering People.<br>
                <span>Improving Performance.</span>
            </h1>

            <p>
                A smarter way to manage employee performance,
                track professional goals and encourage
                continuous workplace development.
            </p>

            <ul class="feature-list">

                <li>
                    <span class="feature-icon">✓</span>
                    Employee Performance Management
                </li>

                <li>
                    <span class="feature-icon">✓</span>
                    Goal Tracking and Evaluation
                </li>

                <li>
                    <span class="feature-icon">✓</span>
                    Feedback and Performance Reports
                </li>

            </ul>

        </div>

        <div class="brand-footer">
            © 2026 EPES. All rights reserved.
        </div>

    </section>

    <section class="login-section">

        <div class="login-heading">

            <h2>Welcome Back!</h2>

            <p>
                Please enter your details to sign in.
            </p>

        </div>

        <% if ("invalid".equals(error)) { %>

            <div class="error-message" role="alert">
                Invalid email or password. Please check your
                credentials and account type, then try again.
            </div>

        <% } %>

        <form action="<%= contextPath %>/login" method="post">

            <!-- CSRF SECURITY TOKEN -->
            <input type="hidden"
                   name="csrfToken"
                   value="<%= csrfToken %>">

            <span class="role-label">Select your account type</span>

            <div class="role-options">

                <input type="radio"
                       id="employee"
                       name="role"
                       value="EMPLOYEE"
                       checked
                       required>

                <label for="employee">Employee</label>

                <input type="radio"
                       id="manager"
                       name="role"
                       value="MANAGER">

                <label for="manager">Manager</label>

            </div>

            <div class="form-group">

                <label for="email">Email Address</label>

                <input type="email"
                       id="email"
                       name="email"
                       placeholder="Enter your email address"
                       autocomplete="username"
                       maxlength="150"
                       required>

            </div>

            <div class="form-group">

                <label for="password">Password</label>

                <div class="input-box">

                    <input class="password-input"
                           type="password"
                           id="password"
                           name="password"
                           placeholder="Enter your password"
                           autocomplete="current-password"
                           required>

                    <button type="button"
                            class="toggle-password"
                            id="togglePassword"
                            onclick="togglePasswordVisibility()">
                        Show
                    </button>

                </div>

            </div>

            <div class="forgot">
                Forgot Password? Contact your administrator.
            </div>

            <button type="submit" class="login-button">
                Sign In
            </button>

        </form>

        <div class="secure-note">
            Secure access for employees and managers.
            <br>
            Your account information is protected.
        </div>

        <div class="login-footer">
            Employee Performance Evaluation System
        </div>

    </section>

</div>

<script>
    function togglePasswordVisibility() {

        const passwordInput = document.getElementById("password");
        const toggleButton = document.getElementById("togglePassword");

        if (passwordInput.type === "password") {
            passwordInput.type = "text";
            toggleButton.textContent = "Hide";
        } else {
            passwordInput.type = "password";
            toggleButton.textContent = "Show";
        }
    }
</script>

</body>
</html>
