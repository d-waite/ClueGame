package clueGame;

import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	public Solution createSuggestion() {
		Random randInt = new Random();
		Board board = Board.getInstance();
		ArrayList<Card> unseenPeople = new ArrayList<Card>();
		ArrayList<Card> unseenWeapons = new ArrayList<Card>();
		
		for (int i = 0; i < board.getDeck().size(); i++) {
			boolean seenFlag = false;
			boolean handFlag = false;
			for (int j = 0; j < getSeen().size(); j++) {
				if (getSeen().get(j).equals(board.getDeck().get(i))) {
						seenFlag = true;
				}
			}
			if (seenFlag) {
				continue;
			}
			for (int k = 0; k < getHand().size(); k++) {
				if (getHand().get(k).equals(board.getDeck().get(i))) {
					handFlag = true;
				}
			}
			if (handFlag) {
				continue;
			}
			
			if (board.getDeck().get(i).getCardType() == CardType.PERSON) {
				unseenPeople.add(board.getDeck().get(i));
			} else if (board.getDeck().get(i).getCardType() == CardType.WEAPON) {
				unseenWeapons.add(board.getDeck().get(i));
			}
		}
		int randPerson = randInt.nextInt(unseenPeople.size());
		Card person = unseenPeople.get(randPerson);
		int randWeapon = randInt.nextInt(unseenWeapons.size());
		Card weapon = unseenWeapons.get(randWeapon);
		Card room = new Card(board.getRoom(board.getCell(super.getRow(), super.getColumn())).getName(), CardType.ROOM);
		Solution suggestion = new Solution(room, weapon, person);
		
		
		
		return suggestion;
	}
	
	
}