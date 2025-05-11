package com.ejoe.chess

import com.ejoe.chess.domain.logic.CellPosition
import com.ejoe.chess.domain.logic.GameEngine
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameEngineTest {

    private lateinit var engine: GameEngine

    @Before
    fun setup() {
        engine = GameEngine(4)
        engine.resetGameState()
    }

    // Verifies the engine initializes with no queens and zero clicks
    @Test
    fun testInitialStateIsEmpty() {
        assertTrue(engine.getQueens().isEmpty())
        assertEquals(0, engine.getClickCount())
    }

    // Ensures placing a queen on a valid cell is successful and state updates correctly
    @Test
    fun testPlacingQueenOnValidCellSucceeds() {
        val success = engine.toggleQueen(CellPosition(0, 0))
        assertTrue(success)
        assertTrue(engine.getQueens().contains(CellPosition(0, 0)))
        assertEquals(1, engine.getClickCount())
    }

    // Ensures placing a queen on an invalid cell (e.g. same row) fails
    @Test
    fun testPlacingQueenOnInvalidCellFails() {
        engine.toggleQueen(CellPosition(0, 0))
        val fail = engine.toggleQueen(CellPosition(0, 1))
        assertFalse(fail)
        assertFalse(engine.getQueens().contains(CellPosition(0, 1)))
    }

    // Verifies that a queen can be removed by toggling its cell again
    @Test
    fun testRemovingPlacedQueenSucceeds() {
        engine.toggleQueen(CellPosition(1, 1))
        val success = engine.toggleQueen(CellPosition(1, 1))
        assertTrue(success)
        assertFalse(engine.getQueens().contains(CellPosition(1, 1)))
    }

    // Confirms that a conflicting cell is correctly identified
    @Test
    fun testConflictingQueenDetectedCorrectly() {
        engine.toggleQueen(CellPosition(0, 0))
        val conflicts = engine.getConflictMap(CellPosition(1, 1))
        assertTrue(conflicts[CellPosition(1, 1)] == true)
    }

    // Confirms that a non-conflicting cell is not marked as conflicting
    @Test
    fun testNonConflictingQueenNotMarked() {
        engine.toggleQueen(CellPosition(0, 0))
        val conflicts = engine.getConflictMap(CellPosition(1, 2))
        assertFalse(conflicts[CellPosition(1, 2)] == true)
    }

    // Tests that a full valid N-Queens solution is detected as a win
    @Test
    fun testCheckWinReturnsTrueForValidSolution() {
        val validSolution = setOf(
            CellPosition(0, 1),
            CellPosition(1, 3),
            CellPosition(2, 0),
            CellPosition(3, 2)
        )
        validSolution.forEach { engine.toggleQueen(it) }
        assertTrue(engine.checkWin(engine.getQueens()))
    }

    // Ensures partial solutions are not incorrectly marked as wins
    @Test
    fun testCheckWinReturnsFalseForPartialSolution() {
        engine.toggleQueen(CellPosition(0, 1))
        engine.toggleQueen(CellPosition(1, 3))
        assertFalse(engine.checkWin(engine.getQueens()))
    }

    // Verifies that the timer increments correctly each time it's called
    @Test
    fun testTimerIncrementsCorrectly() {
        repeat(5) { engine.incrementTimer() }
        assertEquals(5, engine.incrementTimer() - 1)
    }

    // Ensures the click counter updates for each toggle, including removals
    @Test
    fun testClickCountUpdatesCorrectly() {
        engine.toggleQueen(CellPosition(0, 0))
        engine.toggleQueen(CellPosition(0, 0)) // remove
        engine.toggleQueen(CellPosition(1, 1))
        assertEquals(3, engine.getClickCount())
    }

    // Asserts that a known conflict-free configuration indeed has no internal conflicts
    @Test
    fun testNoConflictsInValidPlacement() {
        val safePositions = listOf(
            CellPosition(0, 1),
            CellPosition(1, 3),
            CellPosition(2, 0),
            CellPosition(3, 2)
        )
        safePositions.forEach { engine.toggleQueen(it) }
        val all = engine.getQueens().toList()
        for (i in all.indices) {
            for (j in i + 1 until all.size) {
                assertFalse("Conflict between ${all[i]} and ${all[j]}", engine.inConflict(all[i], all[j]))
            }
        }
    }

    // Tests that a valid 8x8 solution is accepted and marked as a win
    @Test
    fun testLargeBoardAcceptsValidQueenPlacement() {
        val largeEngine = GameEngine(8)
        largeEngine.resetGameState()

        val solution = listOf(
            CellPosition(0, 0), CellPosition(1, 4), CellPosition(2, 7), CellPosition(3, 5),
            CellPosition(4, 2), CellPosition(5, 6), CellPosition(6, 1), CellPosition(7, 3)
        )

        solution.forEach {
            val success = largeEngine.toggleQueen(it)
            assertTrue("Placement should succeed for $it", success)
        }

        assertTrue(largeEngine.checkWin(largeEngine.getQueens()))
        assertEquals(8, largeEngine.getQueens().size)
        assertEquals(8, largeEngine.getClickCount())
    }

    // Confirms that the engine detects invalid placements on large boards
    @Test
    fun testLargeBoardDetectsConflicts() {
        val largeEngine = GameEngine(8)
        largeEngine.resetGameState()

        val queens = listOf(
            CellPosition(0, 0), CellPosition(1, 1), // diagonal conflict
            CellPosition(2, 2), CellPosition(3, 3)
        )

        queens.forEach {
            largeEngine.toggleQueen(it)
        }

        assertFalse(largeEngine.checkWin(largeEngine.getQueens()))
        assertTrue(largeEngine.getConflictMap(CellPosition(1, 1))[CellPosition(1, 1)] == true)
    }

    // Validates that a correct 16x16 solution is accepted and passes win condition
    @Test
    fun testBoardSize16AcceptsValidSolution() {
        val engine16 = GameEngine(16)
        engine16.resetGameState()

        val solution16 = listOf(
            CellPosition(0, 0), CellPosition(1, 2), CellPosition(2, 4), CellPosition(3, 1),
            CellPosition(4, 12), CellPosition(5, 8), CellPosition(6, 13), CellPosition(7, 11),
            CellPosition(8, 14), CellPosition(9, 5), CellPosition(10, 15), CellPosition(11, 6),
            CellPosition(12, 3), CellPosition(13, 10), CellPosition(14, 7), CellPosition(15, 9)
        )

        solution16.forEach {
            val placed = engine16.toggleQueen(it)
            assertTrue("Valid placement failed at $it", placed)
        }

        assertEquals(16, engine16.getQueens().size)
        assertEquals(16, engine16.getClickCount())
        assertTrue(engine16.checkWin(engine16.getQueens()))
    }
}