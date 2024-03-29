// Authors: David Waite & Dillinger Day
package tests;

import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ClueGame;
import clueGame.ComputerPlayer;
import clueGame.GameCardPanel;
import clueGame.GameControlPanel;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class GameSolutionTest {
	private static Board board;
	private static ClueGame clue;
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
		clue = new ClueGame();
		clue.setControlPanel(new GameControlPanel());
		clue.setBoard(board);
		clue.setCardPanel(new GameCardPanel());
		clue.setVisible(false);
	}
	
	@Test
	public void testAccusation() {
		// create known solution
		board.setSolution(new Solution(keep,axe,queen));
		// check a correct accusation
		assertTrue(board.checkAccusation(new Solution(keep,axe,queen)));
		// check wrong room
		assertFalse(board.checkAccusation(new Solution(armory,axe,queen)));
		// check wrong person
		assertFalse(board.checkAccusation(new Solution(keep,axe,human)));
		// check wrong weapon
		assertFalse(board.checkAccusation(new Solution(keep,knife,queen)));
	}
	
	@Test
	public void testDisproveSuggestion() {
		Player player = new HumanPlayer("You", "Blue", 0, 0);
		//add known cards to the player's hand
		player.updateHand(axe);
		player.updateHand(keep);
		player.updateHand(queen);
		//returns null when player doesn't have any of the cards
		assertEquals(player.disproveSuggestion(new Solution(armory, knife, human)), (null));
		//returns card that matches when only one card matches
		assertTrue(player.disproveSuggestion(new Solution(keep, knife, human)).equals(keep));
		assertTrue(player.disproveSuggestion(new Solution(armory, axe, human)).equals(axe));
		assertTrue(player.disproveSuggestion(new Solution(armory, knife, queen)).equals(queen));
		
		//check randomness of return when multiple cards match
		int roomCount = 0;
		int weaponCount = 0;
		int personCount = 0;
		for (int i = 0; i < 20; i++) {
			if (player.disproveSuggestion(new Solution(axe, keep, queen)).equals(axe)) {
				weaponCount ++;
			} else if (player.disproveSuggestion(new Solution(axe, keep, queen)).equals(keep)) {
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
	
	@Test
	public void testHandleSuggestion() {
		// initialization
		ArrayList<Player> players = new ArrayList<Player>();
		Player player0 = new HumanPlayer("You","Blue",0,0); 
		Player player1 = new ComputerPlayer("The Queen","Green",1,0);
		Player player2 = new ComputerPlayer("Sir Knight","Red",0,1);
		
		for (Player player: board.getAllPlayers()) { // clear hands so we start fresh for the test
			player.clearHand();
		}
		
		
		// simulate deal to players
		player0.updateHand(armory);
		player0.updateHand(axe);
		player0.updateHand(queen);
		
		player1.updateHand(knight);
		player1.updateHand(human);
		player1.updateHand(knife);
		
		player2.updateHand(keep);
		player2.updateHand(lookout);
		player2.updateHand(sword);
		
		players.add(player0);
		players.add(player1);
		players.add(player2);
		board.setAllPlayers(players); // game has above 3 players now
		
		// test suggestion no one can disprove
		board.setGuess(new Solution(hall,prof,poison));
		assertEquals(board.handleSuggestion(new Solution(hall,prof,poison),player0), null);
		// test suggestion only suggesting player can disprove
		board.setGuess(new Solution(keep, prof, poison));
		assertEquals(board.handleSuggestion(new Solution(keep, prof, poison), player2), null);
		// test suggestion that human disproves & suggestion in which multiple people could disprove
		board.setGuess(new Solution(armory,prof,poison));
		assertTrue(board.handleSuggestion(new Solution(armory,prof,poison), player1).equals(armory));
		board.setGuess(new Solution(keep, knight, axe));
		assertTrue(board.handleSuggestion(new Solution(keep, knight, axe), player0).equals(knight));
	}
	

}
