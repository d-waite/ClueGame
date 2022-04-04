// Authors: David Waite & Dillinger Day
package clueGame;

import java.util.ArrayList;
import java.util.Random;

public class Player {
	// variables needed to describe player
	private String name;
	private String color;
	private int row;
	private int column;
	private ArrayList<Card> hand;
	private ArrayList<Card> seen;
	
	public Player(String name, String color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
		hand = new ArrayList<Card>(); // create space for the player to have cards
		seen = new ArrayList<Card>(); // create space for the seen cards
	}
	
	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public void updateHand(Card card) { // card is dealt to player
		hand.add(card);
	}
	
	// for testing purposes
	// we need to clear hand when testing new deals since there is only one instance of Board & deal() would normally
	// be only called once
	public void clearHand() {
		hand = new ArrayList<Card>(); // reallocating space on a new deal
	}
	
	public Card disproveSuggestion(Card room, Card weapon, Card person) {
		ArrayList<Card> matches = new ArrayList<Card>();
		//check if any of the cards in the player's hand match the suggestion, and add them to the array list
		if (hand.contains(room)) {
			matches.add(room);
		}
		if (hand.contains(weapon)) {
			matches.add(weapon);
		}
		if (hand.contains(person)) {
			matches.add(person);
		}
		//if no cards have been added to the array list, then return null
		if (matches.size() == 0) {
			return null;
		} else { //choose a random card from the array list (if the size is one it will always choose that card)
			Random randInt = new Random();
			int randCard = randInt.nextInt(matches.size());
			return matches.get(randCard);
		}
		
	}
	public void updateSeen(Card seenCard) {
		seen.add(seenCard);
	}
}
