package com.epe.employeeperformanceevaluationsystem.model;

// Represents a team in the Employee Performance Evaluation System.
public class Team {

    // Stores the unique ID and name of the team.
    private int teamId;
    private String teamName;

    // Constructor to initialize team details when a Team object is created.
    public Team(int teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    // Returns the unique ID of the team.
    public int getTeamId() {
        return teamId;
    }

    // Returns the name of the team.
    public String getTeamName() {
        return teamName;
    }
}