package com.stoyan.associationGame;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String color;
    private List<Player> players = new ArrayList<>();
    private int points;

    public boolean userIsPartOf(int playerId) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                return true;
            }
        }
        return false;
    }

    public void increasePoints() {
        points += 1;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
