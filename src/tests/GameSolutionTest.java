// Authors: David Waite & Dillinger Day
package tests;

import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class GameSolutionTest {
	private static Board board;
	private Card keep = new Card("Keep",CardType.ROOM);
	private Card armory = new Card("Armory",CardType.ROOM);
	private Card human = new Card("You",CardType.PERSON);
	private Card queen = new Card("The Queen",CardType.PERSON);
	private Card knife = new Card("Knife",CardType.WEAPON);
	private Card axe = new Card("Axe",CardType.WEAPON);
	

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		
	}
	
	@Test
	public void testAccusation() {
		// create known solution
		board.setSolution(new Solution(keep,axe,queen));
		// check a correct accusation
		assertTrue(board.checkAccusation("Keep","Axe","The Queen"));
		// check wrong room
		assertFalse(board.checkAccusation("Armory", "Axe", "The Queen"));
		// check wrong person
		assertFalse(board.checkAccusation("Keep", "Axe", "You"));
		// check wrong weapon
		assertFalse(board.checkAccusation("Keep", "Knife", "The Queen"));
	}
	
	@Test
	public void testDisproveSuggestion() {
		Player player = new HumanPlayer("You", "Blue", 0, 0);
		//add known cards to the player's hand
		player.updateHand(axe);
		player.updateHand(keep);
		player.updateHand(queen);
		//returns null when player doesn't have any of the cards
		assertEquals(player.disproveSuggestion(armory, knife, human), (null));
		//returns card that matches when only one card matches
		assertTrue(player.disproveSuggestion(keep, knife, human).equals(keep));
		assertTrue(player.disproveSuggestion(armory, axe, human).equals(axe));
		assertTrue(player.disproveSuggestion(armory, knife, queen).equals(queen));
		
		//check randomness of return when multiple cards match
		int roomCount = 0;
		int weaponCount = 0;
		int personCount = 0;
		for (int i = 0; i < 20; i++) {
			if (player.disproveSuggestion(axe, keep, queen).equals(axe)) {
				weaponCount ++;
			} else if (player.disproveSuggestion(axe, keep, queen).equals(keep)) {
				roomCount++;
			} else {
				personCount++;
			}
		}
		//make sure every card was returned at least once
		assertTrue(roomCount >= 1);
		assertTrue(weaponCount >= 1);
		assertTrue(personCount >= 1);
	}
}
