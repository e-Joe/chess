package com.ejoe.chess.presentation.game

import androidx.compose.ui.graphics.Color
import com.ejoe.chess.domain.logic.CellPosition

/**
 * Created by Ilija Vucetic on 10.5.25..
 * 
 */
data class GameState(
    val queens: Set<CellPosition> = emptySet(),
    val boardSize: Int = 4,
    val clickCount: Int = 0,
    val elapsedTime: Long = 0,
    val lightColor: Color = Color(0xFFDCE775),
    val darkColor: Color = Color(0xFF558B2F),
    val conflictCells: Map<CellPosition, Boolean> = emptyMap(),
    val isPaused: Boolean = false
) {
    val queensLeft: Int get() = boardSize - queens.size
}
