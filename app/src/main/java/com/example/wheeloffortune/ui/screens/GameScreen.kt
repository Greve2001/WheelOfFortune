package com.example.wheeloffortune.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wheeloffortune.*
import com.example.wheeloffortune.R
import com.example.wheeloffortune.data.GameState
import com.example.wheeloffortune.ui.theme.WheelOfFortuneTheme

/**
 * Game Screen Composable. Main screen for playing THe Wheel of Fortune game
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(
    viewModel: WoFViewModel,
    onGameOver: () -> Unit
) {
    // Initialize variables to use through out the composition
    val uiState = viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var lettersToDisplay = uiState.value.lettersToShow


    // Initial call to display the words, becuase the uiState string is empty
    viewModel.updateLettersToShow()

    // Not sure why i have to make a single time trigger, when it switches screens
    // But if i dont the onGameOver() gets called infinite times, though we should be on another screen
    var once = remember { mutableStateOf(true) }
    // If game is Lost or Won navigate to GameOver screen
    if ((uiState.value.gameState == GameState.WON || uiState.value.gameState == GameState.LOST) && once.value){
        onGameOver()
        once.value = false
    }

    // Main Column
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Absolute
        TopBar(points = uiState.value.points, lives = uiState.value.lives)

        Spacer(modifier = Modifier.height(20.dp))

        // Source of image. Verified copyright free: (https://www.flaticon.com/free-icon/wheel-of-fortune_2006249?related_id=2006249&origin=search)
        Image(painter = painterResource(id = R.drawable.wheel_of_fortune), contentDescription = "", Modifier.width(150.dp))
        Spacer(modifier = Modifier.height(20.dp))

        // Result String
        Text(text = viewModel.getSpinResultString())

        WordDisplay(letters = lettersToDisplay.uppercase())
        Spacer(modifier = Modifier.height(40.dp))

        // IDLE
        if (uiState.value.gameState == GameState.IDLE) {
            Button(
                onClick = {
                    viewModel.spinTheWheel()
                }
            ) {
                Text(text = "Spin the Wheel")
            }
        }

        // INPUTTING
        if (uiState.value.gameState == GameState.INPUTTING)  {
            // Guessed Letters
            GuessedLetters(
                focusRequester = focusRequester,
                guessedLetters = "" //uiState.value.lettersGuessed
            ) {
                if (viewModel.guessLetter(it)){
                    keyboardController?.hide()

                    focusManager.clearFocus()
                }
            }
            SideEffect { // Focus on TextField
                focusRequester.requestFocus()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    WheelOfFortuneTheme {
        val viewModel = remember { WoFViewModel() }
        GameScreen(viewModel, {})
    }
}