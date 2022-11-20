package com.example.wheeloffortune.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(
    viewModel: WoFViewModel,
    onGameOver: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    var lettersToDisplay = uiState.value.lettersToShow
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Initial
    viewModel.updateLettersToShow()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Absolute
        TopBar(points = uiState.value.points, lives = uiState.value.lives)

        Spacer(modifier = Modifier.height(20.dp))
        Image(painter = painterResource(id = R.drawable.wheel_of_fortune), contentDescription = "", Modifier.width(150.dp))
        Spacer(modifier = Modifier.height(20.dp))
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
            // Result String
            val resultString = "You landed on ${uiState.value.pointsPerLetter} points!"
            Text(text = resultString)

            Spacer(modifier = Modifier.height(40.dp))

            // Guessed Letters
            GuessedLetters(
                focusRequester = focusRequester,
                guessedLetters = uiState.value.lettersGuessed
            ) {
                if (viewModel.guessLetter(it.last())){
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            }
            SideEffect { // Focus on TextField
                focusRequester.requestFocus()
            }

            Spacer(modifier = Modifier.height(80.dp))

            DescriptionBox(text = "Please input a letter. You will be rewarded X-times " +
                    "the points for each letter in the hidden word")
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