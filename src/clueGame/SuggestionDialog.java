// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SuggestionDialog extends PopUpDialog {
	private JTextField roomTextField;
	private JComboBox<String> personMenu;
	private JComboBox<String> weaponMenu;

	

	public SuggestionDialog(Player player) {
		super();
		this.personMenu = super.getPersonMenu();
		this.weaponMenu = super.getWeaponMenu();
		Board board = Board.getInstance();
		roomTextField = new JTextField((board.getRoom(board.getCell(player.getRow(), player.getColumn()))).getName());
		roomTextField.setEditable(false);
		
		super.getRightPanel().add(roomTextField);
		super.getRightPanel().add(personMenu);
		super.getRightPanel().add(weaponMenu);
		
		
		
		add(super.getRightPanel());


	}
	
	//MOVE TO POPUPDIALOG
	private class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Card roomCard = new Card(roomTextField.getName(), CardType.ROOM);
			Card personCard = new Card((String)personMenu.getSelectedItem(), CardType.PERSON);
			Card weaponCard = new Card((String)weaponMenu.getSelectedItem(), CardType.WEAPON);
			Solution solution = new Solution(roomCard, personCard, weaponCard);
		}
		
	}
	
	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
		board.initialize();
		SuggestionDialog test = new SuggestionDialog(board.getHumanPlayer());
		test.setSize(300, 200);
		test.setTitle("Make a Suggestion");
		test.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		test.setVisible(true);
	}
}
