package com.ejoe.chess.domain.usecase

import com.ejoe.chess.domain.model.GameSettings
import com.ejoe.chess.domain.repository.SettingsRepository

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
class GetGameSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): GameSettings? = repository.getSettings()
}