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

		board.calcTargets(board.getCell(13,17), 1); // testing open walkway with a roll of 1
		Set<BoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 4);
		assertTrue(targets.contains(board.getCell(14, 17)));
		assertTrue(targets.contains(board.getCell(12, 17)));
		assertTrue(targets.contains(board.getCell(13, 18)));
		assertTrue(targets.contains(board.getCell(13, 16)));

		board.calcTargets(board.getCell(13,17), 2); // testing same space with a roll of 2
		Set<BoardCell> targets2 = board.getTargets();
		assertEquals(targets2.size(), 6);
		assertTrue(targets2.contains(board.getCell(15, 17)));
		assertTrue(targets2.contains(board.getCell(11, 17)));
		assertTrue(targets2.contains(board.getCell(14, 18)));
		assertTrue(targets2.contains(board.getCell(14, 16)));
		assertTrue(targets2.contains(board.getCell(12, 18)));
		assertTrue(targets2.contains(board.getCell(12, 16)));

		board.calcTargets(board.getCell(0, 5), 4); // testing space where you can only move one direction
		Set<BoardCell> targets3 = board.getTargets();
		assertEquals(targets3.size(), 1);
		assertTrue(targets3.contains(board.getCell(4, 5)));

		board.calcTargets(board.getCell(28, 4), 3); // testing target that lets user go into a room
		Set<BoardCell> targets4 = board.getTargets();
		assertEquals(targets4.size(), 4);
		assertTrue(targets4.contains(board.getCell(25, 2)));
		assertTrue(targets4.contains(board.getCell(25, 4)));
		assertTrue(targets4.contains(board.getCell(28, 5)));
		assertTrue(targets4.contains(board.getCell(26, 5)));

		board.calcTargets(board.getCell(15, 0), 2); // testing targets from room center w/o a secret passage
		Set<BoardCell> targets5 = board.getTargets();
		assertEquals(targets5.size(), 3);
		assertTrue(targets5.contains(board.getCell(14, 2)));
		assertTrue(targets5.contains(board.getCell(15, 3)));
		assertTrue(targets5.contains(board.getCell(16, 2)));

		board.calcTargets(board.getCell(2, 12), 1); // testing targets from room center w/ a secret passage
		Set<BoardCell> targets6 = board.getTargets();
		assertEquals(targets6.size(), 2);
		assertTrue(targets6.contains(board.getCell(6, 14)));
		assertTrue(targets6.contains(board.getCell(25, 2)));
	}

	@Test
	public void testOccupied() {
		board.getCell(5, 9).setOccupied(true); // testing blocking with a small roll
		board.calcTargets(board.getCell(4, 9), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 1);
		assertTrue(targets.contains(board.getCell(3, 9)));
		board.getCell(5, 9).setOccupied(false);
		
		board.getCell(19, 18).setOccupied(true); // testing large roll w/ many occupied spaces
		board.getCell(19, 17).setOccupied(true);
		board.getCell(20, 17).setOccupied(true);
		board.calcTargets(board.getCell(20, 18), 6);
		Set<BoardCell> targets2 = board.getTargets();
		assertEquals(targets2.size(), 5);
		assertTrue(targets2.contains(board.getCell(19, 22)));
		assertTrue(targets2.contains(board.getCell(25, 17)));
		assertTrue(targets2.contains(board.getCell(20, 14)));
		assertTrue(targets2.contains(board.getCell(19, 15)));
		assertTrue(targets2.contains(board.getCell(18, 16)));
		board.getCell(19, 18).setOccupied(false);
		board.getCell(19, 17).setOccupied(false);
		board.getCell(20, 17).setOccupied(false);
	}
}
