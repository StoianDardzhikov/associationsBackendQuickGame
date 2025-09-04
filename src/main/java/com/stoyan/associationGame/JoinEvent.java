package com.stoyan.associationGame;

public class JoinEvent {
    private String name;
    private String event = "join";
    public JoinEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
