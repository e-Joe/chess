package com.ejoe.chess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ejoe.chess.ui.theme.ChessTryTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ejoe.chess.domain.logic.GameEngine
import com.ejoe.chess.presentation.navigation.NavHostController
import com.ejoe.chess.presentation.splash.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Ilija Vucetic on 11.5.25.
 * This activity handles:
 * - Splash screen integration
 * - Dependency injection for the splash view model
 * - Setting up the UI content using Jetpack Compose
 */
class MainActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }
        setContent {
            ChessTryTheme {
                NavHostController()
            }
        }
    }
}