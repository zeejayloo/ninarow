package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import models.Game;
import play.mvc.Controller;

public class Application extends Controller {
  
	private static Map<String, Game> games = new HashMap<String, Game>();

    public static void newGame() {
    	String id = UUID.randomUUID().toString();
    	Game game = new Game();
    	games.put(id, game);
    	showGame(id);
    }
    
    public static void showGame(String id) {
    	Game game = games.get(id);
    	Character winner = game.checkWinner();
    	render(id, game, winner);
    }
    
    public static void play(String id, int row, int col, String player) {
    	Game game = games.get(id);
    	char playerChar = player.charAt(0);
    	boolean success = game.play(row, col, playerChar);
    	JsonObject result = new JsonObject();
    	if (success) {
	    	Character winner = game.checkWinner(playerChar, row, col);
	    	result.addProperty("winner", winner);
	    	result.addProperty("currentPlayer", game.getCurrentPlayer());
    	} else {
    		result.addProperty("error", "Refresh!");
    	}
    	renderJSON(result.toString());
    }
    
    public static void deleteGame(String id) {
    	games.remove(id);
    	newGame();
    }
}

