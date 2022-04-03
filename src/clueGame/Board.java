// Authors: David Waite & Dillinger Day
package clueGame;

import java.util.*;
import java.util.function.BooleanSupplier;

import experiment.TestBoardCell;
import clueGame.Solution;
import clueGame.Card;

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
	private ArrayList<ComputerPlayer> computers;
	private ArrayList<Card> deck;
	private Solution theAnswer;

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
		createAdjacencyList(); // creates adjacency list for all cells on the board
		deal(); // deals out solution and cards to players
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
		deck = new ArrayList<Card>();
		int setupLineLength = 3; // amount of entries for room info
		//Open the file
		FileReader input = new FileReader("data/" + setupConfigFile);
		Scanner scan = new Scanner(input);
		//initialize room map
		rooms = new HashMap<Character, Room>();
		computers = new ArrayList<ComputerPlayer>();
		//Loop until all rooms have been read
		
		while (scan.hasNextLine()) {
			String roomLine = scan.nextLine();
			//If we reach the line that says players, then we need to go to the next loop
			if (roomLine.contains("players")) {
				break;
			}
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
			//We don't want to make cards for the spaces
			if (!roomType.equals("Space")) {
				deck.add(new Card(roomName, CardType.ROOM));
			}
		}
		
		setupLineLength = 5; // amount of entries for player info
		
		while (scan.hasNextLine()) {
			String playerLine = scan.nextLine();
			//If we reach the line that says weapons, then we need to go to the next loop
			if(playerLine.contains("weapons")) {
				break;
			}
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
			deck.add(new Card(playerName, CardType.PERSON));
			
		}
		
		setupLineLength = 1; // amount of entries for weapon info
		while(scan.hasNextLine()) {
			String weaponName = scan.nextLine();
			deck.add(new Card(weaponName, CardType.WEAPON));
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

	public void createAdjacencyList() {
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

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public Solution getSolution() {
		return theAnswer;
	}

	public void deal() {
		clearHands(); // starting with empty hand
		
		//create a copy of the deck that we can remove from, leaving original deck alone for testing purposes
		ArrayList<Card> newDeck = new ArrayList<Card>();
		for (int i = 0; i < deck.size(); i++) {
			newDeck.add(deck.get(i));
		}
		
		dealSolution(newDeck); // deal the solution first

		// deal the rest of the cards to the players
		Random randCard = new Random();
		int deckSize = 0;
		//loop through each of the players
		for (int playerNum = 6; playerNum >= 1; playerNum--) {
			//update the deck size so that the right amount of cards are dealt to each person
			deckSize = newDeck.size();
			//loop through the amount of cards per player
			for (int j = 0; j < (deckSize / playerNum); j++) {
				//choose a random card 
				int randCardNum = randCard.nextInt(deckSize - j);
				//add it to the player's hand and remove it from newDeck
				switch (playerNum) {
				case 1:
					getHumanPlayer().updateHand(newDeck.get(randCardNum));
					newDeck.remove(newDeck.get(randCardNum));
					break;
				case 2:
					getComputerPlayers().get(0).updateHand(newDeck.get(randCardNum));
					newDeck.remove(newDeck.get(randCardNum));
					break;
				case 3:
					getComputerPlayers().get(1).updateHand(newDeck.get(randCardNum));
					newDeck.remove(newDeck.get(randCardNum));
					break;
				case 4:
					getComputerPlayers().get(2).updateHand(newDeck.get(randCardNum));
					newDeck.remove(newDeck.get(randCardNum));
					break;
				case 5:
					getComputerPlayers().get(3).updateHand(newDeck.get(randCardNum));
					newDeck.remove(newDeck.get(randCardNum));
					break;
				case 6:
					getComputerPlayers().get(4).updateHand(newDeck.get(randCardNum));
					newDeck.remove(newDeck.get(randCardNum));
					break;
				default:
					break;
				}
			}
		}
	}
	
	// clearing hands for when we test new deals
	public void clearHands() {
		getHumanPlayer().clearHand();
		for (int i = 0; i < getComputerPlayers().size(); i++) {
			getComputerPlayers().get(i).clearHand();
		}
	}
	
	public void dealSolution(ArrayList<Card> newDeck) {
		// these arraylists hold the position of cards with said card type in the deck
		ArrayList<Integer> rooms = new ArrayList<Integer>();
		ArrayList<Integer> people = new ArrayList<Integer>();
		ArrayList<Integer> weapons = new ArrayList<Integer>();
		
		//separate the deck into the different card types
		for (int k = 0; k < deck.size(); k++) {
			if (deck.get(k).getCardType() == CardType.PERSON) {
				people.add(k);
			} else if (deck.get(k).getCardType() == CardType.WEAPON) {
				weapons.add(k);
			} else {
				rooms.add(k);
			}
		}
		
		//select a random card of each type
		Random randomCard = new Random();
		int roomNum = randomCard.nextInt(rooms.size()); // getting an index to get a position of a room card in the deck
		Card solutionRoom = deck.get(rooms.get(roomNum));
		int weaponNum = randomCard.nextInt(weapons.size()); // getting an index to get a position of a weapon card in the deck
		Card solutionWeapon = deck.get(weapons.get(weaponNum));
		int peopleNum = randomCard.nextInt(people.size()); // getting an index to get a position of a person card in the deck
		Card solutionPerson = deck.get(people.get(peopleNum));
		
		//add the random cards to the solution
		theAnswer = new Solution(solutionRoom, solutionWeapon, solutionPerson);
		
		//Take solution cards out of the new deck
		newDeck.remove(newDeck.get(weapons.get(weaponNum)));
		newDeck.remove(newDeck.get(people.get(peopleNum)));
		newDeck.remove(newDeck.get(rooms.get(roomNum)));
	}
	
	// passing in strings since player most likely won't have the cards in the solution (i.e. won't need to search through deck to get card)
	public boolean checkAccusation(String room, String weapon, String person) { 
		return room.equals(theAnswer.getRoom().getCardName()) && weapon.equals(theAnswer.getWeapon().getCardName()) && person.equals(theAnswer.getPerson().getCardName());
	}

	public void setSolution(Solution solution) {
		theAnswer = solution;		
	}

	public Card handleSuggestion(Card hall, Card prof, Card poison, Player playerSuggesting) {
		return new Card("iLike2eat",CardType.WEAPON);
	}
}

