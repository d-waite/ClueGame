package clueGame;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SuggestionDialog extends PopUpDialog {
	private JTextField roomTextField;	

	

	public SuggestionDialog(Player player) {
		super();
		roomTextField = new JTextField((board.getRoom(board.getCell(board.getHumanPlayer().getRow(), board.getHumanPlayer().getColumn())).getName()));
		roomTextField.setEditable(false);

		add(roomTextField);


	}
}
