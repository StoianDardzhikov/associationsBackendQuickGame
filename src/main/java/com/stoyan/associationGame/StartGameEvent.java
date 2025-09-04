package com.stoyan.associationGame;

import java.util.List;

public class StartGameEvent {
    private String event = "start";
    private List<Player> playerList;

    public StartGameEvent(List<Player> playerList) {
        this.playerList = playerList;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
