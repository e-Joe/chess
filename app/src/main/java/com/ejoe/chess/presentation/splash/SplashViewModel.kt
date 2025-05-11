package com.ejoe.chess.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the splash screen loading state.
 *
 * Created by Ilija Vucetic on 11.5.25.
 *
 * Responsibilities:
 * - Exposes a loading flag [isLoading] to control the splash screen display
 * - Automatically dismisses the splash after a short delay
 */
class SplashViewModel : ViewModel() {
    private val mutableStateFlow = MutableStateFlow(true)
    val isLoading = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            delay(500)
            mutableStateFlow.value = false
        }
    }
}