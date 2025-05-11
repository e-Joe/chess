package com.ejoe.chess.domain.repository

import com.ejoe.chess.domain.model.GameSettings

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
interface SettingsRepository {
    suspend fun getSettings(): GameSettings?
    suspend fun saveSettings(settings: GameSettings)
}