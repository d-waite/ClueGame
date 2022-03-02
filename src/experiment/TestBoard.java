package experiment;

import java.util.*;

public class TestBoard {
	private TestBoardCell[][] grid = new TestBoardCell[ROWS][COLS];
	private Set<TestBoardCell> targets = new HashSet<TestBoardCell>();
	private Set<TestBoardCell> visited  = new HashSet<TestBoardCell>();
	final static int COLS = 4;
	final static int ROWS = 4;

	public TestBoard() { // creating a 4x4 board
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				TestBoardCell cell = new TestBoardCell(i, j);
				this.grid[i][j] = cell;
			}
		}

		createAdjacencyList(grid); // make adjacency list for every cell
	}

	public void calcTargets(TestBoardCell startCell, int pathlength) {
		visited.clear(); // start with empty lists
		targets.clear();
		visited.add(startCell); // if we started on it, we visited it
		findAllTargets(startCell, pathlength);
	}

	private void findAllTargets(TestBoardCell thisCell, int pathlength) {
		for (TestBoardCell adjCell: thisCell.getAdjList()) {
			if ((!(visited.contains(adjCell))) && (!(adjCell.getOccupied()))) { // if cell has not been visited and isn't occupied, visit it
				visited.add(adjCell); 
				if (adjCell.getRoom()) { // if cell is a room, it is a target
					targets.add(adjCell);
				} else if (pathlength == 1) { // if turn is over after we visit, the cell is a target
					targets.add(adjCell);
				} else {
					findAllTargets(adjCell, pathlength - 1); // else, still have more movement to do
				}
				visited.remove(adjCell); // so we can go through it in a different path
			}
		}
	}

	public Set<TestBoardCell> getTargets() {
		return targets;
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
					if (j == 0) { // at top left, so we can only go down or right
						grid[i][j].addAdjacency(grid[i+1][j]);
						grid[i][j].addAdjacency(grid[i][j+1]);
					} else if (j == COLS - 1) { // at top right, so we can only go down or left
						grid[i][j].addAdjacency(grid[i+1][j]);
						grid[i][j].addAdjacency(grid[i][j-1]);
					} else { // at top edge, so we can only go down,left, or right
						grid[i][j].addAdjacency(grid[i+1][j]);
						grid[i][j].addAdjacency(grid[i][j+1]);
						grid[i][j].addAdjacency(grid[i][j-1]);
					}
				} else if (i == ROWS -1) {
					if (j == 0) { // // at bottom left, so we can only go up or right
						grid[i][j].addAdjacency(grid[i-1][j]);
						grid[i][j].addAdjacency(grid[i][j+1]);
					} else if (j == COLS - 1) { // at bottom right, so we can only go up or left
						grid[i][j].addAdjacency(grid[i-1][j]);
						grid[i][j].addAdjacency(grid[i][j-1]);
					} else { // at bottom edge, so we can only go up, right, and left
						grid[i][j].addAdjacency(grid[i-1][j]);
						grid[i][j].addAdjacency(grid[i][j+1]);
						grid[i][j].addAdjacency(grid[i][j-1]);
					}
				} else if (j == 0) { // at left edge, so we can only go up, down, or right
					grid[i][j].addAdjacency(grid[i-1][j]);
					grid[i][j].addAdjacency(grid[i+1][j]);
					grid[i][j].addAdjacency(grid[i][j+1]);
				} else if (j == 3) { // at right edge, so we can only go up, down, or left
					grid[i][j].addAdjacency(grid[i-1][j]);
					grid[i][j].addAdjacency(grid[i+1][j]);
					grid[i][j].addAdjacency(grid[i][j-1]);
				} else { // in middle, we have adjacent cells on all 4 sides
					grid[i][j].addAdjacency(grid[i-1][j]);
					grid[i][j].addAdjacency(grid[i+1][j]);
					grid[i][j].addAdjacency(grid[i][j-1]);
					grid[i][j].addAdjacency(grid[i][j+1]);
				}
			}
		}
	}


}
