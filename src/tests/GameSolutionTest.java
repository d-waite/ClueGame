// Authors: David Waite & Dillinger Day
package tests;

import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Solution;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class GameSolutionTest {
	private static Board board;
	private Card keep = new Card("Keep",CardType.ROOM);
	//private Card armory = new Card("Armory",CardType.ROOM);
	//private Card human = new Card("You",CardType.PERSON);
	private Card queen = new Card("The Queen",CardType.PERSON);
	//private Card knife = new Card("Knife",CardType.WEAPON);
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
}
