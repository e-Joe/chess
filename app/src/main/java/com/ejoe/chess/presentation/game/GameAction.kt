package com.ejoe.chess.presentation.game

/**
 * Created by Ilija Vucetic on 10.5.25..
 * 
 */
sealed interface GameAction {
    data class OnCellClick(val row: Int, val col: Int) : GameAction
    data object OnResetClick : GameAction
    data object OnPauseToggle : GameAction
}