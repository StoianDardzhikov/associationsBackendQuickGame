package com.stoyan.associationGame;

/**
 * State of the round the host is currently running.
 * <p>
 * It is kept on the game so that every client can see the countdown, both through the
 * {@code round} websocket event and by polling {@code GET /game/{id}} (phones where the
 * websocket never connects rely on the polling path).
 * <p>
 * The remaining time is derived from {@link #endsAt}, a server timestamp, so a client with a
 * skewed clock still gets the correct number of seconds.
 */
public class RoundState {

    private boolean active;
    private boolean finished;
    private int round;
    private String contestantName;
    private String teamColor;

    /** Epoch millis (server clock) when the round ends. Only meaningful while active. */
    private long endsAt;

    /** Seconds left frozen at the moment the round was paused/ended. Used when not active. */
    private int frozenSeconds;

    /** Full length of the round, so clients can draw a progress ring. */
    private int totalSeconds;

    public int getSecondsLeft() {
        if (!active) {
            return Math.max(0, frozenSeconds);
        }
        long remainingMillis = endsAt - System.currentTimeMillis();
        return (int) Math.max(0, Math.round(remainingMillis / 1000.0));
    }

    public void update(boolean active, int secondsLeft, int round, String contestantName, String teamColor, boolean finished, int totalSeconds) {
        // 0 means "unchanged": the host only sends the total when a round starts,
        // not on every heartbeat.
        if (totalSeconds > 0) {
            this.totalSeconds = totalSeconds;
        }
        this.active = active;
        this.finished = finished;
        this.round = round;
        this.contestantName = contestantName;
        this.teamColor = teamColor;
        this.frozenSeconds = Math.max(0, secondsLeft);
        this.endsAt = System.currentTimeMillis() + (Math.max(0, secondsLeft) * 1000L);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getContestantName() {
        return contestantName;
    }

    public void setContestantName(String contestantName) {
        this.contestantName = contestantName;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    public long getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(long endsAt) {
        this.endsAt = endsAt;
    }

    public int getFrozenSeconds() {
        return frozenSeconds;
    }

    public void setFrozenSeconds(int frozenSeconds) {
        this.frozenSeconds = frozenSeconds;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }
}
