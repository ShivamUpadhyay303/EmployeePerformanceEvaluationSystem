# Employee Performance Evaluation System (EPES)

A Java-based web application designed to manage employee information, performance evaluations, goals, feedback, and performance reports.

## 1. Project Overview

The Employee Performance Evaluation System (EPES) is a web-based application that simplifies employee performance management.

It provides separate access for Managers and Employees. Managers can manage employee records, evaluate performance, assign goals, and provide feedback. Employees can log in to view their performance reports, track goals, and review feedback.

## 2. Project Objectives

- Simplify employee performance evaluation.
- Maintain employee information digitally.
- Allow managers to evaluate employee performance.
- Track employee goals and progress.
- Improve communication through structured feedback.
- Provide secure and role-based access.

## 3. Technologies Used

| Technology | Purpose |
|---|---|
| Java 25 | Backend development |
| Jakarta Servlets 6.1 | Request handling |
| JSP | Dynamic web pages |
| HTML5 | Web page structure |
| CSS3 | Styling and responsive design |
| JavaScript | Frontend functionality |
| MySQL 8 | Database management |
| JDBC | Database connectivity |
| Apache Tomcat 11 | Application server |
| Maven | Build and dependency management |
| IntelliJ IDEA | Development environment |

## 4. Main Features

### Manager Module

- Secure manager login.
- Add new employee records.
- View the employee directory.
- Update employee information.
- Delete employee records.
- Add employee performance evaluations.
- Assign goals to employees.
- Track employee goal progress.
- Provide ratings and feedback.
- View employee performance reports.

### Employee Module

- Secure employee login.
- Personalized employee dashboard.
- View personal performance evaluations.
- View overall performance ratings.
- View assigned goals.
- Update goal progress.
- View feedback provided by managers.

## 5. Security Features

- Password hashing using PBKDF2WithHmacSHA256.
- Role-based access control for Managers and Employees.
- Session management and secure logout.
- CSRF protection for POST requests.
- Prepared statements for database operations.
- Input validation in application modules.
- Database CHECK constraints for ratings and goal statuses.
- Employee-specific data access using authenticated employee IDs.

## 6. Database Design

**Database name:** `epes_db`

The project uses MySQL to store employee and performance-related information.

### Database Tables

| Table | Purpose |
|---|---|
| employees | Stores employee information |
| users | Stores login accounts and roles |
| evaluations | Stores performance evaluation records |
| goals | Stores employee goals and progress |
| feedback | Stores manager feedback |
| teams | Stores team information |
| team_members | Maintains employee-team relationships |
| performance_reports | Reserved for report data; reports are currently calculated dynamically by the application |

The database uses primary keys, foreign keys, and CHECK constraints to maintain data integrity.

### Database Constraints

- Evaluation ratings must be between 1 and 5.
- Feedback ratings must be between 1 and 5.
- Goal status must be Pending, In Progress, or Completed.

## 7. Project Structure

The project follows a modular Java web application structure.

```text
EmployeePerformanceEvaluationSystem/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/epe/employeeperformanceevaluationsystem/
│       │       ├── dao/
│       │       ├── model/
│       │       ├── servlet/
│       │       └── util/
│       │
│       └── webapp/
│           ├── WEB-INF/
│           ├── css/
│           ├── index.jsp
│           ├── employee-dashboard.jsp
│           ├── manager-dashboard.jsp
│           ├── employees.jsp
│           ├── edit-employee.jsp
│           ├── evaluations.jsp
│           ├── goals.jsp
│           ├── feedback.jsp
│           └── other JSP pages
│
├── .mvn/
├── mvnw
├── mvnw.cmd
├── epes_database.sql
├── pom.xml
├── README.md
└── README_Review1_Backup.md
```

## 8. System Requirements

Install the following software before running the application:

- JDK 25
- MySQL Server 8
- MySQL Workbench
- Apache Tomcat 11
- IntelliJ IDEA
- Git (for version control)

## 9. Installation and Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/ShivamUpadhyay303/EmployeePerformanceEvaluationSystem.git
```

Open the downloaded project in IntelliJ IDEA.

### Step 2: Create the Database

1. Open MySQL Workbench and connect to your MySQL server.
2. Create the database by executing:

```sql
CREATE DATABASE epes_db;
```

3. Select `epes_db` as the default schema in MySQL Workbench before executing the SQL file.
4. Open the `epes_database.sql` file from the project root folder.
5. Execute its SQL statements in MySQL Workbench.
6. Refresh the SCHEMAS panel and verify that all eight tables have been created.

**Note:** The SQL file contains database structure only. It does not contain existing employee records or login accounts. Create the required records and manager account separately.

### Step 3: Configure Database Connection

Configure your MySQL connection in the DBConnection utility.

Set the environment variable:

```text
EPES_DB_PASSWORD
```

Use the password of your configured MySQL account as its value.

Do not commit database passwords or other secrets to GitHub.

### Step 4: Build the Project

Open a terminal in the project root directory and execute:

```powershell
java -classpath .mvn\wrapper\maven-wrapper.jar "-Dmaven.multiModuleProjectDirectory=$PWD" org.apache.maven.wrapper.MavenWrapperMain package
```

After a successful build, Maven generates the WAR file inside the `target` directory.

### Step 5: Deploy on Apache Tomcat

1. Copy the generated WAR file from the `target` directory.
2. Place it inside the Tomcat `webapps` directory.
3. Start the Tomcat server.
4. Wait for the application to deploy.

### Step 6: Open the Application

Open the following URL in your browser:

```text
http://localhost:8080/EmployeePerformanceEvaluationSystem-1.0-SNAPSHOT/
```

## 10. Testing

The following application features have been tested:

- Manager and employee login.
- Employee management operations (Add, View, Update, Delete).
- Manager and employee dashboard access.
- Role-based access restrictions.
- Performance evaluation functionality.
- Goal management and progress updates.
- Feedback management.
- Performance report access.
- Logout and protected-page access.
- Database CHECK constraint enforcement.
- Maven project build.

## 11. Screenshots

Add actual screenshots of the following application pages:

- Login Page
- Manager Dashboard
- Employee Dashboard
- Employee Management
- Performance Evaluations
- Goal Management
- Feedback Page
- Performance Reports

## 12. Future Enhancements

- Forgot Password functionality.
- Automatic employee account creation.
- Email notifications.
- Downloadable performance reports.

## 13. GitHub Repository

**Repository Link:** https://github.com/ShivamUpadhyay303/EmployeePerformanceEvaluationSystem

## 14. Team Members

Add the actual names of project team members and their respective contributions here.

## 15. Project Status

The main application modules and core functional tests have been completed. Documentation, repository preparation, and final presentation are being finalized.

---

**Employee Performance Evaluation System (EPES)**

Developed as an academic project.