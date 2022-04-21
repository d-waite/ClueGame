package clueGame;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SuggestionDialog extends PopUpDialog {
	private JTextField roomTextField;	

	

	public SuggestionDialog(Player player) {
		super();
		Board board = Board.getInstance();
		roomTextField = new JTextField((board.getRoom(board.getCell(player.getRow(), player.getColumn()))).getName());
		roomTextField.setEditable(false);
		
		super.getRightPanel().add(roomTextField);
		super.getRightPanel().add(super.getPersonMenu());
		super.getRightPanel().add(super.getWeaponMenu());
		
		add(super.getRightPanel());


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
