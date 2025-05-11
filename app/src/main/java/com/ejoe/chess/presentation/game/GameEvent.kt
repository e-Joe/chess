package com.ejoe.chess.presentation.game

/**
 * Created by Ilija Vucetic on 10.5.25..
 * 
 */
sealed interface GameEvent {
    data class Move(val feedback: MoveFeedback) : GameEvent
    data object ShowWinDialog : GameEvent
}