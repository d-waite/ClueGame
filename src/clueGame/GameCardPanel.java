package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameCardPanel extends JPanel {
	private JPanel peopleCards;
	private JPanel roomCards;
	private JPanel weaponCards;
	
	public GameCardPanel() {
		setLayout(new GridLayout(3,1));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		
		peopleCards = new JPanel();
		peopleCards.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		peopleCards.setLayout(new GridLayout(0,1));
		peopleCards.add(createInHandLabel());
		peopleCards.add(createNoneField());
		peopleCards.add(createSeenLabel());
		peopleCards.add(createNoneField());
		
		roomCards = new JPanel();
		roomCards.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		roomCards.setLayout(new GridLayout(0,1));
		roomCards.add(createInHandLabel());
		roomCards.add(createNoneField());
		roomCards.add(createSeenLabel());
		roomCards.add(createNoneField());
		
		weaponCards = new JPanel();
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
	
	public void updatePeopleCards(ArrayList<Card> seenList, ArrayList<Card> hand) {
		peopleCards.removeAll();
		peopleCards.add(createInHandLabel());
		for (Card card: hand) {
			if (card.getCardType().equals(CardType.PERSON)) {
				peopleCards.add(newKnownCard(card));
			}
		}
		peopleCards.add(createSeenLabel());
		for (Card card: seenList) {
			if (card.getCardType().equals(CardType.PERSON)) {
				peopleCards.add(newKnownCard(card));
			}
		}
	}
	
	public void updateRoomCards(ArrayList<Card> seenList, ArrayList<Card> hand) {
		roomCards.removeAll();
		roomCards.add(createInHandLabel());
		for (Card card: hand) {
			if (card.getCardType().equals(CardType.ROOM)) {
				roomCards.add(newKnownCard(card));
			}
		}
		roomCards.add(createSeenLabel());
		for (Card card: seenList) {
			if (card.getCardType().equals(CardType.ROOM)) {
				roomCards.add(newKnownCard(card));
			}
		}
	}
	
	public void updateWeaponCards(ArrayList<Card> seenList, ArrayList<Card> hand) {
		weaponCards.removeAll();
		weaponCards.add(createInHandLabel());
		for (Card card: hand) {
			if (card.getCardType().equals(CardType.WEAPON)) {
				weaponCards.add(newKnownCard(card));
			}
		}
		weaponCards.add(createSeenLabel());
		for (Card card: seenList) {
			if (card.getCardType().equals(CardType.WEAPON)) {
				weaponCards.add(newKnownCard(card));
			}
		}
	}
	
	public static void main(String[] args) {
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
		cardDisplay.updatePeopleCards(seen, hand);
		cardDisplay.updateRoomCards(seen, hand);
		cardDisplay.updateWeaponCards(seen, hand);
	}
}
