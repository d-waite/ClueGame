// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Player {
	// variables needed to describe player
	private String name;
	private Color color;
	private int row;
	private int column;
	private ArrayList<Card> hand;
	private ArrayList<Card> seen;
	
	public Player(String name, String color, int row, int column) {
		this.name = name;
		this.row = row;
		this.color = stringToColor(color);
		this.column = column;
		hand = new ArrayList<Card>(); // create space for the player to have cards
		seen = new ArrayList<Card>(); // create space for the seen cards
		
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	
	public String getName() {
		return name;
	}

	public Color getColor() {
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
	
	public ArrayList<Card> getSeen() {
		return seen;
	}
	
	public void updateHand(Card card) { // card is dealt to player
		hand.add(card);
	}
	
	public void updateSeen(Card seenCard) {
		seen.add(seenCard);
	}
	
	public Color stringToColor(String colorStr) {
		switch(colorStr) { // all the colors in our ClueSetup.txt
			case "Yellow":
				return Color.yellow;
			case "Blue":
				return Color.blue;
			case "Green":
				return Color.green;
			case "White":
				return Color.white;
			case "Gray":
				return Color.gray;
			case "Magenta":
				return Color.magenta;
			default:
				return Color.black;
				
		}
	}
	
	// for testing purposes
	// we need to clear hand when testing new deals since there is only one instance of Board & deal() would normally
	// be only called once
	public void clearHand() {
		hand = new ArrayList<Card>(); // reallocating space on a new deal
	}
	
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> matches = new ArrayList<Card>();
		//check if any of the cards in the player's hand match the suggestion, and add them to the array list
		if (hand.contains(suggestion.getRoom())) {
			matches.add(suggestion.getRoom());
		}
		if (hand.contains(suggestion.getWeapon())) {
			matches.add(suggestion.getWeapon());
		}
		if (hand.contains(suggestion.getPerson())) {
			matches.add(suggestion.getPerson());
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

	public void draw(Graphics g, int cellSize, int offsetX, int offsetY) {
		g.setColor(color);
		g.fillOval(column * cellSize + offsetX, row * cellSize + offsetY, cellSize, cellSize);
		g.setColor(Color.black);
		g.drawOval(column * cellSize + offsetX, row * cellSize + offsetY, cellSize, cellSize);
	}
	
}
