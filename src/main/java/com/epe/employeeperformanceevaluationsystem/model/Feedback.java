package com.epe.employeeperformanceevaluationsystem.model;

// Represents feedback provided for an employee in the performance evaluation system.
public class Feedback {

    // Stores the unique feedback ID and the ID of the employee receiving feedback.
    private int feedbackId;
    private int employeeId;

    // Stores the feedback rating and written comments.
    private int rating;
    private String comments;

    // Constructor to initialize all feedback details when a Feedback object is created.
    public Feedback(int feedbackId, int employeeId, int rating, String comments) {
        this.feedbackId = feedbackId;
        this.employeeId = employeeId;
        this.rating = rating;
        this.comments = comments;
    }

    // Returns the unique ID of the feedback.
    public int getFeedbackId() {
        return feedbackId;
    }

    // Returns the ID of the employee associated with this feedback.
    public int getEmployeeId() {
        return employeeId;
    }

    // Returns the rating given to the employee.
    public int getRating() {
        return rating;
    }

    // Returns the written feedback comments.
    public String getComments() {
        return comments;
    }
}