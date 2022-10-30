package com.example.wheeloffortune

import android.util.Log
import androidx.lifecycle.ViewModel

class WoFViewModel : ViewModel() {
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
        //Log.println(Log.DEBUG, "TEST", "Letters Guessed: ${uiState.value.lettersGuessed}")
    }
}