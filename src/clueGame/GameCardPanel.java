// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameCardPanel extends JPanel {
	//static because there will only be one instance because there will only be one board
	private static JPanel peopleCards;
	private static JPanel roomCards;
	private static JPanel weaponCards;
	
	public GameCardPanel() {
		//set up GameCardPanel size and border
		setLayout(new GridLayout(3,1));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		
		//initialie people, room, and weapon panels
		peopleCards = createCardPanel("People");
		roomCards = createCardPanel("Rooms");
		weaponCards = createCardPanel("Weapons");
		
		//add people, room, and weapon panels to GameCardPanel
		add(peopleCards);
		add(roomCards);
		add(weaponCards);
	}
	
	public JPanel createCardPanel( String name) {
		//create a new JPanel with name as the border
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), name));
		panel.setLayout(new GridLayout(0,1));
		panel.add(createInHandLabel());
		panel.add(createNoneField());
		panel.add(createSeenLabel());
		panel.add(createNoneField());
		return panel;
	}
	
	public JTextField createNoneField() {
		//Default for when there are no cards
		JTextField none = new JTextField("none");
		none.setEditable(false);
		return none;
	}
	
	public JTextField newKnownCard(Card card) {
		//make a new card that isn't editable and has the same color as the player that owns it
		JTextField knownCard = new JTextField(card.getCardName());
		knownCard.setEditable(false);
		knownCard.setBackground(card.getPlayerColor());
		return knownCard;
	}
	
	public JLabel createInHandLabel() {
		return new JLabel("In Hand:");
	}
	
	public JLabel createSeenLabel() {
		return new JLabel("Seen:");
	}
	
	public void updateCardLabels(ArrayList<Card> seenList, ArrayList<Card> hand, JPanel panel) {
		//set the card type based on what panel we are changing
		CardType cardType;
		if (panel.equals(peopleCards)) {
			cardType = CardType.PERSON;
		} else if (panel.equals(roomCards)) {
			cardType = CardType.ROOM;
		} else {
			cardType = CardType.WEAPON;
		}
		
		//clear all of the previous contents
		panel.removeAll();
		//rebuild the hand card labels
		panel.add(createInHandLabel());
		updatePanelList(hand, cardType, panel);
		//rebuild the seen card labels
		panel.add(createSeenLabel());
		updatePanelList(seenList, cardType, panel);
	}

	
	public void updatePanelList(ArrayList<Card> list,CardType cardType, JPanel panel) {
		//loop through the cards and add the cards of the correct type into the panel
		for (Card card: list) {
			if (card.getCardType().equals(cardType)) {
				panel.add(newKnownCard(card));
			}
		}
	}
	
	public static void main(String[] args) {
		//for testing
		ArrayList<Card> hand = new ArrayList<Card>();
		ArrayList<Card> seen = new ArrayList<Card>();
		Card A = new Card("A", CardType.PERSON);
		A.setPlayerColor(Color.white);
		hand.add(A);
		hand.add(new Card("C", CardType.ROOM));
		hand.add(new Card("E", CardType.WEAPON));
		Card B = new Card("B", CardType.PERSON);
		B.setPlayerColor(Color.magenta);
		seen.add(B);
		Card D = new Card("D", CardType.ROOM);
		D.setPlayerColor(Color.blue);
		seen.add(D);
		Card F = new Card("F", CardType.WEAPON);
		F.setPlayerColor(Color.magenta);
		seen.add(F);
		Card G = new Card("G", CardType.WEAPON);
		G.setPlayerColor(Color.red);
		seen.add(G);
		GameCardPanel cardDisplay = new GameCardPanel();
		JFrame clueFrame = new JFrame();
		clueFrame.setContentPane(cardDisplay);
		clueFrame.setSize(180,750);
		clueFrame.setTitle("Clue--Medieval Theme");
		clueFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clueFrame.setVisible(true);
		cardDisplay.updateCardLabels(seen, hand, getPeopleCards());
		cardDisplay.updateCardLabels(seen, hand, getRoomCards());
		cardDisplay.updateCardLabels(seen, hand, getWeaponCards());
	}

	public static JPanel getPeopleCards() {
		return peopleCards;
	}

	public static JPanel getRoomCards() {
		return roomCards;
	}

	public static JPanel getWeaponCards() {
		return weaponCards;
	}
}
