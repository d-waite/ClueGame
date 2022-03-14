package clueGame;

import java.util.*;

import experiment.TestBoardCell;

import java.io.*;

public class Board {
	private String setupConfigFile;
	private String layoutConfigFile;
	private int numRows;
	private int numCols;
	private static Board theInstance = new Board();
	private BoardCell[][] grid;
	private Map<Character, Room> rooms;
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private Set<BoardCell> visited  = new HashSet<BoardCell>();

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
		createAdjacencyList(grid);
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
	private void initializeCell(BoardCell cell, String label) throws BadConfigFormatException{
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
		for(int i = 0; i < numRows; i++) { // go through every cell in the grid
			for(int j = 0; j < numCols; j++) {
				BoardCell currentCell = grid[i][j];
				if (currentCell.getInitial() == 'W') { // if cell is a walkway
					if(currentCell.isDoorway()) { 
						initializeDoorway(currentCell, i, j);
					}
					
					if (i == 0) {
						if (j == 0) { // at top left, so we can only go down or right
							addCellsToAdjList(currentCell, false, true, false, true, i, j);
						} else if (j == numCols - 1) { // at top right, so we can only go down or left
							addCellsToAdjList(currentCell, true, false, false, true, i, j);
						} else { // at top edge, so we can only go down,left, or right
							addCellsToAdjList(currentCell, true, true, false, true, i, j);
						}
					} else if (i == numRows - 1) {
						if (j == 0) { // // at bottom left, so we can only go up or right
							addCellsToAdjList(currentCell, false, true, true, false, i, j);
						} else if (j == numCols - 1) { // at bottom right, so we can only go up or left
							addCellsToAdjList(currentCell, true, false, true, false, i, j);
						} else { // at bottom edge, so we can only go up, right, and left
							addCellsToAdjList(currentCell, true, true, true, false, i, j);
						}
					} else if (j == 0) { // at left edge, so we can only go up, down, or right
						addCellsToAdjList(currentCell, false, true, true, true, i, j);
					} else if (j == numCols -1) { // at right edge, so we can only go up, down, or left
						addCellsToAdjList(currentCell, true, false, true, true, i, j);
					} else { // in middle, we have adjacent cells on all 4 sides
						addCellsToAdjList(currentCell, true, true, true, true, i, j);
					}
					
				} else {
					if(currentCell.isSecretPassage()) {
						// connect center cells of rooms that have secret passages
						getRoom(currentCell).getCenterCell().addAdjacency(getRoom(currentCell.getSecretPassage()).getCenterCell());
					}
				}
			}
		}
	}
	
	private void initializeDoorway(BoardCell currentCell,int i,int j) {
		int above = i-1; 
		int below = i+1;
		int right = j+1;
		int left = j-1;
		switch (currentCell.getDoorDirection()) {
		// add center cell of the room the doorway enters into to the adjacency list of the current cell
		// & vice versa
		case UP:
			currentCell.addAdjacency(getRoom(grid[above][j].getInitial()).getCenterCell());
			getRoom(grid[above][j].getInitial()).getCenterCell().addAdjacency(currentCell);
			break;
		case DOWN:
			currentCell.addAdjacency(getRoom(grid[below][j].getInitial()).getCenterCell());
			getRoom(grid[below][j].getInitial()).getCenterCell().addAdjacency(currentCell);
			break;
		case RIGHT:
			currentCell.addAdjacency(getRoom(grid[i][right].getInitial()).getCenterCell());
			getRoom(grid[i][right].getInitial()).getCenterCell().addAdjacency(currentCell);
			break;
		case LEFT:
			currentCell.addAdjacency(getRoom(grid[i][left].getInitial()).getCenterCell());
			getRoom(grid[i][left].getInitial()).getCenterCell().addAdjacency(currentCell);
			break;
		default:
			break;
		}
	}

	private void addCellsToAdjList(BoardCell cell, boolean left, boolean right, boolean up, boolean down, int i, int j) {
		if (left) { // add left cell to adjacency list
			BoardCell leftCell = getCell(i,j-1);
			if (testValidAdjacency(leftCell)) {
				cell.addAdjacency(leftCell);
			}
		}
		if (right) { // add right cell to adjacency list
			BoardCell rightCell = getCell(i,j+1);
			if (testValidAdjacency(rightCell)) {
				cell.addAdjacency(rightCell);
			}
		}
		if (up) { // add above cell to adjacency list
			BoardCell aboveCell = getCell(i-1,j);
			if (testValidAdjacency(aboveCell)) {
				cell.addAdjacency(aboveCell);
			}
		}
		if (down) { // add below cell to adjacency list
			BoardCell belowCell = getCell(i+1,j);
			if (testValidAdjacency(belowCell)) {
				cell.addAdjacency(belowCell);
			}
		}
	}
	
	// test whether cell is a valid adjacent cell
	// essentially, if cell is part of a room (room center already dealt with) or is unused,
	// it should not go in adjacency list
	private boolean testValidAdjacency(BoardCell cell) {
		if (cell.getInitial() == 'W') {
			return true;
		} else {
			return false;
		}
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		return grid[i][j].getAdjList();
	}

	public void calcTargets(BoardCell cell, int pathlength) {
		// make sure data structures are cleared for new calculation
		visited.clear(); 
		targets.clear();
		visited.add(cell); // visited current cell
		findAllTargets(cell, pathlength);
	}

	private void findAllTargets(BoardCell cell, int pathlength) {
		for (BoardCell adjCell: cell.getAdjList()) { // look at all adjacent cells
			// can't go through same space twice in one turn & can't visit an occupied space
			if ((!(visited.contains(adjCell))) && (!(adjCell.getOccupied()))) { 
				visited.add(adjCell); 
				if (adjCell.getRoom()) { // turn ends once room is entered
					targets.add(adjCell);
				} else if (pathlength == 1) { // end of roll
					targets.add(adjCell);
				} else {
					findAllTargets(adjCell, pathlength - 1); 
				}
				visited.remove(adjCell); 
			}
		}
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}


}

