// Authors: David Waite & Dillinger Day
package clueGame;

public class Player {
	private String name;
	private String color;
	private int row;
	private int column;
	
	public Player(String name, String color, int row, int column) {
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
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
}
