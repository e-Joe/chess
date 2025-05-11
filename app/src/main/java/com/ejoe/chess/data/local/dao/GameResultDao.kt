package com.ejoe.chess.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ejoe.chess.data.local.entity.GameResultEntity

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
@Dao
interface GameResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: GameResultEntity)

    @Query("SELECT * FROM game_results ORDER BY timeMillis ASC")
    suspend fun getAll(): List<GameResultEntity>
}