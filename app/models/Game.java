package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game {

	private static final int DEFAULT_BOARD_SIZE = 20;
	private static final int DEFAULT_NUM_TO_WIN = 5;
	private static final List<Character> DEFAULT_PLAYERS = new ArrayList<Character>();
	{
		DEFAULT_PLAYERS.add('X');
		DEFAULT_PLAYERS.add('O');
	}
	
	private final int numToWin;
	private Board board;
	private List<Character> players;
	private int currentPlayer;
	
	
	public Game() {
		this(DEFAULT_BOARD_SIZE, DEFAULT_NUM_TO_WIN, DEFAULT_PLAYERS);
	}
	
	public Game(int boardSize, int numToWin, List<Character> players) {
		if (numToWin < 1) {
			throw new IllegalArgumentException("The number in a row to win must be positive.");
		}
		if (boardSize < numToWin) {
			throw new IllegalArgumentException("The board must be at least bigger than the number to win");
		}
		for (Character p : players) {
			if (p == null) {
				throw new IllegalStateException("Players cannot be null");
			}
		}
		this.board = new Board(boardSize);
		this.numToWin = numToWin;
		this.players = players;
		this.currentPlayer = 0;
	}
	
	public int getNumToWin() {
		return numToWin;
	}
	
	public char getCurrentPlayer() {
		return players.get(currentPlayer);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public boolean play(int x, int y, char player) {
		if (player != getCurrentPlayer()) {
			return false;
		}
		board.play(x, y, player);
		currentPlayer++;
		if (currentPlayer >= players.size()) {
			currentPlayer = 0;
		}
		return true;
	}
	
	public Character checkWinner() {
		for (int x = 0; x < board.getSize(); x++) {
			for (int y = 0; y < board.getSize(); y++) {
				Character player = board.getPlayer(x, y);
				if (player != null) {
					Character winner = checkWinner(player, x, y);
					if (winner != null) {
						return winner;
					}
				}
			}
		}
		return null;
	}
	
	public Character checkWinner(char player, int x, int y) {
		return board.checkNumInARow(player, x, y, numToWin);
	}
}
