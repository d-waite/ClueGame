package clueGame;



import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PopUpDialog extends JDialog {
	private JLabel roomLabel;
	private JLabel personLabel;
	private JLabel weaponLabel;
	private JButton submitButton;
	private JButton cancelButton;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel bottomPanel;
	private JMenuBar personMenu;
	private JMenuBar weaponMenu;
	
	

	public PopUpDialog() {
		roomLabel = new JLabel("Current room");
		personLabel = new JLabel("Person");
		weaponLabel = new JLabel("Weapon");
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(3, 1));
		rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(3, 1));
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		
		leftPanel.add(roomLabel);
		leftPanel.add(personLabel);
		leftPanel.add(weaponLabel);
		
		bottomPanel.add(submitButton);
		bottomPanel.add(cancelButton);
		
		add(leftPanel, BorderLayout.WEST);
		add(bottomPanel, BorderLayout.SOUTH);

		
	}
	
	public static void main(String[] args) {
		PopUpDialog test = new PopUpDialog();
		test.setSize(300, 200);
		test.setTitle("Make a Suggestion");
		test.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		test.setVisible(true);
	}
}
