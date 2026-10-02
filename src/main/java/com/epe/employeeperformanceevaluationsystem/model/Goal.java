package com.epe.employeeperformanceevaluationsystem.model;

import java.time.LocalDate;

// Represents a goal assigned to an employee in the performance evaluation system.
public class Goal {

    // Stores the unique goal ID and the ID of the employee assigned to the goal.
    private int goalId;
    private int employeeId;

    // Stores the goal description, target completion date, and current status.
    private String description;
    private LocalDate targetDate;
    private String status;

    // Constructor to initialize all goal details when a Goal object is created.
    public Goal(int goalId, int employeeId, String description, LocalDate targetDate, String status) {
        this.goalId = goalId;
        this.employeeId = employeeId;
        this.description = description;
        this.targetDate = targetDate;
        this.status = status;
    }

    // Returns the unique ID of the goal.
    public int getGoalId() {
        return goalId;
    }

    // Returns the ID of the employee assigned to this goal.
    public int getEmployeeId() {
        return employeeId;
    }

    // Returns the description of the goal.
    public String getDescription() {
        return description;
    }

    // Returns the target date for completing the goal.
    public LocalDate getTargetDate() {
        return targetDate;
    }

    // Returns the current status of the goal.
    public String getStatus() {
        return status;
    }

    // Updates the goal's status, such as "In Progress" or "Completed".
    public void setStatus(String status) {
        this.status = status;
    }
}