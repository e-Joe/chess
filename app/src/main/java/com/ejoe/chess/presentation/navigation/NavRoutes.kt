package com.ejoe.chess.presentation.navigation

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
sealed class Screen(val route: String) {
    data object Menu : Screen("menu")
    data object Game : Screen("game")
    data object Leaderboard : Screen("leaderboard")
    data object Setting : Screen("settings")
}