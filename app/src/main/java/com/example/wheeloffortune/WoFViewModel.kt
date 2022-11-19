package com.example.wheeloffortune

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.wheeloffortune.data.WoFUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WoFViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WoFUiState())
    val uiState: StateFlow<WoFUiState> = _uiState.asStateFlow()


    fun updateLettersToShow() {
        var lettersGuessed : String = uiState.value.lettersGuessed
        var hiddenWord : String = uiState.value.hiddenWord
        var lettersToDisplay : String = ""

        Log.println(Log.DEBUG, "TEST", "Letters to show: ${lettersGuessed}")

        hiddenWord.forEach {
            if (lettersGuessed.contains(it))
                lettersToDisplay += it
            else
                lettersToDisplay += " "
        }

        _uiState.update { currentState ->
            currentState.copy(
                lettersToShow = lettersToDisplay
            )
        }
    }


    fun addGuessedLetter(letter: String){
        uiState.value.lettersGuessed += letter
        Log.println(Log.DEBUG, "TEST", "Letters Guessed: ${uiState.value.lettersGuessed}")
    }
}