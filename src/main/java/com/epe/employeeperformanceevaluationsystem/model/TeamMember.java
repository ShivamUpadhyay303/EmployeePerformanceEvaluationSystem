package com.epe.employeeperformanceevaluationsystem.model;

// Represents an employee's membership in a team.
public class TeamMember {

    // Stores the employee ID, team ID, and the employee's role in that team.
    private int employeeId;
    private int teamId;
    private String role;

    // Constructor to initialize team membership details.
    public TeamMember(int employeeId, int teamId, String role) {
        this.employeeId = employeeId;
        this.teamId = teamId;
        this.role = role;
    }

    // Returns the ID of the employee belonging to the team.
    public int getEmployeeId() {
        return employeeId;
    }

    // Returns the ID of the team the employee belongs to.
    public int getTeamId() {
        return teamId;
    }

    // Returns the employee's role within the team.
    public String getRole() {
        return role;
    }
}