// Authors: David Waite & Dillinger Day
package tests;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

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
		// testing human player is loaded in and data is correct
		HumanPlayer human = board.getHumanPlayer();
		assertEquals("You", human.getName());
		assertEquals(Color.yellow, human.getColor());
		assertEquals(8, human.getRow());
		assertEquals(0, human.getColumn());
		// testing computer players are loaded in and data is correct
		ArrayList<ComputerPlayer> computers = board.getComputerPlayers();
		assertEquals(computers.size(), 5);
		assertEquals(computers.get(0).getName(), "King Bartholomew");
		assertEquals(Color.blue, computers.get(0).getColor());
		assertEquals(0, computers.get(0).getRow());
		assertEquals(5, computers.get(0).getColumn());

		assertEquals(computers.get(4).getName(), "Prof. Swords");
		assertEquals(Color.gray, computers.get(4).getColor());
		assertEquals(28, computers.get(4).getRow());
		assertEquals(8, computers.get(4).getColumn());
	}

	@Test
	public void testCards() {
		//test that all cards are made
		ArrayList<Card> testDeck = board.getDeck();
		assertEquals(21, testDeck.size());
		// test that all types of cards are in the deck
		assertEquals(testDeck.get(0).getCardType(), CardType.ROOM);
		assertEquals(testDeck.get(9).getCardType(), CardType.PERSON);
		assertEquals(testDeck.get(20).getCardType(), CardType.WEAPON);
	}

	@Test
	public void testSolution() {
		
		//test solution
		board.deal();
		Solution testSolution = board.getSolution();
		assertEquals(testSolution.getRoom().getCardType(), CardType.ROOM);
		assertEquals(testSolution.getPerson().getCardType(), CardType.PERSON);
		assertEquals(testSolution.getWeapon().getCardType(), CardType.WEAPON);
		// testing randomness of solution
		int roomMatch = 0;
		int weaponMatch = 0;
		int personMatch = 0;
		for(int i = 0; i < 10; i++) { // 11 deals including deal above
			// previous solution
			Card testRoom1 = testSolution.getRoom();
			Card testPerson1 = testSolution.getPerson();
			Card testWeapon1 = testSolution.getWeapon();
			board.deal();
			testSolution = board.getSolution();
			// new solution
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
		// making sure we are not getting the same solution a lot
		assertTrue(roomMatch < 7);
		assertTrue(personMatch < 4);
		assertTrue(weaponMatch < 4);

	}

	@Test
	public void testPlayerHands() {
		HumanPlayer human = board.getHumanPlayer();
		ArrayList<ComputerPlayer> computers = board.getComputerPlayers();
		ArrayList<Card> playerHand = human.getHand();
		//make sure the difference between evenly dividing the hands and the player's hand is one or less
		//minus three because of the cards dealt to solution divided by six because six players
		assertTrue(((board.getDeck().size() - 3) / 6) - playerHand.size() < 2);
		//make sure the difference between evenly dividing the hands and the computer's hand is one or less
		for (int i = 0; i < 5; i++) {
			assertTrue(((board.getDeck().size() - 3) / 6) - computers.get(i).getHand().size() < 2);
		}
		
		HashSet<Card> dealtCards = new HashSet<Card>();
		Solution testSolution = board.getSolution();
		
		//put the solution into the dealt cards
		dealtCards.add(testSolution.getPerson());
		dealtCards.add(testSolution.getWeapon());
		dealtCards.add(testSolution.getRoom());
		
		//test that cards are unique for player
		for (int i = 0; i < playerHand.size(); i++) {
			assertFalse(dealtCards.contains(playerHand.get(i)));
			dealtCards.add(playerHand.get(i));
		}
		//test that cards are unique for computers
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < computers.get(i).getHand().size(); j++) {
				assertFalse(dealtCards.contains(computers.get(i).getHand().get(j)));

				dealtCards.add(computers.get(i).getHand().get(j));
			}
		}
		//check to make sure every card is dealt
		assertEquals(dealtCards.size(), board.getDeck().size());
	}
	
}
