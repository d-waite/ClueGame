package clueGame;

import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	public Solution createSuggestion() {
		//get the instance of the board so that we can access the deck
		Board board = Board.getInstance();
		Random randInt = new Random();
		//create a list of the unseen cards
		ArrayList<Card> unseenPeople = new ArrayList<Card>();
		ArrayList<Card> unseenWeapons = new ArrayList<Card>();

		//loop through the entire deck to check for the unseen cards
		for (int i = 0; i < board.getDeck().size(); i++) {
			boolean seenFlag = false;
			boolean handFlag = false;
			//set the flag to true if the card has been seen
			for (int j = 0; j < getSeen().size(); j++) {
				if (getSeen().get(j).equals(board.getDeck().get(i))) {
					seenFlag = true;
				}
			}
			if (seenFlag) { //skip the rest if the card has been seen
				continue;
			}
			//set the flag to true if the card is in the player's hand
			for (int k = 0; k < getHand().size(); k++) {
				if (getHand().get(k).equals(board.getDeck().get(i))) {
					handFlag = true;
				}
			}
			if (handFlag) { //skip the rest if card is in hand
				continue;
			}

			//If the card is a person, add it to the unseenPeople list
			if (board.getDeck().get(i).getCardType() == CardType.PERSON) {
				unseenPeople.add(board.getDeck().get(i));
			}
			//If the card is a weapon, add it to the unseenWeapons list
			if (board.getDeck().get(i).getCardType() == CardType.WEAPON) {
				unseenWeapons.add(board.getDeck().get(i));
			}
		}
		//Pick a random person from unseen people
		int randPerson = randInt.nextInt(unseenPeople.size());
		Card person = unseenPeople.get(randPerson);
		//Pick a random weapon from unseen weapons
		int randWeapon = randInt.nextInt(unseenWeapons.size());
		Card weapon = unseenWeapons.get(randWeapon);
		//Set the room card to the room you are currently in
		Card room = new Card(board.getRoom(board.getCell(super.getRow(), super.getColumn())).getName(), CardType.ROOM);

		//Create a new solution as the suggestion and return it
		Solution suggestion = new Solution(room, weapon, person);
		return suggestion;
	}


}