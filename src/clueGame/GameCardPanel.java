package clueGame;

import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameCardPanel extends JPanel {

	public GameCardPanel() {
		setLayout(new GridLayout(3,1));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		JPanel peopleCards = new JPanel();
		peopleCards.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		peopleCards.setLayout(new GridLayout(0,1));
		peopleCards.add(createInHandLabel());
		peopleCards.add(createNoneField());
		peopleCards.add(createSeenLabel());
		peopleCards.add(createNoneField());
		JPanel roomCards = new JPanel();
		roomCards.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		roomCards.setLayout(new GridLayout(0,1));
		roomCards.add(createInHandLabel());
		roomCards.add(createNoneField());
		roomCards.add(createSeenLabel());
		roomCards.add(createNoneField());
		JPanel weaponCards = new JPanel();
		weaponCards.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		weaponCards.setLayout(new GridLayout(0,1));
		weaponCards.add(createInHandLabel());
		weaponCards.add(createNoneField());
		weaponCards.add(createSeenLabel());
		weaponCards.add(createNoneField());
		add(peopleCards);
		add(roomCards);
		add(weaponCards);
	}
	
	public JTextField createNoneField() {
		JTextField none = new JTextField("none");
		none.setEditable(false);
		return none;
	}
	
	public JTextField newKnownCard(Card card) {
		JTextField knownCard = new JTextField(card.getCardName());
		return knownCard;
	}
	
	public JLabel createInHandLabel() {
		return new JLabel("In Hand:");
	}
	
	public JLabel createSeenLabel() {
		return new JLabel("Seen:");
	}
	
	public static void main(String[] args) {
		GameCardPanel cardDisplay = new GameCardPanel();
		JFrame clueFrame = new JFrame();
		clueFrame.setContentPane(cardDisplay);
		clueFrame.setSize(180,750);
		clueFrame.setTitle("Clue--Medieval Theme");
		clueFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clueFrame.setVisible(true);
	}
}
