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
		game.setSize(750,750);
		game.setTitle("Clue--Medieval Theme");
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
	}
}
