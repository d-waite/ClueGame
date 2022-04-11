package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class ClueGame extends JFrame {
	
	public ClueGame(Board board) {
		add(new GameCardPanel(), BorderLayout.EAST);
		add(new GameControlPanel(), BorderLayout.SOUTH);
		add(board, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
		board.initialize();
		ClueGame game = new ClueGame(board);
		game.setSize(500,500);
		game.setVisible(true);
	}
}
