package com.ejoe.chess.domain.model

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
data class GameSettings(
    val boardSize: Int = 8,
    val lightColor: Long = 0xFFDCE775,
    val darkColor: Long = 0xFF558B2F
)
