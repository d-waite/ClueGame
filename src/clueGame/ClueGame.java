// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	
	public ClueGame(Board board, GameCardPanel cardPanel, GameControlPanel controlPanel) {
		add(cardPanel, BorderLayout.EAST); // cards on the right
		add(controlPanel, BorderLayout.SOUTH); // roll, button, who's turn it is, etc. at the bottom
		add(board, BorderLayout.CENTER); // board in the top left
	}

	public static void main(String[] args) {
		// get the board and panels and set it up so that grid is made
		Board board = Board.getInstance();
		GameCardPanel cardPanel = new GameCardPanel();
		GameControlPanel controlPanel = new GameControlPanel();
		board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
		board.initialize();
		cardPanel.updateCardLabels(board.getHumanPlayer().getSeen(), board.getHumanPlayer().getHand(), cardPanel.getPeopleCards());
		cardPanel.updateCardLabels(board.getHumanPlayer().getSeen(), board.getHumanPlayer().getHand(), cardPanel.getRoomCards());
		cardPanel.updateCardLabels(board.getHumanPlayer().getSeen(), board.getHumanPlayer().getHand(), cardPanel.getWeaponCards());
		ClueGame game = new ClueGame(board,cardPanel,controlPanel); // pass in board and panels to create GUI
		// setting up JFrame
		game.setSize(750,750); // big enough size to start so you can see everything
		game.setTitle("Clue--Medieval Theme");
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
		JOptionPane.showMessageDialog(game, "Find who commited the crime before the other players!", "Welcome to Clue",  JOptionPane.PLAIN_MESSAGE);
		// executing first player's turn
		board.setUpTurn();
		controlPanel.setRoll(board.getRoll());
		controlPanel.setTurn(board.getWhoseTurn());
		board.processTurn();
	}
}
