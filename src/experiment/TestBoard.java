package experiment;

import java.util.*;

public class TestBoard {
	private TestBoardCell[][] grid = new TestBoardCell[ROWS][COLS];
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	final static int COLS = 4;
	final static int ROWS = 4;
	
	public TestBoard() {
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				TestBoardCell cell = new TestBoardCell(i, j);
				this.grid[i][j] = cell;
			}
		}
		
		createAdjacencyList(grid);
	}
	
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		
	}
	
	public Set<TestBoardCell> getTargets() {
		Set<TestBoardCell> empty = new HashSet<TestBoardCell>();
		return empty;
	}
	
	public TestBoardCell getCell( int row, int col ) {
		return grid[row][col];
	}
	
	public static void main(String[] args) {
		TestBoard board = new TestBoard();
	}
	
	public void createAdjacencyList( TestBoardCell[][] grid) {
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				if (i == 0) {
					if (j == 0) {
						grid[i][j].addAdjacency(grid[i+1][j]);
						grid[i][j].addAdjacency(grid[i][j+1]);
					} else if (j == COLS - 1) {
						grid[i][j].addAdjacency(grid[i+1][j]);
						grid[i][j].addAdjacency(grid[i][j-1]);
					} else {
						grid[i][j].addAdjacency(grid[i+1][j]);
						grid[i][j].addAdjacency(grid[i][j+1]);
						grid[i][j].addAdjacency(grid[i][j-1]);
					}
				} else if (i == ROWS -1) {
					if (j == 0) {
						grid[i][j].addAdjacency(grid[i-1][j]);
						grid[i][j].addAdjacency(grid[i][j+1]);
					} else if (j == COLS - 1) {
						grid[i][j].addAdjacency(grid[i-1][j]);
						grid[i][j].addAdjacency(grid[i][j-1]);
					} else {
						grid[i][j].addAdjacency(grid[i-1][j]);
						grid[i][j].addAdjacency(grid[i][j+1]);
						grid[i][j].addAdjacency(grid[i][j-1]);
					}
				} else if (j == 0) {
					grid[i][j].addAdjacency(grid[i-1][j]);
					grid[i][j].addAdjacency(grid[i+1][j]);
					grid[i][j].addAdjacency(grid[i][j+1]);
				} else if (j == 3) {
					grid[i][j].addAdjacency(grid[i-1][j]);
					grid[i][j].addAdjacency(grid[i+1][j]);
					grid[i][j].addAdjacency(grid[i][j-1]);
				} else {
					grid[i][j].addAdjacency(grid[i-1][j]);
					grid[i][j].addAdjacency(grid[i+1][j]);
					grid[i][j].addAdjacency(grid[i][j-1]);
					grid[i][j].addAdjacency(grid[i][j+1]);
				}
			}
		}
	}
	
	
}
