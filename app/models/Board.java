package models;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private final int size;
	private final Character[][] board;
	
	public Board(int size) {
		this.size = size;
		this.board = new Character[size][size];
	}
	
	protected void play(int x, int y, char player) {
		board[x][y] = player;
	}
	
	public int getSize() {
		return size;
	}
	
	protected Character checkNumInARow(char player, int x, int y, int num) {
		if (checkNumInARow(player, x, y, 1, 0, num) ||
			checkNumInARow(player, x, y, 0, 1, num) ||
			checkNumInARow(player, x, y, 1, 1, num) ||
			checkNumInARow(player, x, y, -1, 1, num)) {
			return player;
		}
		return null;
	}
	
	public Character getPlayer(int x, int y) {
		return board[x][y];
	}
	
	private boolean checkNumInARow(
			char player,
			int x,
			int y,
			int dx,
			int dy,
			int num) {
		int count = 0;
		for (int magnitude = -num + 1; magnitude <= num - 1; magnitude++) {
			int currX = x + magnitude * dx;
			int currY = y + magnitude * dy;
			if (isValidPosition(currX, currY)) {
				Character value = board[currX][currY];
				if (value != null && value == player) {
					count++;
					if (count >= num) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		return false;
	}
	
	private boolean isValidPosition(int x, int y) {
		return x >= 0 && x < size && y >= 0 && y < size;
	}
}
