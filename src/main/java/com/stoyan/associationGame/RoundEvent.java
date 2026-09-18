package com.stoyan.associationGame;

import java.util.List;

public class RoundEvent {
    private String event = "round";
    private RoundState roundState;
    private List<Team> teams;
    private GameStats stats;

    public RoundEvent(RoundState roundState, List<Team> teams, GameStats stats) {
        this.roundState = roundState;
        this.teams = teams;
        this.stats = stats;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public RoundState getRoundState() {
        return roundState;
    }

    public void setRoundState(RoundState roundState) {
        this.roundState = roundState;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public GameStats getStats() {
        return stats;
    }

    public void setStats(GameStats stats) {
        this.stats = stats;
    }
}
