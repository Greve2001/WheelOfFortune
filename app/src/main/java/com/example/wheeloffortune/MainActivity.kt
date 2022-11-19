package com.example.wheeloffortune

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wheeloffortune.data.GameState
import com.example.wheeloffortune.ui.theme.WheelOfFortuneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WheelOfFortuneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WheelOfFortune()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun WheelOfFortune() {
    // TODO all viewModel calls be needs to be from one source of truth

    val viewModel = remember { WoFViewModel() }
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

        WordDisplay(letters = lettersToDisplay.uppercase())

        Spacer(modifier = Modifier.height(40.dp))
        
        if (uiState.value.gameState == GameState.IDLE) {
            Button(
                onClick = {
                    viewModel.spinTheWheel()
                }
            ) {
                Text(text = "Spin the Wheel")
            }
        }

        if (uiState.value.gameState == GameState.INPUTTING)  {
            // Result String
            val resultString = "You landed on ${uiState.value.pointsPerLetter} points!"
            Text(text = resultString)

            Spacer(modifier = Modifier.height(40.dp))

            // Guessed Letters
            TextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = uiState.value.lettersGuessed,
                onValueChange = {
                    if (viewModel.guessLetter(it.last())){
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    } },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    // unfocusedIndicatorColor = Color.Transparent
                )
            )
            focusRequester.requestFocus() // Focus on TextField
            Text(text = "Letters Guessed") // Textfield Description

            Spacer(modifier = Modifier.height(80.dp))

            // Description / Instructions
            var description: String = "Please input a letter. You will be rewarded X-times " +
                    "the points for each letter in the hidden word"
            Text(text = description)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WheelOfFortuneTheme {
        WheelOfFortune()
    }
}