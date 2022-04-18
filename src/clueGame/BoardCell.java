// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	// variables describing the cell
	private int row;
	private int column;
	private int x;
	private int y;
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
		x = 0;
		y = 0;
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

	public boolean isOccupied() {
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
		this.x = x;
		this.y = y;
		if (initial != 'X') { // only draw if we are a walkway; unused spaces are skipped, leaving the black background visible
			// draw a yellow rectangle with a black border (to separate it from other cells) at position (x,y) of size cellSize x cellSize
			g.setColor(new Color(0, 76, 153));
			g.fillRect(x, y, cellSize, cellSize); 
			g.setColor(Color.black);
			g.drawRect(x, y, cellSize, cellSize);
		}
	}

	public void drawRoomName(Graphics g, String roomName, int fontSize, int x, int y) {
		g.setFont(new Font("SansSerif", Font.BOLD, fontSize));
		g.setColor(Color.CYAN); // cyan contrasts really well with the grey of the rooms :)
		g.drawString(roomName, x, y);
	}

	public void drawRoom(Graphics g, int x, int y, int cellSize) { 
		this.x = x;
		this.y = y;
		// no borders to separate room cells, since we do not move around in room
		g.setColor(Color.gray);
		g.fillRect(x, y, cellSize, cellSize);
		g.setColor(Color.gray);
		g.drawRect(x, y, cellSize, cellSize);
	}
	
	public void drawDoor(Graphics g, int x, int y, int cellSize) {
		g.setColor(Color.CYAN);
		int partOfCell = cellSize / 6; // doors will be drawn on cell that door opens into; using 1/6 of the cell size was good enough to show a door
		switch (doorDirection) {
		case DOWN:
			y += cellSize; // draw door on cell below
			g.fillRect(x, y, cellSize, partOfCell);
			break;
		case UP:
			y -= partOfCell; // draw door on cell above
			g.fillRect(x, y, cellSize, partOfCell);
			break;
		case LEFT:
			x -= partOfCell; // draw door on cell to the left
			g.fillRect(x, y, partOfCell, cellSize);
			break;
		case RIGHT:
			x += cellSize; // draw door on cell to the right
			g.fillRect(x, y, partOfCell, cellSize);
			break;
		default:
			break;
		}
	}
	
	public void drawTarget(Graphics g, int x, int y, int cellSize) {
		g.setColor(new Color(204, 30, 102));
		g.fillRect(x, y, cellSize, cellSize);
		if (!isRoom) {
			g.setColor(Color.black);
			g.drawRect(x, y, cellSize, cellSize);
		}
	}


	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	//coordinates on screen
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
