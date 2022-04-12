// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	
	public ClueGame(Board board) {
		add(new GameCardPanel(), BorderLayout.EAST); // cards on the right
		add(new GameControlPanel(), BorderLayout.SOUTH); // roll, button, who's turn it is, etc. at the bottom
		add(board, BorderLayout.CENTER); // board in the top left
	}

	public static void main(String[] args) {
		// get the board and set it up so that grid is made
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
		board.initialize(); 
		ClueGame game = new ClueGame(board); // pass in board as opposed to getting the instance again in constructor
		// setting up JFrame
		game.setSize(750,750); // big enough size to start so you can see everything
		game.setTitle("Clue--Medieval Theme");
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
		JOptionPane.showMessageDialog(game, "Find who commited the crime before the other players!");
		
	}
}
