// Authors: David Waite & Dillinger Day
package clueGame;

import java.util.*;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class Board extends JPanel {
	// all our variables needed for the board
	private String setupConfigFile;
	private String layoutConfigFile;
	private int numRows;
	private int numCols;
	private static Board theInstance = new Board();
	private BoardCell[][] grid; // contains all of our cells
	private Map<Character, Room> rooms; // contains all of the rooms with their corresponding initial
	private ArrayList<Card> people; // contains all of the people cards
	private ArrayList<Card> weapons; // contains all of the weapon cards
	private Set<BoardCell> targets = new HashSet<BoardCell>(); // stores all choices for player to move to
	private Set<BoardCell> visited  = new HashSet<BoardCell>(); // helps us to get our target list
	private HumanPlayer human;
	private ArrayList<ComputerPlayer> computers;
	private ArrayList<Player> currentPlayers; // for when we don't care if players are human or computer
	private ArrayList<Player> allPlayers; // we don't delete from this one when a player makes a wrong accusation
	private ArrayList<Card> deck;
	private Solution theAnswer;
	private boolean humanFinished; // so we know whether it is okay to move to next player
	private Player whoseTurn;
	private int whoseTurnNum; // helps move from human player to computer player while keeping them separate
	private int roll;
	private boolean highlightTargets; // tells us whether we need to show possible moves for human
	private Solution guess;
	private Player whoDisproved;
	private int cellSize;
	private boolean computerAccusationFlag = false;
	private Solution computerAccusation;

	private Board() {
		super();
		setBackground(Color.black); // to have something where there is no board
		addMouseListener(new clickListener());
	}

	public static Board getInstance() {
		return theInstance; // Singleton pattern
	}

	public void initialize() {
		try {
			//load the information in the files into the board
			loadSetupConfig();
			loadLayoutConfig();
		}
		catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}
		catch (FileNotFoundException e) {
			System.out.println("Error opening file.");
		}
		createAdjacencyList(); // creates adjacency list for all cells on the board
		deal(); // deals out solution and cards to players
		whoseTurn = human; // human player starts first in the game
		whoseTurnNum = -1; // negative number indicates it is human's turn
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

	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		deck = new ArrayList<Card>();
		int setupLineLength = 3; // amount of entries for room info
		//Open the file
		FileReader input = new FileReader("data/" + setupConfigFile);
		Scanner scan = new Scanner(input);
		//initialize room map
		rooms = new HashMap<Character, Room>();
		computers = new ArrayList<ComputerPlayer>();
		currentPlayers = new ArrayList<Player>();
		allPlayers = new ArrayList<Player>();
		people = new ArrayList<Card>();
		weapons = new ArrayList<Card>();
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
				scan.close();
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
				scan.close();
				throw new BadConfigFormatException("Bad player type in setup file.");
			}
			String playerName = playerInfo[1];
			String playerColor = playerInfo[2];
			int playerRow = Integer.parseInt(playerInfo[3]);
			int playerColumn = Integer.parseInt(playerInfo[4]);

			if (playerType.equals("Human")) {
				human = new HumanPlayer(playerName, playerColor, playerRow, playerColumn);
				currentPlayers.add(human);
				allPlayers.add(human);
			} else {
				ComputerPlayer computer = new ComputerPlayer(playerName, playerColor, playerRow, playerColumn);
				computers.add(computer);
				currentPlayers.add(computer);
				allPlayers.add(computer);
			}
			Card currentPerson = new Card(playerName, CardType.PERSON);
			deck.add(currentPerson);
			people.add(currentPerson);
		}

		setupLineLength = 1; // amount of entries for weapon info
		while(scan.hasNextLine()) {
			String weaponName = scan.nextLine();
			Card currentWeapon = new Card(weaponName, CardType.WEAPON);
			deck.add(currentWeapon);
			weapons.add(currentWeapon);
		}
		scan.close();
	}

	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException{
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
				scanSize.close();
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
			getRoom(cell.getInitial()).addRoomCell(cell); //adds cell to the room's array list of cells
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
			if ((!(visited.contains(adjCell))) && (!(adjCell.isOccupied()))) { 
				visited.add(adjCell); 
				if (adjCell.isRoom()) { // turn ends once room is entered
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
		ArrayList<Card> newDeck = new ArrayList<Card>(deck);

		dealSolution(newDeck); // deal the solution first

		// deal the rest of the cards to the players
		Random randCard = new Random();
		int deckSize = 0;
		int undealtPlayers = currentPlayers.size();
		//loop through each of the players
		for (Player player: currentPlayers) {
			//update the deck size so that the right amount of cards are dealt to each person
			deckSize = newDeck.size();
			//loop through the amount of cards per player
			for (int cardsDealt = 0; cardsDealt < deckSize / undealtPlayers; cardsDealt++) { // this evenly deals out cards to each player
				int randCardNum = randCard.nextInt(deckSize - cardsDealt);
				//set the player color of the card
				newDeck.get(randCardNum).setPlayerColor(player.getColor());
				//add the card to the hand and remove it from newDeck
				player.updateHand(newDeck.get(randCardNum));
				newDeck.remove(newDeck.get(randCardNum));
			}
		undealtPlayers--;
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
		// these array lists hold the position of cards with said card type in the deck
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

	// if all cards match the accusation, return true
	public boolean checkAccusation(Solution accusation) { 
		return accusation.getRoom().equals(theAnswer.getRoom()) && accusation.getPerson().equals(theAnswer.getPerson()) && accusation.getWeapon().equals(theAnswer.getWeapon());
	}

	public void setSolution(Solution solution) {
		theAnswer = solution;		
	}

	public Card handleSuggestion(Solution suggestion, Player playerSuggesting) {
		int startIndex = currentPlayers.indexOf(playerSuggesting); // get index of person who is making the suggestion

		for (int i = startIndex + 1; i < currentPlayers.size(); i++) { // start with next person till end of list
			Card cardShown = currentPlayers.get(i).disproveSuggestion(suggestion);
			if (cardShown != null) {
				whoDisproved = currentPlayers.get(i);
				playerSuggesting.updateSeen(cardShown);
				if (playerSuggesting.getName().equals(human.getName())) {
					ClueGame.updateCardPanel();
					ClueGame.getControlPanel().setGuessResult(cardShown.getCardName(),whoDisproved.getColor());
				}
				return cardShown;
			}
		}

		for (int i = 0; i < startIndex; i++) { // start at beginning and stop before person suggesting
			Card cardShown = currentPlayers.get(i).disproveSuggestion(suggestion);
			if (cardShown != null) {
				whoDisproved = currentPlayers.get(i);
				playerSuggesting.updateSeen(cardShown);
				if (playerSuggesting.getName().equals(human.getName())) {
					ClueGame.updateCardPanel();
					ClueGame.getControlPanel().setGuessResult(cardShown.getCardName(),whoDisproved.getColor());
				}
				return cardShown;
			}
		}
		ClueGame.getControlPanel().setGuessResult("Suggestion Not Disproved", Color.WHITE);
		computerAccusationFlag = true;
		computerAccusation = suggestion;
		return null; // no one could disprove
	}

	public void setAllPlayers(ArrayList<Player> players) { // for testing
		currentPlayers = players;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // need to call for its functionality
		// getting the size of the board JPanel
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		// whole board needs to fit in the minimum length of the frame, each cell should take up the same amount of space
		cellSize = Math.min(panelWidth / numCols, panelHeight / numRows);
		// need an offset for x-direction and y-direction so board stays in center of panel, no matter any resizing
		// if board takes up less space than the panel width or height, make it centered in that direction
		int offsetX = (panelWidth - (numCols * cellSize)) / 2; // get any space left in panel, distribute half of the space to both sides of the board
		int offsetY = (panelHeight - (numRows * cellSize)) / 2;
		// first, draw walkways and rooms
		drawCellsAndRooms(g, offsetX, offsetY, highlightTargets);
		// then, draw doors to rooms; doors drawn on top of rooms so different function
		drawDoors(g, offsetX, offsetY);
		// next, draw room names on rooms
		drawRoomLabels(g, offsetX, offsetY);
		// finally, draw players
		drawPlayers(g, offsetX, offsetY);		
	}
	
	private void drawDoors(Graphics g, int offsetX, int offsetY) { // private since it is a helper function for paintComponent()
		int x = offsetX, y = offsetY; // start at our offset so we are centered
		for (int row = 0; row < numRows; row++) { // go through grid
			for (int column = 0; column < numCols; column++) {
				if (getCell(row, column).isDoorway()) {
					getCell(row, column).drawDoor(g, x, y, cellSize);
				}
				x += cellSize; // move to next column
			}
			y += cellSize; // move to next row
			x = offsetX; // go back to first column
		}
	}

	private void drawCellsAndRooms(Graphics g, int offsetX, int offsetY, boolean highlight) {
		int x = offsetX, y = offsetY; 
		for (int row = 0; row < numRows; row++) {
			for (int column = 0; column < numCols; column++) {
				if (getCell(row, column).isRoom()) { // separating rooms from walkways
					if (targets.contains(rooms.get(getCell(row,column).getInitial()).getCenterCell()) && highlight == true) {
						getCell(row, column).drawTarget(g, x, y, cellSize);
					} else {
						getCell(row, column).drawRoom(g, x, y, cellSize);
					}
				} else if (targets.contains(getCell(row,column)) && highlight == true) {
					getCell(row,column).drawTarget(g, x, y, cellSize);
				} else {
					getCell(row, column).draw(g, x, y, cellSize);
				}	
				x += cellSize; // move to next column
			}
			y += cellSize; // move to next row
			x = offsetX; // go back to first column
		}
	}

	private void drawRoomLabels(Graphics g, int offsetX, int offsetY) {
		for (char c: rooms.keySet()) {
			if (!(c == 'X') && !(c == 'W')) { // if not a walkway or unused space, we are in a true room and need to draw its label
				BoardCell labelCell = rooms.get(c).getLabelCell();	// get the cell in the room marked to hold label		
				int fontSize = cellSize / 2; // we found that cellSize / 2 kept the names within the space of the rooms, even w/ resizing
				// calculating coordinates for name
				int roomLabelY = labelCell.getRow() * cellSize + offsetY; // find our label cell's drawn position and start writing name there
				int roomLabelX = labelCell.getColumn() * cellSize + offsetX;
				String roomName = rooms.get(c).getName(); // find out the name of the room
				labelCell.drawRoomName(g, roomName, fontSize, roomLabelX, roomLabelY);
			}
		}
	}
	
	// instead of offsetting player positioning, this function displays the names of the players in the room
	public void drawOverlappingPlayers(Player player, Graphics g) {
		if (getCell(player.getRow(), player.getColumn()).isRoom()) { // if a player is in the room, check to see if others are in the room
			String overlappingPlayers = "Players: "; // creating a string to hold a list of the players currently in the room
			BoardCell cell = grid[player.getRow()][player.getColumn()];
			for (Player p: currentPlayers) {
				if ((p.getRow() == cell.getRow()) && (p.getColumn() == cell.getColumn())) { // if multiple players in same room
					overlappingPlayers += p.getName() + ", ";
					// display list of players in the room
					g.setColor(Color.white);
					g.drawString(overlappingPlayers, getRoom(getCell(player.getRow(), player.getColumn())).getCenterCell().getX() - cellSize, getRoom(getCell(player.getRow(), player.getColumn())).getCenterCell().getY() + 2 * cellSize);
				}
			}
		}
	}

	private void drawPlayers(Graphics g, int offsetX, int offsetY) {
		// tell each player to draw themselves on the board
		for (Player player: currentPlayers) {
			player.draw(g, cellSize, offsetX, offsetY);
			drawOverlappingPlayers(player, g); // deal with the overlapping players in rooms
		}
	}
	
	public boolean isHumanPlayerFinished() {
		return humanFinished;
	}

	public void displayTargets() {
		highlightTargets = true;
		repaint();
	}

	public void rollDie() {
		Random random = new Random();
		roll = random.nextInt(6) + 1; // 6-sided die from 1 - 6
	}
	
	public int getRoll() {
		return roll;
	}
	
	// function to move to the next person's turn
	public void next() {
		if (whoseTurnNum < computers.size() - 1) { // stay in bounds of computer player array
			whoseTurnNum++;
			whoseTurn = computers.get(whoseTurnNum);
			highlightTargets = false; //moved here so that when you resize the window, the targets are still on screen
		} else {
			whoseTurnNum = -1; // if at the end of the computer player array, human is up next
			whoseTurn = human;
		}
	}
	
	// gets the player ready to move
	public void setUpTurn() {
		rollDie();
		calcTargets(getCell(whoseTurn.getRow(),whoseTurn.getColumn()), roll);
	}

	public Player getWhoseTurn() {
		return whoseTurn;
	}

	public void processTurn() {
		if (whoseTurn.getName().equals(human.getName())) { // human's turn, so show targets and set turn finished flag to false
			displayTargets();
			humanFinished = false;
		} else {
			// accusation???
			if (computerAccusationFlag) {
				boolean computerAccusationResult = checkAccusation(computerAccusation);
				if (computerAccusationResult) {
					JOptionPane.showMessageDialog(ClueGame.getGame(), whoseTurn.getName() + " Won!\nIt was " + theAnswer.getPerson().getCardName() + " in the " + theAnswer.getRoom().getCardName() + " with the " + theAnswer.getWeapon().getCardName(), "Game Result",  JOptionPane.PLAIN_MESSAGE);
					ClueGame.getGame().setVisible(false);
				} else {
					computerAccusationFlag = false;
					JOptionPane.showMessageDialog(ClueGame.getGame(), whoseTurn.getName() + " made an incorrect accusation of " + computerAccusation.getPerson().getCardName() + " in the " + computerAccusation.getRoom().getCardName() + " with the " + computerAccusation.getWeapon().getCardName(), "Accusation", JOptionPane.PLAIN_MESSAGE);
					currentPlayers.remove(whoseTurnNum + 1);
					computers.remove(whoseTurnNum);
					repaint();
				}
			} else {
				ComputerPlayer currentComputerPlayer = computers.get(whoseTurnNum);
				currentComputerPlayer.move(targets);
				repaint(); // display player's new position
				if (getCell(currentComputerPlayer.getRow(),currentComputerPlayer.getColumn()).isRoom()) {
					guess = currentComputerPlayer.createSuggestion();
					handleSuggestion(guess, currentComputerPlayer);
				} else {
					guess = null;
					whoDisproved = null;
				}
			}
		}
	}

	
	public Solution getGuess() {
		return guess;
	}

	public Player getWhoDisproved() {
		return whoDisproved;
	}
	
	private class clickListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {}
		
		public void clickCheck(MouseEvent e, BoardCell cell) {
			//get the bounds of the cell
			int xCoord1 = cell.getX();
			int xCoord2 = cell.getX() + cellSize;
			int yCoord1 = cell.getY();
			int yCoord2 = cell.getY() + cellSize;
			//if it is a room, you have to move the player to the center of the cell
			if (cell.isRoom()) {
				if ((e.getX() >= xCoord1 && e.getX() <= xCoord2) && (e.getY() >= yCoord1 && e.getY() <= yCoord2)) {
					human.movePlayer(getRoom(cell).getCenterCell().getRow(), getRoom(cell).getCenterCell().getColumn());
					grid[human.getRow()][human.getColumn()].setOccupied(false);
					humanFinished = true;
					highlightTargets = false;
					PopUpDialog suggestionDialog = new SuggestionDialog(human);
					suggestionDialog.setVisible(true);
				}
			//otherwise, move the player to the cell clicked on
			} else {
				if ((e.getX() > xCoord1 && e.getX() < xCoord2) && (e.getY() > yCoord1 && e.getY() < yCoord2)) {
					human.movePlayer(cell.getRow(), cell.getColumn());
					cell.setOccupied(true);
					grid[human.getRow()][human.getColumn()].setOccupied(false);
					humanFinished = true;
					highlightTargets = false;
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (humanFinished) {
				JOptionPane.showMessageDialog(null, "You already moved.", "Error",  JOptionPane.ERROR_MESSAGE);
				return;
			}
			//moves the player to the target clicked, or displays an error message
			for (BoardCell cell: targets) {
				//check every cell in the room for a click
				if (cell.isRoom()) {
					for (BoardCell roomCell: getRoom(cell.getInitial()).getRoomCells()) {
						clickCheck(e, roomCell);
						if (humanFinished) {
							break;
						}
					}
				} else {//check all of the other targets
					clickCheck(e, cell);
					if (humanFinished) {
						break;
					}
				}
			}
			//humanFinished will be false if none of the targets were clicked on
			if (!humanFinished) {
				JOptionPane.showMessageDialog(null, "Invalid Space!", "Error",  JOptionPane.ERROR_MESSAGE);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	public ArrayList<Player> getCurrentPlayers(){
		return currentPlayers;
	}
	
	public ArrayList<Player> getAllPlayers(){
		return allPlayers;
	}

	public ArrayList<Card> getWeapons() {
		return weapons;
	}

	public Map<Character, Room> getAllRooms() {
		return rooms;
	}

}

