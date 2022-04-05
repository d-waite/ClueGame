package clueGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

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

	public BoardCell selectTarget(Set<BoardCell> targets) {		
		Board board = Board.getInstance();
		ArrayList<BoardCell> unseenRooms = new ArrayList<BoardCell>(); // for choosing room if not seen
		ArrayList<BoardCell> targetsArray = new ArrayList<BoardCell>(); // since don't have indices for sets
		// check for any rooms in target list, if not seen add to unseen list
		for (BoardCell target: targets) {
			targetsArray.add(target);
			if (target.isRoom()) {
				boolean seenRoom = false;
				for (int i = 0; i < getHand().size(); i++) {
					if (board.getRoom(target).getName().equals(getHand().get(i).getCardName())) { // room is in hand
						seenRoom = true;
					}
				}
				for (int i = 0; i < getSeen().size(); i++) {
					if (board.getRoom(target).getName().equals(getSeen().get(i).getCardName())) { // room has been seen
						seenRoom = true;
					}
				}
				// if room card not in hand or shown to player through a suggestion, add to list of targets we want to go to
				if (!seenRoom) {
					unseenRooms.add(target);
				}
			}
		}
		
		Random rand = new Random();
		if (unseenRooms.size() != 0) { // if there are unseen rooms, choose a random one
			int randomRoom = rand.nextInt(unseenRooms.size()); // if size is 1, will always return 0 so we always choose that room
			return unseenRooms.get(randomRoom);
		} else { // if there are no unseen rooms, just choose a random target
			int randomTarget = rand.nextInt(targets.size());
			return targetsArray.get(randomTarget);
		}
	}

}