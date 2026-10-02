<%@ page import="java.util.List" %>
<%@ page import="com.epe.employeeperformanceevaluationsystem.model.Employee" %>

<%--
    Retrieve the employee list placed in the request by EmployeeServlet.
    The list contains employee records fetched from the database.
--%>
<%
    List<Employee> employees =
            (List<Employee>) request.getAttribute("employees");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <!-- Makes the page responsive on mobile phones and tablets -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Employee Performance Evaluation System</title>

    <style>
        /* Includes padding and borders within an element's total width */
        * {
            box-sizing: border-box;
        }

        /* Overall page appearance */
        body {
            margin: 0;
            padding: 32px 18px;
            font-family: Arial, sans-serif;
            background: #f3f6fb;
            color: #1e293b;
        }

        /* Centers the page content and limits its maximum width */
        .container {
            max-width: 1100px;
            margin: 0 auto;
        }

        /* Main page heading */
        .header {
            margin-bottom: 28px;
        }

        .header h1 {
            margin: 0 0 8px;
            color: #173b70;
            font-size: 28px;
        }

        .header p {
            margin: 0;
            color: #64748b;
            font-size: 15px;
        }

        /* White card containing the employee directory */
        .card {
            background: #ffffff;
            border: 1px solid #e5eaf2;
            border-radius: 14px;
            padding: 24px;
            box-shadow: 0 8px 24px rgba(15, 23, 42, 0.05);
        }

        /* Aligns the directory heading and employee count */
        .card-heading {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            margin-bottom: 18px;
        }

        .card-heading h2 {
            margin: 0;
            color: #1e293b;
            font-size: 20px;
        }

        /* Badge displaying the total number of employees */
        .employee-count {
            padding: 7px 12px;
            border-radius: 20px;
            background: #eaf2ff;
            color: #2459a6;
            font-size: 13px;
            font-weight: bold;
            white-space: nowrap;
        }

        /* Success message displayed after adding, updating, or deleting */
        .success-message {
            margin-bottom: 18px;
            padding: 12px 16px;
            border: 1px solid #a7f3d0;
            border-radius: 8px;
            background: #ecfdf5;
            color: #047857;
            font-size: 14px;
            font-weight: 600;
        }

        /* Allows horizontal scrolling when the table is too wide */
        .table-wrapper {
            width: 100%;
            overflow-x: auto;
        }

        /* Employee table layout */
        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 560px;
        }

        /* Table heading row */
        thead {
            background: #173b70;
            color: #ffffff;
        }

        th {
            padding: 15px 16px;
            text-align: left;
            font-size: 13px;
            font-weight: 600;
            letter-spacing: 0.3px;
        }

        /* Table data cells */
        td {
            padding: 16px;
            border-bottom: 1px solid #edf0f5;
            font-size: 14px;
        }

        /* Removes the bottom border from the final table row */
        tbody tr:last-child td {
            border-bottom: none;
        }

        /* Highlights a row when the pointer moves over it */
        tbody tr:hover {
            background: #f7faff;
        }

        /* Styling for employee ID */
        .employee-id {
            color: #64748b;
            font-weight: 600;
        }

        /* Styling for employee name */
        .employee-name {
            color: #173b70;
            font-weight: 600;
        }

        /* Displays the department as a small badge */
        .department {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 20px;
            background: #eef5ff;
            color: #2459a6;
            font-size: 12px;
            font-weight: 600;
        }

        /* Message displayed when no employee records exist */
        .empty-message {
            padding: 28px;
            text-align: center;
            color: #64748b;
        }

        /* Footer at the bottom of the page */
        .footer {
            margin-top: 18px;
            text-align: center;
            color: #94a3b8;
            font-size: 12px;
        }

        /* Container for the Add Employee form */
        .employee-form {
            margin: 24px 0;
            padding: 20px;
            background: #f8faff;
            border: 1px solid #e5eaf2;
            border-radius: 10px;
        }

        .employee-form h3 {
            margin: 0 0 18px;
            color: #173b70;
            font-size: 17px;
        }

        /* Places the three input fields in a responsive grid */
        .form-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 16px;
        }

        /* Arranges each form label and input vertically */
        .form-group {
            display: flex;
            flex-direction: column;
            gap: 7px;
        }

        .form-group label {
            color: #334155;
            font-size: 13px;
            font-weight: 600;
        }

        /* Appearance of the employee form input fields */
        .form-group input {
            width: 100%;
            padding: 11px 12px;
            border: 1px solid #cbd5e1;
            border-radius: 7px;
            font-size: 14px;
            background: #ffffff;
        }

        /* Highlights an input field when it is selected */
        .form-group input:focus {
            outline: none;
            border-color: #2459a6;
            box-shadow: 0 0 0 3px rgba(36, 89, 166, 0.12);
        }

        /* Styling for the Add Employee button */
        .add-button {
            margin-top: 18px;
            padding: 11px 18px;
            border: none;
            border-radius: 7px;
            background: #173b70;
            color: #ffffff;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
        }

        /* Changes the button color when hovered */
        .add-button:hover {
            background: #2459a6;
        }

        /* Shows form fields in one column on smaller screens */
        @media (max-width: 700px) {
            .form-grid {
                grid-template-columns: 1fr;
            }
        }

        /* Additional layout adjustments for mobile screens */
        @media (max-width: 600px) {
            body {
                padding: 20px 12px;
            }

            .header h1 {
                font-size: 23px;
            }

            .card {
                padding: 16px;
                border-radius: 10px;
            }

            .card-heading {
                align-items: flex-start;
                flex-direction: column;
            }

            th, td {
                padding: 12px;
            }
        }


        /* Keeps the Edit and Delete actions aligned */
        .action-buttons {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        /* Shared appearance for action buttons */
        .action-button {
            display: inline-block;
            padding: 7px 12px;
            border: none;
            border-radius: 6px;
            font-size: 13px;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
        }

        /* Edit button */
        .edit-button {
            background: #eaf2ff;
            color: #2459a6;
        }

        .edit-button:hover {
            background: #d6e6ff;
        }

        /* Delete button */
        .delete-button {
            background: #fee2e2;
            color: #b91c1c;
        }

        .delete-button:hover {
            background: #fecaca;
        }
    </style>
</head>

<body>
<div class="container">

    <!-- Page title and short description -->
    <header class="header">
        <h1>Employee Performance Evaluation System</h1>
        <p>Manage employee information and performance records.</p>
    </header>

    <main class="card">

        <!-- Directory heading and dynamically calculated employee count -->
        <div class="card-heading">
            <h2>Employee Directory</h2>

            <span class="employee-count">
                <%-- Display 0 if the employee list is null; otherwise display its size --%>
                <%= employees == null ? 0 : employees.size() %> Employees
            </span>
        </div>

        <%--
            Read the success parameter from the URL.
            EmployeeServlet adds this parameter after a successful operation.
        --%>
        <%
            String success = request.getParameter("success");
            String successMessage = null;

            // Select the appropriate message based on the operation
            if ("added".equals(success)) {
                successMessage = "Employee added successfully.";
            } else if ("updated".equals(success)) {
                successMessage = "Employee updated successfully.";
            } else if ("deleted".equals(success)) {
                successMessage = "Employee deleted successfully.";
            }

            // Display the message only when a recognized success value exists
            if (successMessage != null) {
        %>
            <div class="success-message">
                <%= successMessage %>
            </div>
        <%
            }
        %>

        <!-- Form submits new employee details to EmployeeServlet -->
        <form action="<%= request.getContextPath() %>/employees"
              method="post"
              class="employee-form">

            <h3>Add New Employee</h3>

            <div class="form-grid">

                <!-- Input field for employee name -->
                <div class="form-group">
                    <label for="name">Employee Name</label>
                    <input type="text"
                           id="name"
                           name="name"
                           placeholder="Enter employee name"
                           required>
                </div>

                <!-- Input field for employee department -->
                <div class="form-group">
                    <label for="department">Department</label>
                    <input type="text"
                           id="department"
                           name="department"
                           placeholder="Enter department"
                           required>
                </div>

                <!-- Input field for employee job title -->
                <div class="form-group">
                    <label for="jobTitle">Job Title</label>
                    <input type="text"
                           id="jobTitle"
                           name="jobTitle"
                           placeholder="Enter job title"
                           required>
                </div>
            </div>

            <!-- Submits the form to create a new employee record -->
            <button type="submit" class="add-button">Add Employee</button>
        </form>

        <!-- Employee table containing employee details and actions -->
        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Employee ID</th>
                    <th>Name</th>
                    <th>Department</th>
                    <th>Job Title</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody>
                <%--
                    Check whether the employee list exists and contains records.
                    If records exist, loop through the list and display each employee.
                --%>
                <%
                    if (employees != null && !employees.isEmpty()) {
                        for (Employee employee : employees) {
                %>
                <tr>
                    <!-- Display the employee's unique ID -->
                    <td class="employee-id">
                        <%= employee.getEmployeeId() %>
                    </td>

                    <!-- Display the employee's name -->
                    <td class="employee-name">
                        <%= employee.getName() %>
                    </td>

                    <!-- Display the employee's department -->
                    <td>
                        <span class="department">
                            <%= employee.getDepartment() %>
                        </span>
                    </td>

                    <!-- Display the employee's job title -->
                    <td>
                        <%= employee.getJobTitle() %>
                    </td>


                   <!-- Edit and Delete actions for the current employee -->
                   <td>
                       <div class="action-buttons">

                           <!-- Opens the edit page for the selected employee -->
                           <a class="action-button edit-button"
                              href="<%= request.getContextPath() %>/employees?id=<%= employee.getEmployeeId() %>">
                               Edit
                           </a>

                           <!-- Sends a delete request to EmployeeServlet -->
                           <form action="<%= request.getContextPath() %>/employees"
                                 method="post"
                                 style="display: inline;"
                                 onsubmit="return confirm('Are you sure you want to delete this employee?');">

                               <input type="hidden" name="action" value="delete">
                               <input type="hidden" name="employeeId"
                                      value="<%= employee.getEmployeeId() %>">

                               <button type="submit"
                                       class="action-button delete-button">
                                   Delete
                               </button>
                           </form>

                       </div>
                   </td>
                </tr>
                <%
                        }
                    } else {
                %>

                <!-- Display a message when the employee list is empty -->
                <tr>
                    <td colspan="5" class="empty-message">
                        No employees found.
                    </td>
                </tr>

                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </main>

    <!-- Page footer -->
    <footer class="footer">
        Employee Performance Evaluation System
    </footer>

</div>
</body>
</html>