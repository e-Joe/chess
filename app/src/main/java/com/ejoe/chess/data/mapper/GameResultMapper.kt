package com.ejoe.chess.data.mapper

import com.ejoe.chess.data.local.entity.GameResultEntity
import com.ejoe.chess.domain.model.GameResult

/**
 * Created by Ilija Vucetic on 11.5.25.
 */

fun GameResult.toEntity(): GameResultEntity = GameResultEntity(
    id = id,
    boardSize = boardSize,
    timeMillis = timeMillis,
    clicks = clicks,
    timestamp = timestamp
)

fun GameResultEntity.toDomain(): GameResult = GameResult(
    id = id,
    boardSize = boardSize,
    timeMillis = timeMillis,
    clicks = clicks,
    timestamp = timestamp
)