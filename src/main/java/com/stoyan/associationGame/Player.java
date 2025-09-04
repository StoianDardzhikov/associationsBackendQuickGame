package com.stoyan.associationGame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Player {

    public static int PLAYER_COUNT = 0;

    private int id;
    private String name;

    public Player(int id, String playerName) {
        this.id = id;
        this.name = playerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
