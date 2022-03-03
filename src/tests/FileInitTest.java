package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

class FileInitTest {
	private static final int NUM_ROWS = 29;
	private static final int NUM_COLS = 25;
	private static final int NUM_DOORS = 18;
	private static final int NUM_ROOMS = 9;

	private static Board board;

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	@Test
	public void testLoadFiles() {
		Set<String> rooms = new HashSet<String>();
		for (int i = 0; i < board.getNumRows(); i++) {
			for (int j = 0; j < board.getNumColumns(); j++) {
				if (board.getRoom(board.getCell(i, j)) != null) {
					rooms.add(board.getRoom(board.getCell(i, j)).getName());
				}
			}
		}
		assertEquals(NUM_ROOMS, rooms.size()); // make sure we loaded in all rooms
		// testing multiple entries in ClueSetup.txt
		assertEquals("Keep", board.getRoom('K').getName() );
		assertEquals("Porch", board.getRoom('P').getName() );
		assertEquals("Throne Room", board.getRoom('T').getName() );
		assertEquals("Dungeon", board.getRoom('D').getName() );
	}

	@Test
	public void testNumColsRows() {
		// making sure dimensions are correct
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLS, board.getNumColumns());
	}

	@Test
	public void testDoorways() {
		// testing directions of doorways
		BoardCell cell = board.getCell(25, 4);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(8, 1);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(18, 18);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(20, 12);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		cell = board.getCell(17, 18); // right next to doorway, making sure edges look good
		assertFalse(cell.isDoorway());
		cell = board.getCell(0, 0); // check a room isn't a doorway
		assertFalse(cell.isDoorway());
	}

	@Test
	public void testNumDoors() {
		int numDoors = 0;
		for (int i = 0; i < board.getNumRows(); i++) {
			for (int j = 0; j < board.getNumColumns(); j++) {
				if (board.getCell(i, j).isDoorway()) { // testing to make sure all doors are loaded in
					numDoors++;
				}
			}
		}
		assertEquals(NUM_DOORS, numDoors);
	}

	@Test
	public void testCellInitials() {
		assertEquals('W', board.getCell(8, 0).getInitial()); // test walkway initial
		assertEquals('S', board.getCell(7, 0).getInitial()); // test room initial
		assertEquals('X', board.getCell(28, 0).getInitial()); // test unused space initial
		assertEquals("L", board.getCell(27, 0).getInitial()); // test initial of a secret doorway
		assertEquals("K", board.getCell(27, 0).getSecretPassage()); // test second initial of a secret doorway
	}

	@Test
	public void testCenterAndLabel() { // testing two rooms for correct center and label cells
		BoardCell cell = new BoardCell(0,0); // check individual cells for being a label and a center
		Room room = board.getRoom(cell); // checking that room label/center is consistent with cell label/center
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isLabel());
		assertFalse(room.getCenterCell() == cell);
		assertFalse(room.getLabelCell() == cell);
		BoardCell label = new BoardCell(2,2);
		assertTrue(label.isLabel());
		assertFalse(label.isRoomCenter());
		assertTrue(room.getLabelCell() == label);
		BoardCell center = new BoardCell(3,2);
		assertTrue(center.isRoomCenter());
		assertFalse(center.isLabel());
		assertTrue(room.getCenterCell() == center);


		BoardCell cell2 = new BoardCell(27,0); // secret passage cell
		Room room2 = board.getRoom(cell2);
		assertFalse(cell2.isRoomCenter());
		assertFalse(cell2.isLabel());
		assertFalse(room2.getCenterCell() == cell2);
		assertFalse(room2.getLabelCell() == cell2);
		BoardCell label2 = new BoardCell(24,1);
		assertTrue(label2.isLabel());
		assertFalse(label2.isRoomCenter());
		assertTrue(room2.getLabelCell() == label2);
		BoardCell center2 = new BoardCell(25,2);
		assertTrue(center2.isRoomCenter());
		assertFalse(center2.isLabel());
		assertTrue(room2.getCenterCell() == center2);
	}

}
