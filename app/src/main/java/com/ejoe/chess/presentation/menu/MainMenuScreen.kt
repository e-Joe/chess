package com.ejoe.chess.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.ejoe.chess.R
import com.ejoe.chess.presentation.components.ChessButton
import com.ejoe.chess.presentation.navigation.Screen
import org.koin.androidx.compose.koinViewModel

/**
 * Created by Ilija Vucetic on 11.5.25.
 */

@Composable
fun MainMenuScreen(navController: NavController) {
    val viewModel: MainMenuViewModel = koinViewModel()
    val context = LocalContext.current

    //#region MediaPlayer Setup
    val lifecycleOwner = LocalLifecycleOwner.current

    // Handle lifecycle events for music playback
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.startMusic(context)
                Lifecycle.Event.ON_PAUSE -> viewModel.pauseMusic()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    //#endregion

    //#region UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_chess_kid),
            contentDescription = "Chess Icon",
            modifier = Modifier
                .height(180.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Fit
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            ChessButton(
                onClick = { navController.navigate(Screen.Game.route) },
                text = stringResource(R.string.new_game)
            )
            Spacer(modifier = Modifier.height(16.dp))
            ChessButton(
                onClick = { navController.navigate(Screen.Leaderboard.route) },
                text = stringResource(R.string.leaderboard)
            )
            Spacer(modifier = Modifier.height(16.dp))
            ChessButton(
                onClick = { navController.navigate(Screen.Setting.route) },
                text = stringResource(R.string.settings)
            )
        }
    }
    //#endregion
}