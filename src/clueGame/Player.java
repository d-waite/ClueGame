// Authors: David Waite & Dillinger Day
package clueGame;

import java.util.ArrayList;

public class Player {
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
		hand = new ArrayList<Card>();
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
}
