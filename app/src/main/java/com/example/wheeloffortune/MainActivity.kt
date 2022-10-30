package com.example.wheeloffortune

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wheeloffortune.ui.WoFUiState
import com.example.wheeloffortune.ui.theme.WheelOfFortuneTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


private val _uiState = MutableStateFlow(WoFUiState())
val uiState: StateFlow<WoFUiState> = _uiState.asStateFlow()
val viewModel = WoFViewModel()

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

@Composable
fun WheelOfFortune(lettersToDisplay: String) {
    var lettersToDisplay = viewModel.getLettersToShow()

    Log.println(Log.DEBUG, "TEST", "Recompose!")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(points = uiState.value.points, lives = uiState.value.lives)
        WordDisplay(letters = lettersToDisplay)
        Button(onClick = { viewModel.addGuessedLetter("C") }) {
            Text(text = "Spin the Wheel")
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