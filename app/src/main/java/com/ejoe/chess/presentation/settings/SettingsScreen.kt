package com.ejoe.chess.presentation.settings


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ejoe.chess.R
import com.ejoe.chess.presentation.components.ChessButton
import com.ejoe.chess.presentation.navigation.Screen
import org.koin.androidx.compose.koinViewModel

/**
 * Created by Ilija Vucetic on 11.5.25.
 */


@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val boardSize by viewModel.boardSize
    val lightColor by viewModel.lightColor
    val darkColor by viewModel.darkColor
    val error by viewModel.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.settings_title), fontSize = 24.sp)

        OutlinedTextField(
            value = boardSize,
            onValueChange = { viewModel.boardSize.value = it },
            label = { Text(stringResource(R.string.board_size_min_4)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = error != null
        )

        if (error != null) {
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }

        Text(stringResource(R.string.choose_board_colors))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            viewModel.colorOptions.forEach { (light, dark) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { viewModel.onSelectColorPair(light to dark) }
                        .padding(8.dp)
                ) {
                    Box(
                        Modifier
                            .size(32.dp)
                            .background(Color(light), shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        Modifier
                            .size(32.dp)
                            .background(Color(dark), shape = CircleShape)
                    )
                    if (light == lightColor && dark == darkColor) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("✓", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        ChessButton(
            onClick = {  navController.popBackStack() },
            text = stringResource(R.string.save)
        )
    }
}