package clueGame;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SuggestionDialog extends PopUpDialog {
	private JTextField roomTextField;	

	

	public SuggestionDialog(Player player) {
		super();
		Board board = Board.getInstance();
		roomTextField = new JTextField((board.getRoom(player.getRow(), player.getColumn())).getName());
		roomTextField.setEditable(false);
		
		add(super.getRightPanel());


	}
}
