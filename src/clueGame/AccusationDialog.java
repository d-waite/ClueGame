// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class AccusationDialog extends PopUpDialog {

	public AccusationDialog() {
		Board board = Board.getInstance();
		JPanel menuPanel = super.getRightPanel();
		JComboBox<String> roomMenu = new JComboBox<String>();
		for (Room room: board.getAllRooms().values()) {
			roomMenu.addItem(room.getName());
		}
		menuPanel.add(roomMenu);
		JComboBox<String> personMenu = super.getPersonMenu();
		menuPanel.add(personMenu);
		JComboBox<String> weaponMenu = super.getWeaponMenu();
		menuPanel.add(weaponMenu);
		add(menuPanel);
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
			
		}
		
	}
}
