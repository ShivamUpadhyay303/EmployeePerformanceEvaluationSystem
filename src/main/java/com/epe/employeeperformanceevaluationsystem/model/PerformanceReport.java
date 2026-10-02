package com.epe.employeeperformanceevaluationsystem.model;

// Represents a summary of an employee's performance evaluation results.
public class PerformanceReport {

    // Stores the employee's ID, total number of evaluations, and average rating.
    private int employeeId;
    private int totalEvaluations;
    private double averageRating;

    // Constructor to initialize the employee's performance report details.
    public PerformanceReport(int employeeId, int totalEvaluations, double averageRating) {
        this.employeeId = employeeId;
        this.totalEvaluations = totalEvaluations;
        this.averageRating = averageRating;
    }

    // Returns the ID of the employee whose performance is being reported.
    public int getEmployeeId() {
        return employeeId;
    }

    // Returns the total number of evaluations received by the employee.
    public int getTotalEvaluations() {
        return totalEvaluations;
    }

    // Returns the average rating calculated from the employee's evaluations.
    public double getAverageRating() {
        return averageRating;
    }
}