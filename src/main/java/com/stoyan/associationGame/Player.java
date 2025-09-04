package com.stoyan.associationGame;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Player {

    public static int PLAYER_COUNT = 0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String color;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
