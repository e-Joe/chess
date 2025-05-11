package com.ejoe.chess.domain.repository

import com.ejoe.chess.domain.model.GameResult

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
interface GameRepository {
    suspend fun saveGameResult(result: GameResult)
    suspend fun getLeaderboard(): List<GameResult>
}