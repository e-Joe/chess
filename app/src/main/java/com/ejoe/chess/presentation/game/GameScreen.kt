package com.ejoe.chess.presentation.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ejoe.chess.R
import com.ejoe.chess.domain.logic.CellPosition
import com.ejoe.chess.domain.logic.GameEngine
import com.ejoe.chess.presentation.components.ChessButton
import com.ejoe.chess.util.sound.SoundPlayer
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
@Composable
fun GameScreen(navController: NavController, viewModel: GameViewModel = koinViewModel()) {
    val state = viewModel.state
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    //region Layout Calculation

    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val boardMaxSizeDp = minOf(screenWidthDp, screenHeightDp - 200.dp)
    val cellSizeDp = boardMaxSizeDp / state.boardSize

    //endregion

    var showDialog by remember { mutableStateOf(false) }

    //region Game Event Handling

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is GameEvent.Move -> when (event.feedback) {
                    MoveFeedback.VALID -> SoundPlayer.play(context, R.raw.correct)
                    MoveFeedback.INVALID -> SoundPlayer.play(context, R.raw.wrong_field)
                }

                GameEvent.ShowWinDialog -> showDialog = true
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { SoundPlayer.stop() }
    }

    //endregion

    //region UI

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = state.lightColor.copy(alpha = 0.2f)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {

                // Win dialog when puzzle is successfully solved
                if (showDialog) {
                    WinDialog {
                        viewModel.onAction(GameAction.OnResetClick)
                        showDialog = false
                    }
                }

                // Status: time, queens left, clicks
                GameStatusRow(state.elapsedTime, state.queensLeft, state.clickCount)

                // Interactive chess board
                ChessBoardGrid(
                    boardSize = state.boardSize,
                    queens = state.queens,
                    conflictCells = state.conflictCells,
                    lightColor = state.lightColor,
                    darkColor = state.darkColor,
                    cellSizeDp = cellSizeDp,
                    onCellClick = { row, col ->
                        viewModel.onAction(GameAction.OnCellClick(row, col))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Game control buttons
                GameControlsRow(
                    isPaused = state.isPaused,
                    onReset = { viewModel.onAction(GameAction.OnResetClick) },
                    onPauseToggle = { viewModel.onAction(GameAction.OnPauseToggle) },
                    onFinish = { navController.popBackStack() }
                )
            }
        }
    }

    //endregion
}

//
//region Helper Composables
//

/**
 * Displays game metrics: time, queens left, and number of clicks.
 */
@Composable
private fun GameStatusRow(time: Long, queensLeft: Int, clicks: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(stringResource(R.string.time_s, time), fontSize = 16.sp)
        Text(stringResource(R.string.queens_left, queensLeft), fontSize = 16.sp)
        Text(stringResource(R.string.clicks, clicks), fontSize = 16.sp)
    }
}

/**
 * Renders an interactive n×n chess board.
 */
@Composable
private fun ChessBoardGrid(
    boardSize: Int,
    queens: Set<CellPosition>,
    conflictCells: Map<CellPosition, Boolean>,
    lightColor: Color,
    darkColor: Color,
    cellSizeDp: Dp,
    onCellClick: (Int, Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(boardSize),
        modifier = Modifier.size(cellSizeDp * boardSize)
    ) {
        items(boardSize * boardSize) { index ->
            val row = index / boardSize
            val col = index % boardSize
            val cell = CellPosition(row, col)
            val isQueen = cell in queens
            val isConflict = conflictCells[cell] == true

            val bgColor by animateColorAsState(
                targetValue = when {
                    isConflict -> Color.Red
                    (row + col) % 2 == 0 -> lightColor
                    else -> darkColor
                },
                label = "cellColor"
            )

            Box(
                modifier = Modifier
                    .size(cellSizeDp)
                    .background(bgColor)
                    .clickable { onCellClick(row, col) },
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = isQueen,
                    enter = fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.queen),
                        contentDescription = stringResource(R.string.queen),
                        modifier = Modifier.size(cellSizeDp * 0.6f)
                    )
                }
            }
        }
    }
}

/**
 * Displays control buttons for game reset, pause/resume, and finish.
 */
@Composable
private fun GameControlsRow(
    isPaused: Boolean,
    onReset: () -> Unit,
    onPauseToggle: () -> Unit,
    onFinish: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ChessButton(
            onClick = onReset,
            text = stringResource(R.string.reset),
            modifier = Modifier.weight(1f)
        )
        ChessButton(
            onClick = onPauseToggle,
            text = if (isPaused) stringResource(R.string.resume) else stringResource(R.string.pause),
            modifier = Modifier.weight(1f)
        )
        ChessButton(
            onClick = onFinish,
            text = stringResource(R.string.finish),
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Dialog shown when the user solves the puzzle successfully.
 */
@Composable
private fun WinDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.congratulations)) },
        text = { Text(stringResource(R.string.you_successfully_solved_the_n_queens_puzzle)) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.play_again))
            }
        }
    )
}

//endregion