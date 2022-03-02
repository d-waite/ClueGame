package clueGame;

public class Board {
	private String setupConfigFile;
	private String layoutConfigFile;
	
	
	public void initialize() {
		
	}
	
	public static Board getInstance() {
		Board empty = new Board();
		return empty;
	}
	
	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		
	}

	public BoardCell getCell(int i, int j) {
		BoardCell cell = new BoardCell(i,j);
		return cell;
	}

	public int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumColumns() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	public void loadLayoutConfig() {
		// TODO Auto-generated method stub
		
	}
	
	
}

