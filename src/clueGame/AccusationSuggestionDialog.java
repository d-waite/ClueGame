package clueGame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AccusationSuggestionDialog extends JDialog {
	private JLabel roomLabel;
	private JLabel personLabel;
	private JLabel weaponLabel;
	private JButton submitButton;
	private JButton cancelButton;
	private JTextField roomTextField;
	
	public AccusationSuggestionDialog() {
		roomLabel = new JLabel("Current room");
		personLabel = new JLabel("Person");
		weaponLabel = new JLabel("Weapon");
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
	}
}
