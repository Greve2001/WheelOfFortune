package com.example.wheeloffortune

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.example.wheeloffortune.ui.WoFUiState
import com.example.wheeloffortune.ui.theme.WheelOfFortuneTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
        TopBar(points = uiState.value.points, lives = uiState.value.lives)
        WordDisplay(letters = lettersToDisplay)
        Button(
            onClick = {
                viewModel.addGuessedLetter("C")
                viewModel.updateLettersToShow()
                focusRequester.requestFocus()
            }
        ) {
            Text(text = "Spin the Wheel")
        }
        TextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = "",
            onValueChange = {
                keyboardController?.hide()
                focusManager.clearFocus()
                            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                // unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WheelOfFortuneTheme {
        WheelOfFortune()
    }
}