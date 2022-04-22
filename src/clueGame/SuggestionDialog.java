// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SuggestionDialog extends PopUpDialog {
	private JTextField roomTextField;
	private JButton submitButton;
	private JButton cancelButton;
	private JComboBox<String> personMenu;
	private JComboBox<String> weaponMenu;

	

	public SuggestionDialog(Player player) {
		super();
		this.personMenu = super.getPersonMenu();
		this.weaponMenu = super.getWeaponMenu();
		Board board = Board.getInstance();
		roomTextField = new JTextField((board.getRoom(board.getCell(player.getRow(), player.getColumn()))).getName());
		roomTextField.setEditable(false);
		
		JPanel choicePanel = super.getRightPanel();
		choicePanel.add(roomTextField);
		choicePanel.add(personMenu);
		choicePanel.add(weaponMenu);
		
		JPanel buttonPanel = super.getBottomPanel();
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		
		submitButton.addActionListener(new SubmitListener());
		cancelButton.addActionListener(new CancelListener());
		
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		add(choicePanel, BorderLayout.EAST);
		
		setSize(300, 200);
		setTitle("Make a Suggestion");
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	
	private class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Board board = Board.getInstance();
			Card roomCard = new Card(roomTextField.getName(), CardType.ROOM);
			Card personCard = new Card((String)personMenu.getSelectedItem(), CardType.PERSON);
			Card weaponCard = new Card((String)weaponMenu.getSelectedItem(), CardType.WEAPON);
			Solution suggestion = new Solution(roomCard, personCard, weaponCard);
			setVisible(false);
			board.handleSuggestion(suggestion, board.getHumanPlayer());
		}
		
	}
	
	private class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "You must make a suggestion!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
		board.initialize();
		SuggestionDialog test = new SuggestionDialog(board.getHumanPlayer());
		test.setVisible(true);
	}
}
