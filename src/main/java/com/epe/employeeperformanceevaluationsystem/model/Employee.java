package com.epe.employeeperformanceevaluationsystem.model;
// Represents an employee in the Employee Performance Evaluation System.
public class Employee {
    // Private fields to store employee information.
    private int employeeId;
    private String name;
    private String department;
    private String jobTitle;

    // Constructor to initialize employee details when an Employee object is created.
    public Employee(int employeeId, String name, String department, String jobTitle){
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.jobTitle = jobTitle;
    }

    // Returns the employee's unique ID.
    public int getEmployeeId(){
        return employeeId;
    }

    // Returns the employee's name
    public String getName(){
        return name;
    }

    // Updates the employee's name.
    public void setName(String name){
        this.name = name;
    }

    // Returns the employee's department.
    public String getDepartment(){
        return department;
    }

    // Updates the employee's department.
    public void setDepartment(String department){
        this.department = department;
    }

    // Returns the employee's job title.
    public String getJobTitle(){
        return jobTitle;
    }

    // Updates the employee's job title.
    public void setJobTitle(String jobTitle){
        this.jobTitle = jobTitle;
    }
}
