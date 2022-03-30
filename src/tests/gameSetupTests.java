// Authors: David Waite & Dillinger Day
package tests;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class gameSetupTests {
	private static Board board;

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	@Test
	public void testLoadPlayers() {
		// testing human player
		HumanPlayer human = board.getHumanPlayer();
		assertEquals("You", human.getName());
		assertEquals("Yellow", human.getColor());
		assertEquals(8, human.getRow());
		assertEquals(0, human.getColumn());
		// testing computer players
		ArrayList<ComputerPlayer> computers = board.getComputerPlayers();
		assertEquals(computers.size(), 5);
		assertEquals(computers.get(0).getName(), "King Bartholomew");
		assertEquals("Blue", computers.get(0).getColor());
		assertEquals(0, computers.get(0).getRow());
		assertEquals(5, computers.get(0).getColumn());
		
		assertEquals(computers.get(4).getName(), "Prof. Swords");
		assertEquals("Gray", computers.get(4).getColor());
		assertEquals(28, computers.get(4).getRow());
		assertEquals(8, computers.get(4).getColumn());
	}
	
	@Test
	public void testCards() {
		Solution testSolution = board.getSolution();
		//test that all cards are made
		ArrayList<Card> testDeck = board.getDeck();
		assertEquals(testDeck.size(), 21);
		assertEquals(testDeck.get(0).getCardType(), CardType.ROOM);
		assertEquals(testDeck.get(9).getCardType(), CardType.PERSON);
		assertEquals(testDeck.get(20).getCardType(), CardType.WEAPON);
	}
	
	@test
	public void testSolution() {
		//test solution
				assertEquals(testSolution.getRoom().getCardType(), CardType.ROOM);
				assertEquals(testSolution.getPerson().getCardType(), CardType.PERSON);
				assertEquals(testSolution.getWeapon().getCardType(), CardType.WEAPON);
				int roomMatch = 0;
				int weaponMatch = 0;
				int personMatch = 0;
				for(int i = 0; i < 10; i++) {
					Card testRoom1 = testSolution.getRoom();
					Card testPerson1 = testSolution.getPerson();
					Card testWeapon1 = testSolution.getWeapon();
					board.deal();
					Card testRoom2 = testSolution.getRoom();
					Card testPerson2 = testSolution.getPerson();
					Card testWeapon2 = testSolution.getWeapon();
					if (testRoom1.equals(testRoom2)) {
						roomMatch++;
					}
					if (testWeapon1.equals(testWeapon2)) {
						weaponMatch++;
					}
					if (testPerson1.equals(testPerson2)) {
						personMatch++;
					}
				}
				assertTrue(roomMatch < 4);
				assertTrue(personMatch < 4);
				assertTrue(weaponMatch < 4);
				
	}

}
