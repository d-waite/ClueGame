package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	// variables describing the cell
	private int row;
	private int column;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	
	private Set<BoardCell> adjList = new HashSet<BoardCell>();
	private boolean isRoom;
	private boolean isOccupied;
	
	public BoardCell(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public void addAdjacency(BoardCell cell) {
		adjList.add(cell); // put adjCell in adjList
	}

	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	public void setRoom(boolean room) {
		this.isRoom = room;
	}

	public boolean getRoom() {
		return isRoom;
	}

	public void setOccupied(boolean occupied) {
		this.isOccupied = occupied;
	}

	public boolean getOccupied() {
		return isOccupied;
	}

	public boolean isDoorway() {
		return false;
	}

	public DoorDirection getDoorDirection() {
		return DoorDirection.NONE;
	}

	public boolean isLabel() {
		return false;
	}

	public boolean isRoomCenter() {
		return false;
	}

	public char getSecretPassage() {
		return 0;
	}

	public char getInitial() {
		return 0;
	}
	
}
