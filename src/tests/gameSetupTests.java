// Authors: David Waite & Dillinger Day
package tests;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import clueGame.Board;
import clueGame.ComputerPlayer;
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
}
