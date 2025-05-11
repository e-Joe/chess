package com.ejoe.chess.presentation.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejoe.chess.domain.model.GameSettings
import com.ejoe.chess.domain.usecase.GetGameSettingsUseCase
import com.ejoe.chess.domain.usecase.SaveGameSettingsUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the settings of the game.
 *
 * Created by Ilija Vucetic on 11.5.25.
 *
 * Responsibilities:
 * - Loading and saving game settings
 * - Validating user input (board size)
 * - Managing selected color themes
 */
class SettingsViewModel(
    private val getGameSettingsUseCase: GetGameSettingsUseCase,
    private val saveGameSettingsUseCase: SaveGameSettingsUseCase
) : ViewModel() {

    //#region State variables

    /** User-input board size as string to allow validation */
    var boardSize = mutableStateOf("8")

    /** Selected light color for the chessboard */
    var lightColor = mutableStateOf(0xFFDCE775)

    /** Selected dark color for the chessboard */
    var darkColor = mutableStateOf(0xFF558B2F)

    /** Holds any validation error messages */
    var error = mutableStateOf<String?>(null)

    /** Predefined color pair options for selection */
    val colorOptions = listOf(
        0xFFDCE775 to 0xFF558B2F,
        0xFFFFF9C4 to 0xFFF57F17,
        0xFFE1BEE7 to 0xFF6A1B9A
    )

    //#endregion

    //#region Initialization

    init {
        loadSettings()
    }

    /**
     * Loads saved game settings using the use case and updates the state.
     */
    private fun loadSettings() {
        viewModelScope.launch {
            val settings = getGameSettingsUseCase()
            settings?.let {
                boardSize.value = it.boardSize.toString()
                lightColor.value = it.lightColor
                darkColor.value = it.darkColor
            }
        }
    }

    //#endregion

    //#region UI Actions

    /**
     * Updates the selected color pair.
     *
     * @param pair A pair of Long integers representing light and dark colors.
     */
    fun onSelectColorPair(pair: Pair<Long, Long>) {
        lightColor.value = pair.first
        darkColor.value = pair.second
    }

    /**
     * Validates the board size and saves the current settings.
     *
     * @param onSuccess Callback invoked after successful save.
     */
    fun saveSettings(onSuccess: () -> Unit) {
        val size = boardSize.value.toIntOrNull()
        if (size == null || size < 4) {
            error.value = "Board size must be at least 4"
            return
        }

        error.value = null

        viewModelScope.launch {
            saveGameSettingsUseCase(
                GameSettings(
                    boardSize = size,
                    lightColor = lightColor.value,
                    darkColor = darkColor.value
                )
            )
            onSuccess()
        }
    }

    //#endregion
}