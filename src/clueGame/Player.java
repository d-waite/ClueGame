// Authors: David Waite & Dillinger Day
package clueGame;

import java.util.ArrayList;

public class Player {
	// variables needed to describe player
	private String name;
	private String color;
	private int row;
	private int column;
	private ArrayList<Card> hand;
	
	public Player(String name, String color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
		hand = new ArrayList<Card>(); // create space for the player to have cards
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
}
