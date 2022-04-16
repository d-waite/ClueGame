// Authors: David Waite & Dillinger Day
package clueGame;

import java.util.ArrayList;

public class Room {

	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private ArrayList<BoardCell> roomCells;
	
	public Room(String name) {
		super();
		this.name = name;
		this.roomCells = new ArrayList<BoardCell>();
	}

	public String getName() { 
		return name;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	public void addRoomCell(BoardCell cell) {
		roomCells.add(cell);
	}
	
	public ArrayList<BoardCell> getRoomCells() {
		return roomCells;
	}

}
