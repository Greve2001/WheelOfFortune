package com.example.wheeloffortune

import android.util.Log
import androidx.lifecycle.ViewModel

class WoFViewModel : ViewModel() {
    fun getLettersToShow() : String {
        var lettersGuessed : List<String> = uiState.value.lettersGuessed.split("")
        var hiddenWord : List<String> = uiState.value.hiddenWord.split("")
        lettersGuessed = lettersGuessed.subList(1, lettersGuessed.lastIndex)
        hiddenWord = hiddenWord.subList(1, hiddenWord.lastIndex)

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