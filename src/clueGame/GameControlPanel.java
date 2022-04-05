package clueGame;

import java.awt.BorderLayout;

import javax.swing.*;

public class GameControlPanel extends JPanel {
	
	public GameControlPanel() {
		JPanel buttonPanel = new JPanel();
		JLabel rollLabel = new JLabel("Roll:");
		JTextField displayRoll = new JTextField(10);
		displayRoll.setEditable(false);
		JPanel rollPanel = new JPanel();
		rollPanel.add(rollLabel);
		rollPanel.add(displayRoll);
		buttonPanel.add(rollPanel);
		JButton nextTurnButton = new JButton("Next Turn");
		buttonPanel.add(nextTurnButton);
		JButton accusationButton = new JButton("Make Accusation");
		buttonPanel.add(accusationButton);
		add(buttonPanel);
		
	}
	
	public static void main(String[] args) {
		GameControlPanel clueDisplay = new GameControlPanel();
		JFrame clueFrame = new JFrame();
		clueFrame.setContentPane(clueDisplay);
		clueFrame.setSize(750,180);
		clueFrame.setTitle("Clue--Medieval Theme");
		clueFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clueFrame.setVisible(true);
	}
}
