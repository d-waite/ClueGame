// Authors: David Waite & Dillinger Day
package clueGame;



import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PopUpDialog extends JDialog {
	private JLabel roomLabel;
	private JLabel personLabel;
	private JLabel weaponLabel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel bottomPanel;
	private JComboBox<String> personMenu;
	private JComboBox<String> weaponMenu;
	
	

	public PopUpDialog() {
		Board board = Board.getInstance();
		roomLabel = new JLabel("Current room");
		personLabel = new JLabel("Person");
		weaponLabel = new JLabel("Weapon");
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(3, 1));
		rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(3, 1));
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		personMenu = new JComboBox<String>();
		for (Player player: board.getAllPlayers()) {
			personMenu.addItem(player.getName());
		}
		weaponMenu = new JComboBox<String>();
		for (Card weapon: board.getWeapons()) {
			weaponMenu.addItem(weapon.getCardName());
		}
		leftPanel.add(roomLabel);
		leftPanel.add(personLabel);
		leftPanel.add(weaponLabel);
		
		add(leftPanel, BorderLayout.WEST);		
	}
	
	
	
	public static void main(String[] args) {
		PopUpDialog test = new PopUpDialog();
		test.setSize(300, 200);
		test.setTitle("Make a Suggestion");
		test.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		test.setVisible(true);
	}
	
	public JPanel getRightPanel() {
		return rightPanel;
	}
	
	public JPanel getBottomPanel() {
		return bottomPanel;
	}
	
	public JComboBox<String> getPersonMenu() {
		return personMenu;
	}
	
	public JComboBox<String> getWeaponMenu() {
		return weaponMenu;
	}
}
