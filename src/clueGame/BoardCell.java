// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {
	// variables describing the cell
	private int row;
	private int column;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private Set<BoardCell> adjList = new HashSet<BoardCell>(); // each cell has its own adjacency list
	private boolean isRoom;
	private boolean isOccupied;
	private boolean isDoorway;
	private boolean isSecretPassage = false; // will only be changed to true if setSecretPassage() is called
	private char secretPassage;
		
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

	public boolean isRoom() {
		return isRoom;
	}

	public void setOccupied(boolean occupied) {
		if (!roomCenter) { // multiple players can be in the same room
			this.isOccupied = occupied;
		}
	}

	public boolean getOccupied() {
		return isOccupied;
	}

	public boolean isDoorway() {
		return isDoorway;
	}
	
	public void setDoorway(boolean isDoorway) {
		this.isDoorway = isDoorway;
	}
	
	public void setDoorDirection(DoorDirection direction) {
		this.doorDirection = direction;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public void setLabel(boolean label) {
		this.roomLabel = label;
	}
	
	public void setRoomCenter(boolean center) {
		this.roomCenter = center;
	}

	public boolean isLabel() {
		return roomLabel;
	}

	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public void setSecretPassage(char secretPassage) {
		this.isSecretPassage = true; 
		this.secretPassage = secretPassage;
	}

	public char getSecretPassage() {
		return secretPassage;
	}
	
	public boolean isSecretPassage() {
		return isSecretPassage;
	}

	public char getInitial() {
		return initial;
	}
	
	public void setInitial(char initial) {
		this.initial = initial;
	}

	public void draw(Graphics g, int x, int y, int cellSize) {
		if (initial != 'X') {
			g.setColor(Color.yellow);
			g.fillRect(x, y, cellSize, cellSize);
			g.setColor(Color.black);
			g.drawRect(x, y, cellSize, cellSize);
		}
	}
	
public void drawRoomName(Graphics g, String roomName, int fontSize, int x, int y) {
		g.setFont(new Font("Serif", Font.PLAIN, fontSize));
		g.setColor(Color.blue);
		g.drawString(roomName, x, y);
	}

public void drawRoom(Graphics g, int x, int y, int cellSize) {
		g.setColor(Color.gray);
		g.fillRect(x, y, cellSize, cellSize);
		g.setColor(Color.gray);
		g.drawRect(x, y, cellSize, cellSize);
	}

public int getRow() {
	return row;
}

public int getColumn() {
	return column;
}
}
