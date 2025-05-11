package com.ejoe.chess.domain.model

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
data class GameResult(
    val id: Int = 0,
    val boardSize: Int,
    val timeMillis: Long,
    val clicks: Int,
    val timestamp: Long = System.currentTimeMillis()
)
