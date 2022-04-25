// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AccusationDialog extends PopUpDialog {
	private JComboBox<String> personMenu;
	private JComboBox<String> weaponMenu;
	private JComboBox<String> roomMenu;
	private JButton submitButton;
	private JButton cancelButton;

	public AccusationDialog() {
		super();
		this.personMenu = super.getPersonMenu();
		this.weaponMenu = super.getWeaponMenu();
		Board board = Board.getInstance();
		JPanel menuPanel = super.getRightPanel();
		roomMenu = new JComboBox<String>();
		for (Room room: board.getAllRooms().values()) {
			roomMenu.addItem(room.getName());
		}
		JPanel buttonPanel = super.getBottomPanel();
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		
		submitButton.addActionListener(new SubmitListener());
		cancelButton.addActionListener(new CancelListener());
		
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		menuPanel.add(roomMenu);
		JComboBox<String> personMenu = super.getPersonMenu();
		menuPanel.add(personMenu);
		JComboBox<String> weaponMenu = super.getWeaponMenu();
		menuPanel.add(weaponMenu);
		add(menuPanel);
		
		setSize(300, 200);
		setTitle("Make an Accusation");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv","ClueSetup.txt");
		board.initialize();
		AccusationDialog accusation = new AccusationDialog();
		accusation.setSize(300, 200);
		accusation.setTitle("Make an Accusation");
		accusation.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		accusation.setVisible(true);
	}
	
	private class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Board board = Board.getInstance();
			Card roomCard = new Card((String)roomMenu.getSelectedItem(), CardType.ROOM);
			Card personCard = new Card((String)personMenu.getSelectedItem(), CardType.PERSON);
			Card weaponCard = new Card((String)weaponMenu.getSelectedItem(), CardType.WEAPON);
			Solution accusation = new Solution(roomCard, weaponCard, personCard);
			boolean reslut = board.checkAccusation(accusation);
			setVisible(false);
			ClueGame.ClueGameResult(reslut);
		}
		
	}
	
	private class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
		
	}
}
