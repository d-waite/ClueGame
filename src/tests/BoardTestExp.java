package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import experiment.TestBoard;
import experiment.TestBoardCell;

class BoardTestExp {

	private TestBoard board;
	@BeforeEach
	public void setup() {
		board = new TestBoard();
		
	}
	
	@Test
	public void adjListTopLeft() {
		//Test adjacent cells for (0,0)
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}
	
	@Test
	public void adjListBottomRight() {
		//Test adjacent cells for (3,3)
		TestBoardCell cell = board.getCell(3, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(2, testList.size());
	}
	@Test
	public void adjListLeftEdge() {
		//Test adjacent cells for (2,0)
		TestBoardCell cell = board.getCell(2, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(3, 0)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(3, testList.size());
	}

	@Test
	public void adjListRightEdge() {
		//Test adjacent cells for (2,3)
		TestBoardCell cell = board.getCell(2, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(3, 3)));
		assertTrue(testList.contains(board.getCell(1, 3)));
		assertTrue(testList.contains(board.getCell(2, 2)));
		assertEquals(3, testList.size());
	}
	
	@Test
	public void adjListMiddle() {
		//Test adjacent cells for (1,2)
		TestBoardCell cell = board.getCell(1, 2);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(2, 2)));
		assertTrue(testList.contains(board.getCell(1, 3)));
		assertTrue(testList.contains(board.getCell(0, 2)));
		assertTrue(testList.contains(board.getCell(1, 1)));
		assertEquals(4, testList.size());
	}

	@Test
	public void emptyBoardTargetListCorner() {
		//Test target spaces for (0,0) with a roll of 2
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targetSet = board.getTargets();
		assertEquals(3, targetSet.size());
		assertTrue(targetSet.contains(board.getCell(1, 1)));
		assertTrue(targetSet.contains(board.getCell(2, 0)));
		assertTrue(targetSet.contains(board.getCell(0, 2)));
	}
	
	@Test
	public void emptyBoardTargetListMiddle() {
		//Test target spaces for (2,1) with a roll of 3
		TestBoardCell cell = board.getCell(2, 1);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targetSet = board.getTargets();
		assertEquals(8, targetSet.size());
		assertTrue(targetSet.contains(board.getCell(0, 0)));
		assertTrue(targetSet.contains(board.getCell(1, 1)));
		assertTrue(targetSet.contains(board.getCell(2, 0)));
		assertTrue(targetSet.contains(board.getCell(0, 2)));
		assertTrue(targetSet.contains(board.getCell(2, 2)));
		assertTrue(targetSet.contains(board.getCell(1, 3)));
		assertTrue(targetSet.contains(board.getCell(3, 1)));
		assertTrue(targetSet.contains(board.getCell(3, 3)));
	}
	
	
	@Test
	public void occupiedBoardTargetList() {
		//Test target spaces for (0,0) with a roll of 2 and an occupied square at (1,1)
		board.getCell(1, 1).setOccupied(true);
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targetSet = board.getTargets();
		assertEquals(2, targetSet.size());
		assertTrue(targetSet.contains(board.getCell(2, 0)));
		assertTrue(targetSet.contains(board.getCell(0, 2)));

	}
	
	@Test
	public void roomBoardTargetList() {
		//Test target spaces for (0,0) with a roll of 2 and a room at (0,1)
		board.getCell(0, 1).setRoom(true);
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targetSet = board.getTargets();
		assertEquals(3, targetSet.size());
		assertTrue(targetSet.contains(board.getCell(2, 0)));
		assertTrue(targetSet.contains(board.getCell(1, 1)));
		assertTrue(targetSet.contains(board.getCell(0, 1)));
	}
	
	@Test
	public void occupiedRoomBoardTargetList() {
		//Test target spaces for (0,0) with a roll of 2
		TestBoardCell cell = board.getCell(2, 1);
		board.calcTargets(cell, 3);
		//(3,3) is occupied and (0,1) is a room
		Set<TestBoardCell> targetSet = board.getTargets();
		assertEquals(8, targetSet.size());
		assertTrue(targetSet.contains(board.getCell(0, 0)));
		assertTrue(targetSet.contains(board.getCell(0, 1)));
		assertTrue(targetSet.contains(board.getCell(1, 1)));
		assertTrue(targetSet.contains(board.getCell(2, 0)));
		assertTrue(targetSet.contains(board.getCell(0, 2)));
		assertTrue(targetSet.contains(board.getCell(2, 2)));
		assertTrue(targetSet.contains(board.getCell(1, 3)));
		assertTrue(targetSet.contains(board.getCell(3, 1)));
	}
}
