// Authors: David Waite and Dillinger Day
package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	private JTextField displayTurn;
	private JTextField displayRoll;
	private JTextField displayGuess;
	private JTextField displayGuessResult;
	private JButton nextTurnButton;
	private JButton accusationButton;
	
	public GameControlPanel() {
		setLayout(new GridLayout(2, 0));
		add(createButtonPanel());
		add(createGuessPanel(createUserGuessPanel(), createGuessResultPanel()));
	}
	
	public JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,4));
		buttonPanel.add(createTurnPanel());
		buttonPanel.add(createRollPanel());
		// making buttons on right side
		nextTurnButton = new JButton("Next Turn");
		nextTurnButton.addActionListener(new nextButtonListener());
		buttonPanel.add(nextTurnButton);
		accusationButton = new JButton("Make Accusation");
		buttonPanel.add(accusationButton);
		return buttonPanel;
	}
	
	public JPanel createTurnPanel() { // creates turn label and displays what player's turn it is
		JPanel turnPanel = new JPanel();
		turnPanel.setLayout(new GridLayout(2,1));
		turnPanel.add(new JLabel("Whose turn?"));
		displayTurn = new JTextField();
		displayTurn.setEditable(false);
		turnPanel.add(displayTurn);
		return turnPanel;
	}
	
	public JPanel createRollPanel() { // creates roll label and displays the roll for that person
		JPanel rollPanel = new JPanel();
		rollPanel.setLayout(new GridLayout(2,1));
		rollPanel.add(new JLabel("Roll:"));
		displayRoll = new JTextField();
		displayRoll.setEditable(false);
		rollPanel.add(displayRoll);
		return rollPanel;
	}
	
	public JPanel createUserGuessPanel() { // creates place to display the player's guess
		JPanel userGuessPanel = new JPanel();
		userGuessPanel.setLayout(new GridLayout(1, 0));
		userGuessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		displayGuess = new JTextField("No Guess");
		displayGuess.setEditable(false);
		userGuessPanel.add(displayGuess);
		return userGuessPanel;
	}
	
	public JPanel createGuessResultPanel() { // creates place to display the result, i.e. was it disproven
		JPanel guessResultPanel = new JPanel();
		guessResultPanel.setLayout(new GridLayout(1,0));
		guessResultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		displayGuessResult = new JTextField("No Result");
		displayGuessResult.setEditable(false);
		guessResultPanel.add(displayGuessResult);
		return guessResultPanel;
	}
	
	public JPanel createGuessPanel(JPanel userGuessPanel, JPanel guessResultPanel) { // puts the two subpanels together
		JPanel guessPanel = new JPanel();
		guessPanel.setLayout(new GridLayout(0,2));
		guessPanel.add(userGuessPanel);
		guessPanel.add(guessResultPanel);
		return guessPanel;
	}
	
	public static void main(String[] args) {
		// creating our bottom panel for the game
		GameControlPanel controlDisplay = new GameControlPanel();
		JFrame clueFrame = new JFrame();
		clueFrame.setContentPane(controlDisplay);
		clueFrame.setSize(750,180);
		clueFrame.setTitle("Clue--Medieval Theme");
		clueFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clueFrame.setVisible(true);
		
		//testing the setters
		controlDisplay.setTurn(new ComputerPlayer("A", "Green", 0, 0));
		controlDisplay.setGuess("You");
		controlDisplay.setRoll(3);
		controlDisplay.setGuessResult("you're wrong", Color.blue);
	}

	public void setTurn(Player player) {
		this.displayTurn.setText(player.getName());
		this.displayTurn.setBackground(player.getColor());
	}

	public void setRoll(int roll) {
		this.displayRoll.setText(Integer.toString(roll));
	}

	public void setGuess(String guess) {
		this.displayGuess.setText(guess);
	}

	public void setGuessResult(String guessResult, Color color) {
		this.displayGuessResult.setText(guessResult);
		this.displayGuessResult.setBackground(color);
	}
	
	private class nextButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Board board = Board.getInstance();
			if (!board.isHumanPlayerFinished()) {
				JOptionPane.showMessageDialog(board, "You haven't finished your turn yet!");
			} else {
				board.next();
				board.setUpTurn();
				setRoll(board.getRoll());
				setTurn(board.getWhoseTurn());
				board.processTurn();
				// future code
//				Solution guess = board.getGuess();
//				if (guess != null) {
//					setGuess(board.getGuess().getPerson().getCardName() + " in the " + board.getGuess().getRoom().getCardName() + " with the " + board.getGuess().getWeapon().getCardName());
//				} else {
//					setGuess("No Guess");
//				}
//				Player whoDisproved = board.getWhoDisproved();
//				if (whoDisproved != null) {
//					setGuessResult("Guess was disproven.",whoDisproved.getColor());
//				} else if (guess != null){
//					setGuessResult("Guess was not disproven.",Color.white);
//				} else {
//					setGuessResult("No Result",Color.white);
//				}
			}
		}
		
	}
}
