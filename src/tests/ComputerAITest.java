// Authors: David Waite & Dillinger Day
package tests;

import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
public class ComputerAITest {
	private static Board board;
	private Card keep = new Card("Keep",CardType.ROOM);
	private Card armory = new Card("Armory",CardType.ROOM);
	private Card lookout = new Card("Lookout",CardType.ROOM);
	private Card hall = new Card("Banquet Hall",CardType.ROOM);
	private Card human = new Card("You",CardType.PERSON);
	private Card queen = new Card("The Queen",CardType.PERSON);
	private Card knight = new Card("Sir Knight",CardType.PERSON);
	private Card prof = new Card("Prof. Swords",CardType.PERSON);
	private Card sword = new Card("Broadsword",CardType.WEAPON);
	private Card knife = new Card("Knife",CardType.WEAPON);
	private Card axe = new Card("Axe",CardType.WEAPON);
	private Card poison = new Card("Poison",CardType.WEAPON);

	

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		
	}
	
	@Test
	public void testComputerSuggestion() {
		ComputerPlayer testPlayer = new ComputerPlayer("dsf", "Green", 0, 11);
		assertTrue(board.getRoom(board.getCell(testPlayer.getRow(), testPlayer.getColumn())).getName().equals("Keep"));
	}
	
}
