package com.epe.employeeperformanceevaluationsystem.model;

// Represents an employee's performance evaluation.
public class Evaluation {

    // Stores the evaluation ID and the ID of the employee being evaluated.
    private int evaluationId;
    private int employeeId;

    // Stores the evaluation criteria, rating, and feedback comments.
    private String criteria;
    private int rating;
    private String comments;

    // Constructor to initialize all evaluation details when an object is created.
    public Evaluation(int evaluationId, int employeeId, String criteria, int rating, String comments) {
        this.evaluationId = evaluationId;
        this.employeeId = employeeId;
        this.criteria = criteria;
        this.rating = rating;
        this.comments = comments;
    }

    // Returns the unique ID of the evaluation.
    public int getEvaluationId() {
        return evaluationId;
    }

    // Returns the ID of the employee associated with this evaluation.
    public int getEmployeeId() {
        return employeeId;
    }

    // Returns the performance criteria being evaluated.
    public String getCriteria() {
        return criteria;
    }

    // Returns the rating assigned to the employee.
    public int getRating() {
        return rating;
    }

    // Returns the evaluator's comments or feedback.
    public String getComments() {
        return comments;
    }
}