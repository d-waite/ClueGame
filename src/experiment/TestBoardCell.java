// Authors: David Waite & Dillinger Day
package experiment;
import java.util.*;

public class TestBoardCell {
	// variables describing the cell
	private int row;
	private int column;
	private Set<TestBoardCell> adjList = new HashSet<TestBoardCell>();
	private boolean isRoom;
	private boolean isOccupied;
	
	public TestBoardCell(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public void addAdjacency(TestBoardCell cell) {
		adjList.add(cell); // put adjCell in adjList
	}

	public Set<TestBoardCell> getAdjList() {
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


}
