// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private static ClueGame game;
	private static Board board;
	private static GameControlPanel controlPanel;
	private static GameCardPanel cardPanel;
	
	
	public ClueGame() {
		add(cardPanel, BorderLayout.EAST); // cards on the right
		add(controlPanel, BorderLayout.SOUTH); // roll, button, who's turn it is, etc. at the bottom
		add(board, BorderLayout.CENTER); // board in the top left
	}
	
	public static void updateCardPanel() {
		cardPanel.updateCardLabels(board.getHumanPlayer().getSeen(), board.getHumanPlayer().getHand(), cardPanel.getPeopleCards());
		cardPanel.updateCardLabels(board.getHumanPlayer().getSeen(), board.getHumanPlayer().getHand(), cardPanel.getRoomCards());
		cardPanel.updateCardLabels(board.getHumanPlayer().getSeen(), board.getHumanPlayer().getHand(), cardPanel.getWeaponCards());
	}
	
	public static GameControlPanel getControlPanel() {
		return controlPanel;
	}
	

	public static void main(String[] args) {
		// get the board and panels and set it up so that grid is made
		board = Board.getInstance();
		cardPanel = new GameCardPanel();
		controlPanel = new GameControlPanel();
		board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
		board.initialize();
		System.out.println(board.getSolution().getPerson().getCardName());
		System.out.println(board.getSolution().getRoom().getCardName());
		System.out.println(board.getSolution().getWeapon().getCardName());
		game = new ClueGame();
		ClueGame.updateCardPanel();
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
	
	public static void ClueGameResult(boolean result) {
		if (result) {
			JOptionPane.showMessageDialog(game, "You Won!\nIt was " + board.getSolution().getPerson().getCardName() + " in the " + board.getSolution().getRoom().getCardName() + " with the " + board.getSolution().getWeapon().getCardName(), "Game Result",  JOptionPane.PLAIN_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(game, "You Lost!\nIt was " + board.getSolution().getPerson().getCardName() + " in the " + board.getSolution().getRoom().getCardName() + " with the " + board.getSolution().getWeapon().getCardName(), "Game Result",  JOptionPane.PLAIN_MESSAGE);
		}
		game.setVisible(false);
	}

	public static ClueGame getGame() {
		return game;
	}
}
