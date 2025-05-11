package com.ejoe.chess.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ejoe.chess.data.local.dao.GameResultDao
import com.ejoe.chess.data.local.entity.GameResultEntity

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
@Database(entities = [GameResultEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao
}