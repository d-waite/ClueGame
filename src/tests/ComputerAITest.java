// Authors: David Waite & Dillinger Day
package tests;

import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;
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
		//give the player all weapons except the bow in both hand and seen
		testPlayer.updateHand(knife);
		testPlayer.updateHand(axe);
		testPlayer.updateSeen(sword);
		testPlayer.updateSeen(poison);
		testPlayer.updateSeen(spear);
		//give the player all people except the king in both hand and seen
		testPlayer.updateHand(human);
		testPlayer.updateSeen(queen);
		testPlayer.updateSeen(knight);
		testPlayer.updateSeen(prof);
		testPlayer.updateSeen(dr);
		//create a new suggestion
		Solution testSuggestion = testPlayer.createSuggestion();
		//test if the suggested room is the room that the player is currently in
		assertTrue(board.getRoom(board.getCell(testPlayer.getRow(), testPlayer.getColumn())).getName().equals(testSuggestion.getRoom().getCardName()));
		//make sure the computer chose the only options we gave it, the king and the bow
		assertTrue(testSuggestion.getPerson().equals(king));
		assertTrue(testSuggestion.getWeapon().equals(bow));
		
		
		//create a new player
		ComputerPlayer testPlayer2 = new ComputerPlayer("2", "Blue", 0, 1);
		
		//give it only some weapons and people in seen
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
		//check for the randomness of the returned weapon and person in the suggestion
		for (int i = 0; i < 24; i++) {
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
	
	@Test
	public void testSelectTarget() {
		ComputerPlayer testPlayer3 = new ComputerPlayer("Comp","Red",28,4);
		board.calcTargets(board.getCell(28, 4), 3);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(testPlayer3.selectTarget(targets),board.getCell(25, 2));
		
		testPlayer3.updateSeen(lookout);
		int cell1 = 0, cell2 = 0, cell3 = 0, cell4 = 0, cell5 = 0;
		for (int i = 0; i < 20; i++) {
			BoardCell target = testPlayer3.selectTarget(targets);
			if (target == board.getCell(27, 4)) {
				cell1++;
			} else if (target == board.getCell(25, 2)) {
				cell2++;
			} else if (target == board.getCell(25, 4)) {
				cell3++;
			} else if (target == board.getCell(28, 5)) {
				cell4++;
			} else {
				cell5++;
			}		
		}
		
		assertTrue(cell1 >= 1);
		assertTrue(cell2 >= 1);
		assertTrue(cell3 >= 1);
		assertTrue(cell4 >= 1);
		assertTrue(cell5 >= 1);
		
		ComputerPlayer testPlayer4 = new ComputerPlayer("Comp","red",13,17);
		board.calcTargets(board.getCell(13, 17), 1);
		targets = board.getTargets();
		
		cell1 = 0;
		cell2 = 0;
		cell3 = 0;
		cell4 = 0;
		
		for (int i = 0; i < 20; i++) {
			BoardCell target = testPlayer4.selectTarget(targets);
			if (target == board.getCell(14,17)) {
				cell1++;
			} else if (target == board.getCell(12,17)) {
				cell2++;
			} else if (target == board.getCell(13,18)) {
				cell3++;
			} else {
				cell4++;
			}
		}
		
		assertTrue(cell1 >= 1);
		assertTrue(cell2 >= 1);
		assertTrue(cell3 >= 1);
		assertTrue(cell4 >= 1);
		
	}
	
}
