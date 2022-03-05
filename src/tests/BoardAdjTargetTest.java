package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

class BoardAdjTargetTest {

	private static Board board;

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	@Test
	public void adjRoomsDoors() {
		// testing a room cell not in the center
		Set<BoardCell> testAdjList = board.getAdjList(0, 0);
		assertEquals(0, testAdjList.size());
		
		testAdjList = board.getAdjList(13, 8); // testing a doorway
		assertEquals(4, testAdjList.size());
		assertTrue(testAdjList.contains(board.getCell(14, 6)));
		assertTrue(testAdjList.contains(board.getCell(14, 8)));
		assertTrue(testAdjList.contains(board.getCell(13, 9)));
		assertTrue(testAdjList.contains(board.getCell(12, 8)));
		
		testAdjList = board.getAdjList(25, 2); // testing a room center w/ a secret passage
		assertEquals(3, testAdjList.size());
		assertTrue(testAdjList.contains(board.getCell(25, 4)));
		assertTrue(testAdjList.contains(board.getCell(26, 4)));
		assertTrue(testAdjList.contains(board.getCell(2, 12)));
	}
	
	@Test
	public void adjWalkways() {
		Set<BoardCell> testAdjList = board.getAdjList(19, 3); // testing a location surrounded by walkways
		assertEquals(4, testAdjList.size());
		assertTrue(testAdjList.contains(board.getCell(20, 3)));
		assertTrue(testAdjList.contains(board.getCell(18, 3)));
		assertTrue(testAdjList.contains(board.getCell(19, 4)));
		assertTrue(testAdjList.contains(board.getCell(19, 2)));
		
		testAdjList = board.getAdjList(0, 18); // testing north edge
		assertEquals(3, testAdjList.size());
		assertTrue(testAdjList.contains(board.getCell(0, 17)));
		assertTrue(testAdjList.contains(board.getCell(0, 19)));
		assertTrue(testAdjList.contains(board.getCell(1, 18)));
		
		testAdjList = board.getAdjList(8, 0); // testing west edge & cell next to room that is not a doorway
		assertEquals(1, testAdjList.size());
		assertTrue(testAdjList.contains(board.getCell(8, 1)));
		
		testAdjList = board.getAdjList(28, 5); // testing south edge
		assertEquals(2, testAdjList.size());
		assertTrue(testAdjList.contains(board.getCell(28, 4)));
		assertTrue(testAdjList.contains(board.getCell(27, 5)));
		
		testAdjList = board.getAdjList(6, 24); // testing east edge
		assertEquals(2, testAdjList.size());
		assertTrue(testAdjList.contains(board.getCell(5, 24)));
		assertTrue(testAdjList.contains(board.getCell(6, 23)));
	}
	
	@Test
	public void testTargets() {
		
	}
	
	@Test
	public void testOccupied() {
		
	}
}
