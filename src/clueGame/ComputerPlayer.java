package clueGame;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	public Solution createSuggestion() {
		Card card1 = new Card("d", CardType.ROOM);
		Solution suggestion = new Solution(card1, card1, card1);
		return suggestion;
	}
	
}