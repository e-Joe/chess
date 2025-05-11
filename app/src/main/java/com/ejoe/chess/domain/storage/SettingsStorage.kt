package com.ejoe.chess.domain.storage

import com.ejoe.chess.domain.model.GameSettings

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
interface SettingsStorage {
    suspend fun getGameSetting(): GameSettings?
    suspend fun saveGameSettings(info: GameSettings?)
}