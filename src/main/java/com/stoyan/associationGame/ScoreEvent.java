package com.stoyan.associationGame;

import java.util.List;

public class ScoreEvent {
    private List<Team> teams;
    private String event = "score";

    public ScoreEvent(List<Team> teams) {
        this.teams = teams;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}