package com.ejoe.chess.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ejoe.chess.presentation.game.GameScreen
import com.ejoe.chess.presentation.leaderboard.LeaderboardScreen
import com.ejoe.chess.presentation.menu.MainMenuScreen
import com.ejoe.chess.presentation.settings.SettingsScreen

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
@Composable
fun NavHostController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Menu.route) {
        composable(Screen.Menu.route) { MainMenuScreen(navController) }
        composable(Screen.Game.route) { GameScreen(navController) }
        composable(Screen.Setting.route) { SettingsScreen(navController) }
        composable(Screen.Leaderboard.route) { LeaderboardScreen(navController) }
    }
}