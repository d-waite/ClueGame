// Authors: David Waite & Dillinger Day
package clueGame;

import java.util.*;

import experiment.TestBoardCell;

import java.io.*;

public class Board {
	// all our variables needed for the board
	private String setupConfigFile;
	private String layoutConfigFile;
	private int numRows;
	private int numCols;
	private static Board theInstance = new Board();
	private BoardCell[][] grid; // contains all of our cells
	private Map<Character, Room> rooms; // contains all of the rooms with their corresponding initial
	private Set<BoardCell> targets = new HashSet<BoardCell>(); // stores all choices for player to move to
	private Set<BoardCell> visited  = new HashSet<BoardCell>(); // helps us to get our target list
	private HumanPlayer human;
	private ArrayList<ComputerPlayer> computers = new ArrayList<ComputerPlayer>();

	private Board() {
		super();
	}

	public static Board getInstance() {
		return theInstance; // Singleton pattern
	}

	public void initialize() {
		try {
			//load the information in the files into the board
			loadSetupConfig(null); // passing in null so tests pass
			loadLayoutConfig(null);
		}
		catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}
		catch (FileNotFoundException e) {
			System.out.println("Error opening file.");
		}
		createAdjacencyList(grid); // creates adjacency list for all cells on the board
	}

	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}

	public BoardCell getCell(int row, int column) {
		return grid[row][column];
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
		int setupLineLength = 3; // amount of entries for room info
		//Open the file
		FileReader input = new FileReader("data/" + setupConfigFile);
		Scanner scan = new Scanner(input);
		//initialize room map
		rooms = new HashMap<Character, Room>();
		//Loop until all rooms have been read
		String roomLine = "";
		while (!roomLine.contains("players")) {
			roomLine = scan.nextLine();
			//Skip the line if the first character is a slash
			if (roomLine.charAt(0) == '/') {
				roomLine = scan.nextLine();
			}
			//Split the line with commas as the delimiters
			String[] roomInfo = new String[setupLineLength];
			roomInfo = roomLine.split(", ");
			//If the first word in the line isn't Space or Room throw an Exception
			String roomType = roomInfo[0];
			if (!(roomType.equals("Room")) && !(roomType.equals("Space"))) {
				throw new BadConfigFormatException("Bad room type in setup file.");
			}
			String roomSymbol = roomInfo[2];
			// Create a new room and insert it into the map
			String roomName = roomInfo[1];
			Room room = new Room(roomName);
			char roomInitial = roomSymbol.charAt(0);
			rooms.put(roomInitial, room);
		}
		
		setupLineLength = 5; // amount of entries for player info
		String playerLine = "";
		while (!playerLine.contains("weapons")) {
			playerLine = scan.nextLine();
			//Skip the line if the first character is a slash
			if (playerLine.charAt(0) == '/') {
				playerLine = scan.nextLine();
			}
			//Split the line with commas as the delimiters
			String[] playerInfo = new String[setupLineLength];
			playerInfo = playerLine.split(", ");
			//If the first word in the line isn't Human or Computer throw an Exception
			String playerType = playerInfo[0];
			if (!(playerType.equals("Human")) && !(playerType.equals("Computer"))) {
				throw new BadConfigFormatException("Bad player type in setup file.");
			}
			String playerName = playerInfo[1];
			String playerColor = playerInfo[2];
			int playerRow = Integer.parseInt(playerInfo[3]);
			int playerColumn = Integer.parseInt(playerInfo[4]);
			
			if (playerType.equals("Human")) {
				human = new HumanPlayer(playerName, playerColor, playerRow, playerColumn);
			} else {
				computers.add(new ComputerPlayer(playerName, playerColor, playerRow, playerColumn));
			}
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
		
			for (int row = 0; row < numRows; row++) {
				//split the row along the commas
				String symbolRow = scanCells.nextLine();
				String[] seperatedRow = new String[numCols];
				seperatedRow = symbolRow.split(",");
				//make all of the cells in the row into BoardCells and add them to the grid
				for (int column = 0; column < numCols; column++) {
					BoardCell cell = new BoardCell(row,column);
					initializeCell(cell, seperatedRow[column]);
					grid[row][column] = cell;
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
				cell.setSecretPassage(symbol); // If we got through all of the other symbols, then the only other option is a secret room

			}
		} 
	}

	public void createAdjacencyList( BoardCell[][] grid) {
		for(int row = 0; row < numRows; row++) { // go through every cell in the grid
			for(int column = 0; column < numCols; column++) {
				BoardCell currentCell = grid[row][column];
				if (currentCell.getInitial() == 'W') { // if cell is a walkway
					//We have specific additional adjacency handling for a doorway
					if(currentCell.isDoorway()) {
						initializeDoorway(currentCell, row, column);
					}
					//This logic is needed regardless of whether or not the current cell is a doorway
					if (row == 0) {
						if (column == 0) { // at top left, so we can only go down or right
							addCellsToAdjList(currentCell, false, true, false, true, row, column);
						} else if (column == numCols - 1) { // at top right, so we can only go down or left
							addCellsToAdjList(currentCell, true, false, false, true, row, column);
						} else { // at top edge, so we can only go down,left, or right
							addCellsToAdjList(currentCell, true, true, false, true, row, column);
						}
					} else if (row == numRows - 1) {
						if (column == 0) { // // at bottom left, so we can only go up or right
							addCellsToAdjList(currentCell, false, true, true, false, row, column);
						} else if (column == numCols - 1) { // at bottom right, so we can only go up or left
							addCellsToAdjList(currentCell, true, false, true, false, row, column);
						} else { // at bottom edge, so we can only go up, right, and left
							addCellsToAdjList(currentCell, true, true, true, false, row, column);
						}
					} else if (column == 0) { // at left edge, so we can only go up, down, or right
						addCellsToAdjList(currentCell, false, true, true, true, row, column);
					} else if (column == numCols -1) { // at right edge, so we can only go up, down, or left
						addCellsToAdjList(currentCell, true, false, true, true, row, column);
					} else { // in middle, we have adjacent cells on all 4 sides
						addCellsToAdjList(currentCell, true, true, true, true, row, column);
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
	
	private void initializeDoorway(BoardCell currentCell,int row,int column) {
		int above = row-1; 
		int below = row+1;
		int right = column+1;
		int left = column-1;
		switch (currentCell.getDoorDirection()) {
		// add center cell of the room the doorway enters into to the adjacency list of the current cell
		// & vice versa
		case UP:
			currentCell.addAdjacency(getRoom(grid[above][column].getInitial()).getCenterCell());
			getRoom(grid[above][column].getInitial()).getCenterCell().addAdjacency(currentCell);
			break;
		case DOWN:
			currentCell.addAdjacency(getRoom(grid[below][column].getInitial()).getCenterCell());
			getRoom(grid[below][column].getInitial()).getCenterCell().addAdjacency(currentCell);
			break;
		case RIGHT:
			currentCell.addAdjacency(getRoom(grid[row][right].getInitial()).getCenterCell());
			getRoom(grid[row][right].getInitial()).getCenterCell().addAdjacency(currentCell);
			break;
		case LEFT:
			currentCell.addAdjacency(getRoom(grid[row][left].getInitial()).getCenterCell());
			getRoom(grid[row][left].getInitial()).getCenterCell().addAdjacency(currentCell);
			break;
		default:
			break;
		}
	}

	private void addCellsToAdjList(BoardCell cell, boolean left, boolean right, boolean up, boolean down, int row, int column) {
		if (left) { // add left cell to adjacency list
			BoardCell leftCell = getCell(row,column-1);
			if (testValidAdjacency(leftCell)) {
				cell.addAdjacency(leftCell);
			}
		}
		if (right) { // add right cell to adjacency list
			BoardCell rightCell = getCell(row,column+1);
			if (testValidAdjacency(rightCell)) {
				cell.addAdjacency(rightCell);
			}
		}
		if (up) { // add above cell to adjacency list
			BoardCell aboveCell = getCell(row-1,column);
			if (testValidAdjacency(aboveCell)) {
				cell.addAdjacency(aboveCell);
			}
		}
		if (down) { // add below cell to adjacency list
			BoardCell belowCell = getCell(row+1,column);
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

	public Set<BoardCell> getAdjList(int row, int column) {
		return grid[row][column].getAdjList();
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

	public ArrayList<ComputerPlayer> getComputerPlayers() {
		return computers;
	}

	public HumanPlayer getHumanPlayer() {
		return human;
	}


}

