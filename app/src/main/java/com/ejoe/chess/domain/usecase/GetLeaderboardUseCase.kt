package com.ejoe.chess.domain.usecase

import com.ejoe.chess.domain.model.GameResult
import com.ejoe.chess.domain.repository.GameRepository

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
class GetLeaderboardUseCase(private val repository: GameRepository) {
    suspend operator fun invoke(): List<GameResult> = repository.getLeaderboard()
}