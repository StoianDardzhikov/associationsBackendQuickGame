package com.stoyan.associationGame;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {

    @Autowired
    private SimpleTextHandler simpleTextHandler;

    static String[] teamColors = new String[]{"Сини", "Зелени", "Червени", "Розови", "Оранжеви", "Бели"};

    static HashMap<Integer, Game> gameList = new HashMap<>();

    @Operation
    @GetMapping("/game")
    List<Game> getGames() {
        return gameList.values().stream().toList();
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
    Player joinGame(@RequestParam String playerName, @RequestParam int gameId) {
        Player player = new Player(Player.PLAYER_COUNT++, playerName);
        Game game = gameList.get(gameId);
        game.getPlayers().add(player);
        return player;
    }

    @Operation
    @PostMapping("/game/start")
    List<Team> startGame(@RequestParam int gameId) {
        Game game = gameList.get(gameId);
        int playerCount = game.getPlayers().size();
        int teamCount = (int) Math.ceil( ((double) playerCount) / game.getPlayersPerTeam());
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < teamCount; i++) {
            teams.add(new Team());
            teams.get(i).setColor(teamColors[i]);
        }
        Collections.shuffle(game.getPlayers());
        for (int i = 0; i < playerCount; i++) {
            teams.get(i % teamCount).getPlayers().add(game.getPlayers().get(i));
        }
        game.setTeams(teams);

        return teams;
    }

    @Operation
    @PostMapping("/game/score")
    void scorePoint(@RequestParam int gameId, @RequestParam int playerId) {
        Game game = gameList.get(gameId);
        game.addPointToTeam(playerId);
        List<Team> teams = game.getTeams();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String message = mapper.writeValueAsString(teams);
            simpleTextHandler.broadcast(game.getId(), message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Operation
    @DeleteMapping("/game/delete")
    void deleteGame(@RequestParam int gameId) {
        gameList.remove(gameId);
    }
}
