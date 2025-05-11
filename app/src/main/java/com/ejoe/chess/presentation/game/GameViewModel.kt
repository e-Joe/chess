package com.ejoe.chess.presentation.game

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejoe.chess.domain.logic.CellPosition
import com.ejoe.chess.domain.logic.GameEngine
import com.ejoe.chess.domain.model.GameResult
import com.ejoe.chess.domain.usecase.GetGameSettingsUseCase
import com.ejoe.chess.domain.usecase.SaveGameResultUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * ViewModel for managing the state and logic of the game.
 *
 * Created by Ilija Vucetic on 11.5.25.
 *
 * Responsibilities:
 * - Delegates core game logic to [GameEngine]
 * - Manages UI state via [GameState]
 * - Handles user interactions (cell click, pause, reset)
 * - Tracks time, click count, and emits game events
 */
class GameViewModel(
    private val getGameSettingsUseCase: GetGameSettingsUseCase,
    private val saveGameResultUseCase: SaveGameResultUseCase
) : ViewModel() {

    //region State, Events & Properties

    var state by mutableStateOf(GameState())
        private set

    private val eventChannel = Channel<GameEvent>()
    val events = eventChannel.receiveAsFlow()

    private var timerJob: kotlinx.coroutines.Job? = null
    private lateinit var gameEngine: GameEngine

    //endregion

    //region Init & Settings

    init {
        loadSettings()
    }

    /**
     * Loads user-defined game settings and initializes game state.
     */
    private fun loadSettings() {
        viewModelScope.launch {
            val settings = getGameSettingsUseCase()
            settings?.let {
                gameEngine = GameEngine(it.boardSize)
                state = state.copy(
                    boardSize = it.boardSize,
                    lightColor = Color(it.lightColor),
                    darkColor = Color(it.darkColor)
                )
                gameEngine.resetGameState()
            }
            onAction(GameAction.OnResetClick)
        }
    }

    //endregion

    //region User Actions

    /**
     * Handles all game-related user actions.
     */
    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.OnCellClick -> if (!state.isPaused) toggleCell(action.row, action.col)
            GameAction.OnResetClick -> resetGame()
            GameAction.OnPauseToggle -> togglePause()
        }
    }

    /**
     * Attempts to place or remove a queen on the board.
     * Updates state and emits events based on result.
     */
    private fun toggleCell(row: Int, col: Int) {
        val cell = CellPosition(row, col)
        val isValid = gameEngine.toggleQueen(cell)
        val updatedQueens = gameEngine.getQueens()
        val updatedClicks = gameEngine.getClickCount()

        if (isValid) {
            state = state.copy(
                queens = updatedQueens,
                clickCount = updatedClicks
            )
            emitEvent(MoveFeedback.VALID)
            checkWin()
        } else {
            val newConflicts = gameEngine.getConflictMap(cell)
            state = state.copy(
                queens = updatedQueens,
                clickCount = updatedClicks,
                conflictCells = newConflicts
            )
            emitEvent(MoveFeedback.INVALID)
            viewModelScope.launch {
                delay(500)
                state = state.copy(conflictCells = newConflicts - cell)
            }
        }
    }

    /**
     * Pauses or resumes the game timer.
     */
    private fun togglePause() {
        if (state.isPaused) startTimer() else stopTimer()
        state = state.copy(isPaused = !state.isPaused)
    }

    //endregion

    //region Game Flow

    /**
     * Resets the game to its initial state and restarts the timer.
     */
    private fun resetGame() {
        gameEngine.resetGameState()
        state = state.copy(
            queens = emptySet(),
            conflictCells = emptyMap(),
            clickCount = 0,
            elapsedTime = 0,
            isPaused = false
        )
        startTimer()
    }

    /**
     * Checks whether the current board state satisfies the win condition.
     */
    private fun checkWin() {
        if (gameEngine.checkWin(state.queens)) {
            stopTimer()
            val result = GameResult(
                boardSize = state.boardSize,
                timeMillis = state.elapsedTime * 1000,
                clicks = state.clickCount
            )
            viewModelScope.launch {
                saveGameResultUseCase(result)
                eventChannel.send(GameEvent.ShowWinDialog)
            }
        }
    }

    //endregion

    //region Timer

    /**
     * Starts the in-game timer that tracks elapsed seconds.
     */
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val newElapsed = gameEngine.incrementTimer()
                state = state.copy(elapsedTime = newElapsed)
            }
        }
    }

    /**
     * Stops the in-game timer.
     */
    private fun stopTimer() {
        timerJob?.cancel()
    }

    override fun onCleared() {
        stopTimer()
        super.onCleared()
    }

    //endregion

    //region Events

    /**
     * Emits a move feedback event (VALID or INVALID) to observers.
     */
    private fun emitEvent(feedback: MoveFeedback) {
        viewModelScope.launch {
            eventChannel.send(GameEvent.Move(feedback))
        }
    }

    //endregion
}

/**
 * Represents the result of a user move.
 */
enum class MoveFeedback {
    VALID, INVALID
}
