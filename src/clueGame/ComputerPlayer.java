package clueGame;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	public Solution createSuggestion() {
		Solution suggestion = new Solution(null, null, null);
		return suggestion;
	}
	
}