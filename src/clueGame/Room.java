package clueGame;

public class Room {

	private String name;
	
	public Room(String name) {
		super();
		this.name = name;
	}

	public String getName() { 
		return "";
	}

	public BoardCell getLabelCell() {
		BoardCell cell = new BoardCell(-1,-1);
		return cell;
	}

	public BoardCell getCenterCell() {
		BoardCell cell = new BoardCell(-1,-1);
		return cell;
	}

}
