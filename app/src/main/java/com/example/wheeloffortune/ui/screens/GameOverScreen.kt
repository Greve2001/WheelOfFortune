package com.example.wheeloffortune.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.wheeloffortune.WoFViewModel

@Composable
fun GameOverScreen(
    viewModel: WoFViewModel,
    onRestartGame: () -> Unit
){
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

        Button(onClick = onRestartGame) {
            Text(text = "Restart Game")
        }
    }
}