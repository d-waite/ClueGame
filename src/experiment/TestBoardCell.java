package experiment;
import java.util.*;

public class TestBoardCell {
	private int row;
	private int column;
	public TestBoardCell(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public void addAdjacency(TestBoardCell cell) {
		
	}
	
	public Set<TestBoardCell> getAdjList() {
		Set<TestBoardCell> empty = new HashSet<TestBoardCell>();
		return empty;
	}
	
	public void setRoom(boolean room) {
		
	}
	
//	public boolean isRoom() {
//		
//	}
	
	public void setOccupied(boolean occupied) {
		
	}
	
//	public boolean getOccupied() {
//		
//	}
}
