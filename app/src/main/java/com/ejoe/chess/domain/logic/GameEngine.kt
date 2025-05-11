package com.ejoe.chess.domain.logic

/**
 * Created by Ilija Vucetic on 10.5.25..
 * Copyright (c) 2025 Aktiia. All rights reserved.
 * Core game logic engine for the game.
 * Tracks board state, queen positions, timer, and click count.
 */
class GameEngine(private val boardSize: Int) {

    private var queens: Set<CellPosition> = emptySet()
    private var elapsedTime: Long = 0L
    private var clickCount: Int = 0

    /**
     * Resets the game state: clears queens, resets timer and click count.
     */
    fun resetGameState() {
        queens = emptySet()
        elapsedTime = 0L
        clickCount = 0
    }

    /**
     * Attempts to toggle a queen on the given cell.
     *
     * @param cell The position on the board to place or remove a queen.
     * @return `true` if the move was successful (either placed or removed), `false` if invalid.
     */
    fun toggleQueen(cell: CellPosition): Boolean {
        clickCount++
        return if (queens.contains(cell)) {
            queens = queens - cell
            true
        } else if (isValidMove(queens, cell)) {
            queens = queens + cell
            true
        } else {
            false
        }
    }

    /**
     * @return A set of all currently placed queens.
     */
    fun getQueens(): Set<CellPosition> = queens

    /**
     * @return The number of cell interactions (clicks) made by the player.
     */
    fun getClickCount(): Int = clickCount

    /**
     * Increments the internal timer counter.
     * @return The updated elapsed time in seconds.
     */
    fun incrementTimer(): Long {
        elapsedTime++
        return elapsedTime
    }

    /**
     * Checks if the current queen configuration is a valid N-Queens solution.
     *
     * @param queens A set of queen positions to evaluate.
     * @return `true` if it's a valid solution, `false` otherwise.
     */
    fun checkWin(queens: Set<CellPosition>): Boolean {
        return queens.size == boardSize && queens.all { q1 ->
            queens.none { q2 -> q1 != q2 && inConflict(q1, q2) }
        }
    }

    /**
     * Returns a map indicating if the given cell conflicts with any existing queen.
     *
     * @param conflictCell The cell to check for conflicts.
     * @return A map with the cell as key and `true` if it conflicts.
     */
    fun getConflictMap(conflictCell: CellPosition): Map<CellPosition, Boolean> {
        return mapOf(conflictCell to isInConflict(queens, conflictCell))
    }

    /**
     * Checks if placing a queen at the given position would be valid.
     *
     * @param queens The current queen positions.
     * @param pos The candidate cell to validate.
     * @return `true` if no conflicts are found, `false` otherwise.
     */
    private fun isValidMove(queens: Set<CellPosition>, pos: CellPosition): Boolean {
        return queens.none { inConflict(it, pos) }
    }

    /**
     * Determines if the given position is in conflict with any existing queens.
     *
     * @param queens The current queen positions.
     * @param pos The position to evaluate.
     * @return `true` if it is in conflict, `false` otherwise.
     */
    private fun isInConflict(queens: Set<CellPosition>, pos: CellPosition): Boolean {
        return queens.any { inConflict(it, pos) }
    }

    /**
     * Checks if two given positions are in conflict based on N-Queens rules.
     *
     * @param a The first queen's position.
     * @param b The second queen's position.
     * @return `true` if they are in the same row, column, or diagonal.
     */
    fun inConflict(a: CellPosition, b: CellPosition): Boolean {
        return a.row == b.row ||
                a.col == b.col ||
                (a.row - a.col) == (b.row - b.col) ||
                (a.row + a.col) == (b.row + b.col)
    }
}