package com.example.wheeloffortune

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.wheeloffortune.ui.WoFUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WoFViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WoFUiState())
    val uiState: StateFlow<WoFUiState> = _uiState.asStateFlow()


    fun getLettersToShow() : String {
        var lettersGuessed : String = uiState.value.lettersGuessed
        var hiddenWord : String = uiState.value.hiddenWord

        Log.println(Log.DEBUG, "TEST", "Letters Guessed: ${lettersGuessed}")

        var lettersToDisplay : String = ""

        hiddenWord.forEach {
            if (lettersGuessed.contains(it))
                lettersToDisplay += it
            else
                lettersToDisplay += " "
        }

        return lettersToDisplay
    }


    fun addGuessedLetter(letter: String){
        uiState.value.lettersGuessed += letter
        Log.println(Log.DEBUG, "TEST", "Letters Guessed: ${uiState.value.lettersGuessed}")
    }
}