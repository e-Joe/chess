package com.ejoe.chess.data.repository

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
import com.ejoe.chess.domain.model.GameSettings
import com.ejoe.chess.domain.repository.SettingsRepository
import com.ejoe.chess.domain.storage.SettingsStorage

class SettingsRepositoryImpl(
    private val storage: SettingsStorage
) : SettingsRepository {
    override suspend fun getSettings(): GameSettings? = storage.getGameSetting()
    override suspend fun saveSettings(settings: GameSettings) = storage.saveGameSettings(settings)
}