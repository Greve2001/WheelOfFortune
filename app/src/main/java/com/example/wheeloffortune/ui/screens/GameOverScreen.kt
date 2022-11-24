package com.example.wheeloffortune.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wheeloffortune.WoFViewModel
import com.example.wheeloffortune.data.GameState

@Composable
fun GameOverScreen(
    viewModel: WoFViewModel,
    onRestartGame: () -> Unit
){
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        // H1
        Text(
            text = "Game Over!",
            fontSize = 40.sp
        )

        if (uiState.value.gameState == GameState.WON) {
            Text(text = "You have Won!")
            Text(text = "You won with ${uiState.value.points} points")
        }
        if (uiState.value.gameState == GameState.LOST) {
            Text(text = "You have Lost!")
            Text(text = "You lost with ${uiState.value.points} points")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "The word was: ${uiState.value.hiddenWord}")
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = onRestartGame) {
            Text(text = "Restart Game")
        }
    }
}