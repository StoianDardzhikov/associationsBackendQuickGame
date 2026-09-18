package com.stoyan.associationGame;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class Controller {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private SimpleTextHandler simpleTextHandler;

    static String[] teamColors = new String[]{"Сини", "Зелени", "Червени", "Розови", "Оранжеви", "Бели"};

    static HashMap<Integer, Game> gameList = new HashMap<>();
    @Autowired
    private AssociationGameApplication associationGameApplication;

    @Operation
    @GetMapping("/game")
    List<Game> getGames() {
        return gameList.values().stream().toList();
    }


    @Operation
    @GetMapping("/game/{id}")
    Game getGames(@PathVariable int id) {
        return requireGame(id);
    }

    @Operation
    @PostMapping("/game")
    Game addGame(@RequestBody Game game) {
        game.setId(Game.GAMES_COUNT++);
        gameList.put(game.getId(), game);
        return game;
    }

    @Operation
    @PutMapping("/game")
    Game edit(@RequestBody Game game) {
        gameList.put(game.getId(), game);
        return game;
    }

    @Operation
    @PostMapping("/game/join")
    Player joinGame(@RequestParam String playerName, @RequestParam int gameId, @RequestBody List<String> words) {
        Player player = new Player(Player.PLAYER_COUNT++, playerName);
        Game game = requireGame(gameId);
        game.getPlayers().add(player);
        game.getWords().addAll(words);
        broadcast(gameId, new JoinEvent(playerName));
        return player;
    }

    @Operation
    @PostMapping("/game/start")
    List<Team> startGame(@RequestParam int gameId) {
        Game game = requireGame(gameId);
        int playerCount = game.getPlayers().size();
        int playersPerTeam = Math.max(1, game.getPlayersPerTeam());
        int teamCount = (int) Math.ceil(((double) playerCount) / playersPerTeam);
        // Never ask for more teams than we have colours for, and always make at least one.
        teamCount = Math.min(Math.max(teamCount, 1), teamColors.length);
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < teamCount; i++) {
            teams.add(new Team());
            teams.get(i).setColor(teamColors[i]);
        }
        Collections.shuffle(game.getPlayers());
        for (int i = 0; i < playerCount; i++) {
            teams.get(i % teamCount).getPlayers().add(game.getPlayers().get(i));
            String color = teamColors[i % teamCount];
            game.getPlayers().get(i).setColor(color);
        }
        game.setTeams(teams);
        game.getRoundState().update(false, 0, 1, null, null, false, 0);
        broadcast(gameId, new StartGameEvent(game.getPlayers()));
        return teams;
    }

    @Operation
    @PostMapping("/game/score")
    void scorePoint(@RequestParam int gameId, @RequestParam int playerId) {
        Game game = requireGame(gameId);
        game.addPointToTeam(playerId);
        broadcast(gameId, new ScoreEvent(game.getTeams()));
    }

    /**
     * Published by the host whenever the countdown changes (round start, skip penalty, round end).
     * Clients tick down locally between updates and re-sync on every websocket event or poll.
     */
    @Operation
    @PostMapping("/game/round")
    RoundState updateRound(@RequestParam int gameId,
                           @RequestParam boolean active,
                           @RequestParam int secondsLeft,
                           @RequestParam(required = false, defaultValue = "0") int round,
                           @RequestParam(required = false) String contestantName,
                           @RequestParam(required = false) String teamColor,
                           @RequestParam(required = false, defaultValue = "false") boolean finished,
                           @RequestParam(required = false, defaultValue = "0") int totalSeconds) {
        Game game = requireGame(gameId);
        RoundState roundState = game.getRoundState();
        roundState.update(active, secondsLeft, round, contestantName, teamColor, finished, totalSeconds);
        broadcast(gameId, new RoundEvent(roundState, game.getTeams()));
        return roundState;
    }

    @Operation
    @DeleteMapping("/game/delete")
    void deleteGame(@RequestParam int gameId) {
        gameList.remove(gameId);
        simpleTextHandler.removeGame(gameId);
    }

    private Game requireGame(int gameId) {
        Game game = gameList.get(gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game " + gameId + " not found");
        }
        return game;
    }

    /** Best effort: a websocket problem must never fail the HTTP call, clients also poll. */
    private void broadcast(int gameId, Object event) {
        try {
            simpleTextHandler.broadcast(gameId, MAPPER.writeValueAsString(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
