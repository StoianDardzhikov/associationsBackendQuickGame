package com.stoyan.associationGame;

import java.util.ArrayList;
import java.util.List;


public class Game {
    public static int GAMES_COUNT = 0;

    private int id;
    private List<Player> players = new ArrayList<Player>();
    private List<String> category;
    private int playersPerTeam;
    private List<String> words = new ArrayList<>();
    private List<Team> teams;

    public void addPointToTeam(int playerId) {
        for (Team team : teams) {
            if (team.userIsPartOf(playerId)) {
                team.increasePoints();
                break;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public int getPlayersPerTeam() {
        return playersPerTeam;
    }

    public void setPlayersPerTeam(int playersPerTeam) {
        this.playersPerTeam = playersPerTeam;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}