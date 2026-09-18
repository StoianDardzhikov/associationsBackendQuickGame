package com.stoyan.associationGame;

/** How often one word was guessed or skipped over the course of a game. */
public class WordStat {

    private String word;
    private int guessed;
    private int skipped;

    public WordStat() {
    }

    public WordStat(String word) {
        this.word = word;
    }

    void recordGuess() {
        guessed++;
    }

    void recordSkip() {
        skipped++;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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
}
