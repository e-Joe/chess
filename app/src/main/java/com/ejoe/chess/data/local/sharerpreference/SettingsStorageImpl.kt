package com.ejoe.chess.data.local.sharerpreference

import android.content.SharedPreferences
import com.ejoe.chess.domain.model.GameSettings
import com.ejoe.chess.domain.storage.SettingsStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
class SettingsStorageImpl(
    private val prefs: SharedPreferences
) : SettingsStorage {

    companion object {
        private const val KEY_BOARD_SIZE = "board_size"
        private const val KEY_LIGHT_COLOR = "light_color"
        private const val KEY_DARK_COLOR = "dark_color"
    }

    override suspend fun getGameSetting(): GameSettings? = withContext(Dispatchers.IO) {
        val boardSize = prefs.getInt(KEY_BOARD_SIZE, 4)
        if (boardSize < 4) return@withContext null

        val light = prefs.getLong(KEY_LIGHT_COLOR, 0xFFDCE775)
        val dark = prefs.getLong(KEY_DARK_COLOR, 0xFF558B2F)
        GameSettings(boardSize, light, dark)
    }

    override suspend fun saveGameSettings(info: GameSettings?) = withContext(Dispatchers.IO) {
        if (info == null) return@withContext
        prefs.edit() {
            putInt(KEY_BOARD_SIZE, info.boardSize)
                .putLong(KEY_LIGHT_COLOR, info.lightColor)
                .putLong(KEY_DARK_COLOR, info.darkColor)
        }
    }
}