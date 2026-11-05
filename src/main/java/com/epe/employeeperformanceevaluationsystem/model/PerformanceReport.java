package com.epe.employeeperformanceevaluationsystem.model;

public class PerformanceReport {

    private int employeeId;
    private String employeeName;
    private int totalEvaluations;
    private double averageRating;

    public PerformanceReport(int employeeId, int totalEvaluations, double averageRating) {
        this.employeeId = employeeId;
        this.totalEvaluations = totalEvaluations;
        this.averageRating = averageRating;
    }

    public PerformanceReport(int employeeId, String employeeName,
                             int totalEvaluations, double averageRating) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalEvaluations = totalEvaluations;
        this.averageRating = averageRating;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getTotalEvaluations() {
        return totalEvaluations;
    }

    public double getAverageRating() {
        return averageRating;
    }
}