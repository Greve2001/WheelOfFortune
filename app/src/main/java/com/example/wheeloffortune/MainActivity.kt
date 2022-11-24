package com.example.wheeloffortune

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wheeloffortune.ui.screens.GameOverScreen
import com.example.wheeloffortune.ui.screens.GameScreen
import com.example.wheeloffortune.ui.screens.StartScreen
import com.example.wheeloffortune.ui.theme.WheelOfFortuneTheme

sealed class Screens(val route: String){
    object Start : Screens("startScreen")
    object Game : Screens("gameScreen")
    object GameOver : Screens("gameOverScreen")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WheelOfFortuneTheme {
                WheelOfFortune()
            }
        }
    }
}

@Composable
fun WheelOfFortune(){
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()
        val viewModel = remember { WoFViewModel() }

        NavHost(navController = navController, startDestination = Screens.Start.route){
            composable(Screens.Start.route) {
                StartScreen(
                    viewModel = viewModel,
                    onNavigateToGameScreen = {
                        navController.navigate(Screens.Game.route)
                        viewModel.startGame()
                    }
                )
            }

            composable(Screens.Game.route) {
                GameScreen(
                    viewModel = viewModel,
                    onGameOver = {
                        navController.navigate(Screens.GameOver.route)
                        viewModel.resetGame()
                    }
                )
            }

            composable(Screens.GameOver.route) {
                GameOverScreen(
                    viewModel = viewModel,
                    onRestartGame = { navController.navigate(Screens.Start.route) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FullAppPreview() {
    WheelOfFortuneTheme {
        WheelOfFortune()
    }
}