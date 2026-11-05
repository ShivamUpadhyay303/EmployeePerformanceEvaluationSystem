
package com.epe.employeeperformanceevaluationsystem.model;

public class Feedback {

    private int feedbackId;
    private int employeeId;
    private int rating;
    private String comments;
    private String employeeName;

    // Constructor
    public Feedback(int feedbackId, int employeeId,
                    int rating, String comments) {

        this.feedbackId = feedbackId;
        this.employeeId = employeeId;
        this.rating = rating;
        this.comments = comments;
    }

    // Constructor with employee name
    public Feedback(int feedbackId, int employeeId,
                    int rating, String comments,
                    String employeeName) {

        this.feedbackId = feedbackId;
        this.employeeId = employeeId;
        this.rating = rating;
        this.comments = comments;
        this.employeeName = employeeName;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    public String getEmployeeName() {
        return employeeName;
    }
}
