package clueGame;

import java.util.*;
import java.io.*;

public class Board {
	private String setupConfigFile;
	private String layoutConfigFile;
	private int numRows;
	private int numCols;
	private static Board theInstance = new Board();
	private BoardCell[][] grid;
	private Map<Character, Room> rooms;

	private Board() {
		super();
	}

	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {
		try {
			loadSetupConfig(null);
			loadLayoutConfig(null);
		}
		catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}
		catch (FileNotFoundException e) {
			System.out.println("Error opening file.");
		}
	}

	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}

	public BoardCell getCell(int i, int j) {
		return grid[i][j];
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numCols;
	}

	public Room getRoom(char c) {
		return rooms.get(c);
	}

	public Room getRoom(BoardCell cell) {
		return rooms.get(cell.getInitial());
	}

	public void loadSetupConfig(Object obj) throws FileNotFoundException, BadConfigFormatException {
		//Open the file
		FileReader input = new FileReader("data/" + setupConfigFile);
		Scanner scan = new Scanner(input);
		//initialize room map
		rooms = new HashMap<Character, Room>();
		//Loop while the scanner has a next line, 
		while (scan.hasNextLine()) {
			String roomLine = scan.nextLine();
			//Skip the line if the first character is a slash
			if (roomLine.charAt(0) == '/') {
				roomLine = scan.nextLine();
			}
			//Split the line with the commas on the delimiters
			String[] roomInfo = new String[3];
			roomInfo = roomLine.split(", ");
			//If the first word in the line isn't Space or Room throw an Exception
			if (!(roomInfo[0].equals("Room")) && !(roomInfo[0].equals("Space"))) {
				throw new BadConfigFormatException("Bad room type in setup file.");
			}
			//if the initial for the cell is longer than 2 characters long, throw an Exception
			if (roomInfo[2].length() > 2) {
				throw new BadConfigFormatException("Bad initial in setup file.");
			}
			// Create a new room and insert it into the map
			String roomName = roomInfo[1];
			Room room = new Room(roomName);
			char roomInitial = roomInfo[2].charAt(0);
			rooms.put(roomInitial, room);
		}
		scan.close();
	}

	public void loadLayoutConfig(Object obj) throws BadConfigFormatException, FileNotFoundException{
		//Open the file
		FileReader inputSize = new FileReader("data/" + layoutConfigFile);
		Scanner scanSize = new Scanner(inputSize);
		String rowCells = "";
		numRows = 0;
		numCols = 0;
		//Get the first line and set the length to numCols
		if (scanSize.hasNextLine()) {
			numRows++;
			rowCells = scanSize.nextLine();
			numCols = rowCells.split(",").length;

		}
		//Increment numRows each loop and check if the column sizes are consistent, if not throw an Exception
		while (scanSize.hasNextLine()) {
			numRows++;
			rowCells = scanSize.nextLine();
			if ((rowCells.split(",").length) != (numCols)) {
				throw new BadConfigFormatException("Layout file did not have the same number of columns per row.");
			}

		}
		grid = new BoardCell[numRows][numCols];

		FileReader inputCells = new FileReader("data/" + layoutConfigFile);
		Scanner scanCells = new Scanner(inputCells);
		//Loop through the file again
		while (scanCells.hasNextLine()) {
			for (int i = 0; i < numRows; i++) {
				//split the row along the commas
				String row = scanCells.nextLine();
				String[] seperatedRow = new String[numCols];
				seperatedRow = row.split(",");
				//make all of the cells in the row into BoardCells and add them to the grid
				for (int j = 0; j < numCols; j++) {
					BoardCell cell = new BoardCell(i,j);
					initializeCell(cell, seperatedRow[j]);
					grid[i][j] = cell;
				}
			}
		}
		scanSize.close();
		scanCells.close();
	}

	//helper function for initializing BoardCells
	public void initializeCell(BoardCell cell, String label) throws BadConfigFormatException{
		cell.setInitial(label.charAt(0));
		//Throw an exception if the initial doesn't match our known room initials
		if (!(rooms.containsKey(cell.getInitial()))){
			throw new BadConfigFormatException("Room reference does not exist.");
		}
		//Initialization for cells that are a walkway or an unused space
		if (cell.getInitial() == 'W' || cell.getInitial() == 'X') {
			cell.setRoom(false);
			cell.setLabel(false);
			cell.setRoomCenter(false);
			//If not, the cell is a room
		} else {
			cell.setRoom(true);
			cell.setDoorway(false);
			cell.setDoorDirection(DoorDirection.NONE);
			cell.setRoomCenter(false);
			cell.setLabel(false);
		}
		//This checks the second symbol and updates the cell attributes accordingly
		if (label.length() == 2) {
			char symbol = label.charAt(1);
			switch (symbol) {
			case '^':
				cell.setDoorway(true);
				cell.setDoorDirection(DoorDirection.UP);
				break;
			case 'v':
				cell.setDoorway(true);
				cell.setDoorDirection(DoorDirection.DOWN);
				break;
			case '>':
				cell.setDoorway(true);
				cell.setDoorDirection(DoorDirection.RIGHT);
				break;
			case '<':
				cell.setDoorway(true);
				cell.setDoorDirection(DoorDirection.LEFT);
				break;
			case '*':
				cell.setRoomCenter(true);
				getRoom(cell.getInitial()).setCenterCell(cell);
				break;
			case '#':
				cell.setLabel(true);
				getRoom(cell.getInitial()).setLabelCell(cell);
				break;
			default:
				cell.setSecretPassage(symbol);

			}
		} 
	}

	public void createAdjacencyList( BoardCell[][] grid) {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if (grid[i][j].getInitial() == 'W') {
					if(grid[i][j].isDoorway()) {
						switch (grid[i][j].getDoorDirection()) {
						case UP:
							grid[i][j].addAdjacency(getRoom(grid[i-1][j].getInitial()).getCenterCell());
							getRoom(grid[i-1][j].getInitial()).getCenterCell().addAdjacency(grid[i][j]);
							break;
						case DOWN:
							grid[i][j].addAdjacency(getRoom(grid[i+1][j].getInitial()).getCenterCell());
							getRoom(grid[i+1][j].getInitial()).getCenterCell().addAdjacency(grid[i][j]);
							break;
						case RIGHT:
							grid[i][j].addAdjacency(getRoom(grid[i][j+1].getInitial()).getCenterCell());
							getRoom(grid[i][j + 1].getInitial()).getCenterCell().addAdjacency(grid[i][j]);
							break;
						case LEFT:
							grid[i][j].addAdjacency(getRoom(grid[i][j-1].getInitial()).getCenterCell());
							getRoom(grid[i][j-1].getInitial()).getCenterCell().addAdjacency(grid[i][j]);
							break;
						default:
							break;
						}

					} 
					if (i == 0) {
						if (j == 0) { // at top left, so we can only go down or right
							if (testRoomAndUnused(grid[i+1][j])) {
								grid[i][j].addAdjacency(grid[i+1][j]);
							}
							if (testRoomAndUnused(grid[i][j+1])) {
								grid[i][j].addAdjacency(grid[i][j+1]);
							}
						} else if (j == numCols - 1) { // at top right, so we can only go down or left
							if(testRoomAndUnused(grid[i+1][j])) {
								grid[i][j].addAdjacency(grid[i+1][j]);
							}
							if(testRoomAndUnused(grid[i][j-1])) {
								grid[i][j].addAdjacency(grid[i][j-1]);
							}
						} else { // at top edge, so we can only go down,left, or right
							if(testRoomAndUnused(grid[i+1][j])) {
								grid[i][j].addAdjacency(grid[i+1][j]);
							}
							if(testRoomAndUnused(grid[i][j+1])) {
								grid[i][j].addAdjacency(grid[i][j+1]);
							}
							if(testRoomAndUnused(grid[i][j-1])) {
								grid[i][j].addAdjacency(grid[i][j-1]);
							}
						}
					} else if (i == numRows -1) {
						if (j == 0) { // // at bottom left, so we can only go up or right
							if(testRoomAndUnused(grid[i-1][j])) {
								grid[i][j].addAdjacency(grid[i-1][j]);
							}
							if(testRoomAndUnused(grid[i][j+1])) {
								grid[i][j].addAdjacency(grid[i][j+1]);
							}
						} else if (j == numCols - 1) { // at bottom right, so we can only go up or left
							if(testRoomAndUnused(grid[i-1][j])) {
								grid[i][j].addAdjacency(grid[i-1][j]);
							}
							if(testRoomAndUnused(grid[i][j-1])) {
								grid[i][j].addAdjacency(grid[i][j-1]);
							}
						} else { // at bottom edge, so we can only go up, right, and left
							if(testRoomAndUnused(grid[i-1][j])) {
								grid[i][j].addAdjacency(grid[i-1][j]);
							}
							if(testRoomAndUnused(grid[i][j+1])) {
								grid[i][j].addAdjacency(grid[i][j+1]);
							}
							if(testRoomAndUnused(grid[i][j-1])) {
								grid[i][j].addAdjacency(grid[i][j-1]);
							}
						}
					} else if (j == 0) { // at left edge, so we can only go up, down, or right
						if(testRoomAndUnused(grid[i-1][j])) {
							grid[i][j].addAdjacency(grid[i-1][j]);
						}
						if(testRoomAndUnused(grid[i+1][j])) {
							grid[i][j].addAdjacency(grid[i+1][j]);
						}
						if(testRoomAndUnused(grid[i][j+1])) {
							grid[i][j].addAdjacency(grid[i][j+1]);
						}
					} else if (j == numCols -1) { // at right edge, so we can only go up, down, or left
						if(testRoomAndUnused(grid[i-1][j])) {
							grid[i][j].addAdjacency(grid[i-1][j]);
						}
						if(testRoomAndUnused(grid[i+1][j])) {
							grid[i][j].addAdjacency(grid[i+1][j]);
						}
						if(testRoomAndUnused(grid[i][j-1])) {
							grid[i][j].addAdjacency(grid[i][j-1]);
						}
					} else { // in middle, we have adjacent cells on all 4 sides
						if(testRoomAndUnused(grid[i-1][j])) {
							grid[i][j].addAdjacency(grid[i-1][j]);
						}
						if(testRoomAndUnused(grid[i+1][j])) {
							grid[i][j].addAdjacency(grid[i+1][j]);
						}
						if(testRoomAndUnused(grid[i][j-1])) {
							grid[i][j].addAdjacency(grid[i][j-1]);
						}
						if(testRoomAndUnused(grid[i][j+1])) {
							grid[i][j].addAdjacency(grid[i][j+1]);
						}
					}
				} else {
					if (grid[i][j].getInitial() != 'X') {
						if(grid[i][j].isRoomCenter()) {
							if(grid[i][j].isSecretPassage()) {
								grid[i][j].addAdjacency(getRoom(grid[i][j].getSecretPassage()).getCenterCell());
							}
						}
			}
		}
	}
}
}

private boolean testRoomAndUnused(BoardCell cell) {
	if (cell.getInitial() == 'W') {
		return true;
	} else {
		return false;
	}
}

public Set<BoardCell> getAdjList(int i, int j) {
	return grid[i][j].getAdjList();
}

public void calcTargets(BoardCell cell, int i) {

}

public Set<BoardCell> getTargets() {
	Set<BoardCell> empty = new HashSet<BoardCell>();
	return empty;
}


}

