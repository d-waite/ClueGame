package clueGame;

public class Board {
	private String setupConfigFile;
	private String layoutConfigFile;
	private static Board theInstance = new Board();

	private Board() {
		super();
	}

	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {

	}

	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {

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
		Room room = new Room();
		return room;
	}

	public Room getRoom(BoardCell cell) {
		Room room = new Room();
		return room;
	}

	public void loadSetupConfig() {

	}

	public void loadLayoutConfig() {

	}


}

