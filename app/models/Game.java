package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

	private static final int DEFAULT_BOARD_SIZE = 10;
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
	
	private static final Pattern COORD_PATTERN = Pattern.compile("^\\(?([0-9]{1,3}),\\s?([0-9]{1,3})\\)?$");
	
	public static void main(String[] args) throws Exception {
		Game game = new Game();
		System.out.println("Starting a game of " + game.getNumToWin() + "-in-a-row");
		Character winner = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (winner == null) {
			System.out.println("Board:\n" + game.getBoard());
			System.out.println("Current player: " + game.getCurrentPlayer());
			System.out.println("Enter coordinate as (row, col): ");
			winner = doValidPlay(game, reader);
		}
		System.out.println("Player " + winner + " has won! Game over!");
		System.exit(0);
	}
	
	private static Character doValidPlay(Game game, BufferedReader reader) throws IOException {
		while (true) {
			Matcher matcher = getValidInput(reader);
			int x = Integer.parseInt(matcher.group(1));
			int y = Integer.parseInt(matcher.group(2));
			if (game.getBoard().getPlayer(x, y) == null) {
				Character currentPlayer = game.getCurrentPlayer();
				game.play(x, y, currentPlayer);
				return game.checkWinner(currentPlayer, x, y);
			}
			System.out.println("Can't play on a cell that's already been played, try again!");
		}
	}
	
	private static Matcher getValidInput(BufferedReader reader) throws IOException {
		while (true) {
			String line = reader.readLine();
			line.trim();
			Matcher matcher = COORD_PATTERN.matcher(line);
			if (matcher.matches()) {
				return matcher;
			}
			System.out.println("Invalid input, try again!");
		}
	}
}
