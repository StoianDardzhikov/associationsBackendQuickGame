package com.stoyan.associationGame;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-word and per-player tallies, accumulated as the host plays through the game.
 * Feeds the end-of-game recap; the raw maps stay internal and only sorted views are
 * serialised, so clients get something they can render directly.
 */
public class GameStats {

    /** How many hardest words to hand back. */
    private static final int HARDEST_LIMIT = 3;

    @JsonIgnore
    private final Map<Integer, PlayerStat> playerStats = new ConcurrentHashMap<>();

    @JsonIgnore
    private final Map<String, WordStat> wordStats = new ConcurrentHashMap<>();

    private int totalGuessed;
    private int totalSkipped;

    public synchronized void record(int playerId, String playerName, String word, boolean guessed) {
        PlayerStat player = playerStats.computeIfAbsent(playerId, id -> new PlayerStat(id, playerName));
        if (player.getName() == null && playerName != null) {
            player.setName(playerName);
        }

        WordStat wordStat = (word == null || word.isBlank())
                ? null
                : wordStats.computeIfAbsent(word, WordStat::new);

        if (guessed) {
            totalGuessed++;
            player.recordGuess();
            if (wordStat != null) {
                wordStat.recordGuess();
            }
        } else {
            totalSkipped++;
            player.recordSkip();
            if (wordStat != null) {
                wordStat.recordSkip();
            }
        }
    }

    /** Explainers, best first. */
    public List<PlayerStat> getPlayers() {
        List<PlayerStat> list = new ArrayList<>(playerStats.values());
        list.sort(Comparator
                .comparingInt(PlayerStat::getGuessed).reversed()
                .thenComparing(Comparator.comparingInt(PlayerStat::getBestStreak).reversed()));
        return list;
    }

    /** Words that got skipped most - ties broken by how rarely they were guessed. */
    public List<WordStat> getHardestWords() {
        return wordStats.values().stream()
                .filter(w -> w.getSkipped() > 0)
                .sorted(Comparator
                        .comparingInt(WordStat::getSkipped).reversed()
                        .thenComparing(Comparator.comparingInt(WordStat::getGuessed)))
                .limit(HARDEST_LIMIT)
                .toList();
    }

    public int getTotalGuessed() {
        return totalGuessed;
    }

    public void setTotalGuessed(int totalGuessed) {
        this.totalGuessed = totalGuessed;
    }

    public int getTotalSkipped() {
        return totalSkipped;
    }

    public void setTotalSkipped(int totalSkipped) {
        this.totalSkipped = totalSkipped;
    }
}
