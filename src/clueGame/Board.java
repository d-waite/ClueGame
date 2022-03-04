package clueGame;

import java.util.*;
import java.io.*;

public class Board {
	private String setupConfigFile;
	private String layoutConfigFile;
	private int numRows = 0;
	private int numCols = 0;
	private static Board theInstance = new Board();
	private BoardCell[][] grid;
	private Map<Character, Room> rooms = new HashMap<Character, Room>();

	private Board() {
		super();
	}

	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {
		loadSetupConfig(setupConfigFile);
		loadLayoutConfig(layoutConfigFile);
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

	public void loadSetupConfig(String setupFile) {
		try {
			FileReader input = new FileReader("data/" + setupFile);
			Scanner scan = new Scanner(input);
			while (scan.hasNextLine()) {
				String roomLine = scan.nextLine();
				if (roomLine.charAt(0) == '/') {
					roomLine = scan.nextLine();
				}
				String[] roomInfo = new String[3];
				roomInfo = roomLine.split(", ");
				Room room = new Room(roomInfo[1]);
				rooms.put(roomInfo[2].charAt(0), room);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error opening Setup file.");
		}
	}

	public void loadLayoutConfig(String layoutFile) {
		try {
			FileReader inputSize = new FileReader("data/" + layoutFile);
			Scanner scanSize = new Scanner(inputSize);
			String rowCells = "";
			while (scanSize.hasNextLine()) {
				numRows++;
				rowCells = scanSize.nextLine();
			}
			numCols = rowCells.split(",").length;
			
			grid = new BoardCell[numRows][numCols];
			
			FileReader inputCells = new FileReader("data/" + layoutFile);
			Scanner scanCells = new Scanner(inputCells);
			
			while (scanCells.hasNextLine()) {
				for (int i = 0; i < numRows; i++) {
					String row = scanCells.nextLine();
					String[] seperatedRow = new String[numCols];
					seperatedRow = row.split(",");
					for (int j = 0; j < numCols; j++) {
						BoardCell cell = new BoardCell(i,j);
						initializeCell(cell, seperatedRow[j]);
						grid[i][j] = cell;
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error opening Layout file.");
		}
		
	}
	
	public void initializeCell(BoardCell cell, String label) {
		cell.setInitial(label.charAt(0));
		if (cell.getInitial() == 'W' || cell.getInitial() == 'X') {
			cell.setRoom(false);
			cell.setLabel(false);
			cell.setRoomCenter(false);
		} else {
			cell.setRoom(true);
			cell.setDoorway(false);
			cell.setDoorDirection(DoorDirection.NONE);
			cell.setRoomCenter(false);
			cell.setLabel(false);
		}
		
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


}

