package com.ejoe.chess.presentation.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ejoe.chess.R
import com.ejoe.chess.domain.model.GameResult
import com.ejoe.chess.ui.theme.GreenTransparent
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel = koinViewModel()
) {
    val results by viewModel.results.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = GreenTransparent
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 16.dp)
        ) {
            Text(stringResource(R.string.leaderboard_smiley), fontSize = 24.sp)

            Spacer(modifier = Modifier.height(16.dp))

            if (results.isEmpty()) {
                Text("No results yet.", fontSize = 16.sp)
            } else {
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(stringResource(R.string.place), Modifier.weight(0.8f), fontSize = 14.sp)
                    Text(stringResource(R.string.size), Modifier.weight(0.8f), fontSize = 14.sp)
                    Text(stringResource(R.string.time), Modifier.weight(1f), fontSize = 14.sp)
                    Text(stringResource(R.string.clicks), Modifier.weight(1f), fontSize = 14.sp)
                    Text(stringResource(R.string.date), Modifier.weight(1.6f), fontSize = 14.sp)
                }

                HorizontalDivider()

                LazyColumn {
                    itemsIndexed(results) { index, item ->
                        LeaderboardRow(index + 1, item)
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(place: Int, result: GameResult) {
    val date = remember(result.timestamp) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(result.timestamp))
    }

     fun formatTime(timeMillis: Long): String {
        val totalSeconds = timeMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0)
            String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
        else
            String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text("$place.", Modifier.weight(0.8f), fontSize = 14.sp)
        Text("${result.boardSize}", Modifier.weight(0.8f), fontSize = 14.sp)
        Text(formatTime(result.timeMillis), Modifier.weight(1f), fontSize = 14.sp)
        Text("${result.clicks}", Modifier.weight(1f), fontSize = 14.sp)
        Text(date, Modifier.weight(1.6f), fontSize = 14.sp)
    }
}