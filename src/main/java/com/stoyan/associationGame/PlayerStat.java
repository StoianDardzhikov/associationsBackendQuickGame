package com.stoyan.associationGame;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** How one player did across the whole game, as the explainer. */
public class PlayerStat {

    private int id;
    private String name;
    private int guessed;
    private int skipped;
    private int bestStreak;

    /** Running streak, reset by a skip. Not part of the payload. */
    @JsonIgnore
    private int currentStreak;

    public PlayerStat() {
    }

    public PlayerStat(int id, String name) {
        this.id = id;
        this.name = name;
    }

    void recordGuess() {
        guessed++;
        currentStreak++;
        if (currentStreak > bestStreak) {
            bestStreak = currentStreak;
        }
    }

    void recordSkip() {
        skipped++;
        currentStreak = 0;
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

    public int getGuessed() {
        return guessed;
    }

    public void setGuessed(int guessed) {
        this.guessed = guessed;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }
}
