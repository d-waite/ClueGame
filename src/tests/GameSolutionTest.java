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
		Solution testSolution = new Solution(keep,axe,queen);
		assertTrue(board.checkAccusation(keep,axe,queen));
		assertFalse(board.checkAccusation(armory, axe, queen));
		assertFalse(board.checkAccusation(keep, axe, human));
		assertFalse(board.checkAccusation(keep, knife, queen));
	}
}
