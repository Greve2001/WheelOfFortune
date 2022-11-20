package com.example.wheeloffortune.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.wheeloffortune.WoFViewModel

@Composable
fun GameOverScreen(
    viewModel: WoFViewModel,
    onRestartGame: () -> Unit
){
    Text(text = "Game Over!")
    Button(onClick = onRestartGame) {
        Text(text = "Restart Game")
    }
}