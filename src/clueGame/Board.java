package clueGame;

import java.util.*;
import java.io.*;

import experiment.TestBoardCell;

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
		BoardCell cell = new BoardCell(-1,-1);
		return cell;
	}

	public int getNumRows() {
		return 0;
	}

	public int getNumColumns() {
		return 0;
	}

	public Room getRoom(char c) {
		Room room = new Room("");
		return room;
	}

	public Room getRoom(BoardCell cell) {
		Room room = new Room("");
		return room;
	}

	public void loadSetupConfig(String setupFile) {
		try {
			FileReader input = new FileReader(setupFile);
			Scanner scan = new Scanner(input);
			while (scan.hasNextLine()) {
				String roomLine = scan.nextLine();
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
			FileReader input = new FileReader(layoutFile);
			Scanner scan = new Scanner(input);
			String rowCells = "";
			while (scan.hasNextLine()) {
				numRows++;
				rowCells = scan.nextLine();
			}
			numCols = rowCells.split(",").length;
			
			grid = new BoardCell[numRows][numCols];
		} catch (FileNotFoundException e) {
			System.out.println("Error opening Layout file.");
		}
		
	}


}

