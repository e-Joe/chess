package com.ejoe.chess.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val boardSize: Int,
    val timeMillis: Long,
    val clicks: Int,
    val timestamp: Long
)
