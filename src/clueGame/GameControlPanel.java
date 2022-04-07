package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	private JTextField displayTurn;
	private JTextField displayRoll;
	private JTextField displayGuess;
	private JTextField displayGuessResult;
	
	public GameControlPanel() {
		setLayout(new GridLayout(2, 0));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,4));
		JLabel rollLabel = new JLabel("Roll:");
		displayRoll = new JTextField(10);
		displayRoll.setEditable(false);
		JPanel rollPanel = new JPanel();
		JPanel turnPanel = new JPanel();
		rollPanel.setLayout(new GridLayout(2,1));
		turnPanel.setLayout(new GridLayout(2,1));
		JLabel turnLabel = new JLabel("Whose turn?");
		displayTurn = new JTextField(10);
		displayTurn.setEditable(false);
		turnPanel.add(turnLabel);
		turnPanel.add(displayTurn);
		rollPanel.add(rollLabel);
		rollPanel.add(displayRoll);
		buttonPanel.add(turnPanel);
		buttonPanel.add(rollPanel);
		JButton nextTurnButton = new JButton("Next Turn");
		buttonPanel.add(nextTurnButton);
		JButton accusationButton = new JButton("Make Accusation");
		buttonPanel.add(accusationButton);
		add(buttonPanel);
		
		JPanel guessPanel = new JPanel();
		guessPanel.setLayout(new GridLayout(0,2));
		JPanel userGuessPanel = new JPanel();
		userGuessPanel.setLayout(new GridLayout(1, 0));
		userGuessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		JPanel guessResultPanel = new JPanel();
		guessResultPanel.setLayout(new GridLayout(1,0));
		guessResultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		displayGuess = new JTextField("No Guess");
		displayGuess.setEditable(false);
		userGuessPanel.add(displayGuess);
		displayGuessResult = new JTextField("No Result");
		displayGuessResult.setEditable(false);
		guessResultPanel.add(displayGuessResult);
		guessPanel.add(userGuessPanel);
		guessPanel.add(guessResultPanel);
		add(guessPanel);
		
	}
	
	public static void main(String[] args) {
		GameControlPanel clueDisplay = new GameControlPanel();
		JFrame clueFrame = new JFrame();
		clueFrame.setContentPane(clueDisplay);
		clueFrame.setSize(750,180);
		clueFrame.setTitle("Clue--Medieval Theme");
		clueFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clueFrame.setVisible(true);
		
		//testing the setters
		clueDisplay.setTurn(new ComputerPlayer("A", "Green", 0, 0));
		clueDisplay.setGuess("You");
		clueDisplay.setRoll(3);
		clueDisplay.setGuessResult("you're wrong");
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

	public void setGuessResult(String guessResult) {
		this.displayGuessResult.setText(guessResult);
	}
	
	
}
