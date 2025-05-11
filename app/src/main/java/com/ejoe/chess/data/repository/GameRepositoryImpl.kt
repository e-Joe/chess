package com.ejoe.chess.data.repository

import com.ejoe.chess.data.local.dao.GameResultDao
import com.ejoe.chess.data.mapper.toDomain
import com.ejoe.chess.data.mapper.toEntity
import com.ejoe.chess.domain.model.GameResult
import com.ejoe.chess.domain.repository.GameRepository

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
class GameRepositoryImpl(
    private val dao: GameResultDao
) : GameRepository {
    override suspend fun saveGameResult(result: GameResult) {
        dao.insert(result.toEntity())
    }

    override suspend fun getLeaderboard(): List<GameResult> {
        return dao.getAll().map { it.toDomain() }
    }
}