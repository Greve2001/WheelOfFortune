package com.example.wheeloffortune.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.example.wheeloffortune.WoFViewModel

@Composable
fun StartScreen(
    viewModel: WoFViewModel,
    onNavigateToGameScreen: () -> Unit
){
    Button(
        onClick = onNavigateToGameScreen
    ){
        Text(text = "Navigate to Game")
    }
}