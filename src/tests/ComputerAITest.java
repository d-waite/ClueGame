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
	private Card dr = new Card("Dr. Leech",CardType.PERSON);
	private Card king = new Card("King Bartholomew",CardType.PERSON);
	private Card sword = new Card("Broadsword",CardType.WEAPON);
	private Card knife = new Card("Knife",CardType.WEAPON);
	private Card axe = new Card("Axe",CardType.WEAPON);
	private Card poison = new Card("Poison",CardType.WEAPON);
	private Card spear = new Card("Spear",CardType.WEAPON);
	private Card bow = new Card("Bow & Arrow",CardType.WEAPON);

	

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		
	}
	
	@Test
	public void testComputerSuggestion() {
		ComputerPlayer testPlayer = new ComputerPlayer("dsf", "Green", 0, 11);
		
		testPlayer.updateHand(knife);
		testPlayer.updateHand(axe);
		testPlayer.updateSeen(sword);
		testPlayer.updateSeen(poison);
		testPlayer.updateSeen(spear);
		
		testPlayer.updateHand(human);
		testPlayer.updateSeen(queen);
		testPlayer.updateSeen(knight);
		testPlayer.updateSeen(prof);
		testPlayer.updateSeen(dr);
		
		Solution testSuggestion = testPlayer.createSuggestion();
		assertTrue(board.getRoom(board.getCell(testPlayer.getRow(), testPlayer.getColumn())).getName().equals(testSuggestion.getRoom()));
		assertTrue(testSuggestion.getPerson().equals(king));
		assertTrue(testSuggestion.getWeapon().equals(bow));
		
		ComputerPlayer testPlayer2 = new ComputerPlayer("2", "Blue", 0, 26); // should be in the lookout
		
		testPlayer2.updateHand(knife);
		testPlayer2.updateHand(axe);
		testPlayer2.updateSeen(sword);

		testPlayer2.updateHand(human);
		testPlayer2.updateSeen(queen);
		testPlayer2.updateSeen(knight);
		
		int poisonCount = 0;
		int spearCount = 0;
		int profCount = 0;
		int drCount = 0;
		
		for (int i = 0; i < 12; i++) {
			Solution testSuggestion2 = testPlayer2.createSuggestion();
			if (testSuggestion2.getPerson().equals(prof)){
				profCount++;
			} else {
				drCount++;
			}
			
			if (testSuggestion2.getWeapon().equals(poison)){
				poisonCount++;
			} else {
				spearCount++;
			}
		}
		
		assertTrue(poisonCount >= 1);
		assertTrue(spearCount >= 1);
		assertTrue(profCount >= 1);
		assertTrue(drCount >= 1);
	}
	
}
