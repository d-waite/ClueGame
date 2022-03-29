package tests;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import clueGame.Board;
import clueGame.HumanPlayer;
import clueGame.Player;

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
		ArrayList<Player> players = new ArrayList<Player>();
		players = board.getPlayers();
		
		assertEquals(players.size(),6);
		assertTrue(players.get(0).
		assertTrue(players.get(1))
		assertTrue(players.get(5))
	}
}
