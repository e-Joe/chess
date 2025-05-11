package com.ejoe.chess.presentation.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejoe.chess.domain.model.GameResult
import com.ejoe.chess.domain.usecase.GetLeaderboardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for displaying the leaderboard of top game results.
 *
 * Created by Ilija Vucetic on 11.5.25.
 *
 * Responsibilities:
 * - Loads a list of saved [GameResult]s via [GetLeaderboardUseCase]
 * - Sorts results by time to determine ranking
 * - Exposes the results as a read-only [StateFlow] for UI to observe
 */
class LeaderboardViewModel(
    private val getLeaderboardUseCase: GetLeaderboardUseCase
) : ViewModel() {

    private val _results = MutableStateFlow<List<GameResult>>(emptyList())
    val results: StateFlow<List<GameResult>> get() = _results

    init {
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            val sorted = getLeaderboardUseCase().sortedBy { it.timeMillis }
            _results.value = sorted
        }
    }
}