package com.example.wheeloffortune

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.wheeloffortune.data.GameState
import com.example.wheeloffortune.data.WoFUiState
import com.example.wheeloffortune.data.wheelResults
import com.example.wheeloffortune.data.words
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WoFViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WoFUiState())
    val uiState: StateFlow<WoFUiState> = _uiState.asStateFlow()

    fun startGame(){
        fetchRandomWord()
        updateLettersToShow()
    }

    fun fetchRandomWord(){
        _uiState.update { currentState ->
            currentState.copy(
                hiddenWord = words.random()
            )
        }
    }

    fun spinTheWheel(){
        _uiState.update { currentState ->
            currentState.copy(
                pointsPerLetter = wheelResults.random()
            )
        }

        setGameState(GameState.INPUTTING)
        updateLettersToShow()
    }


    fun updateLettersToShow() {
        var lettersGuessed : String = uiState.value.lettersGuessed.lowercase()
        var hiddenWord : String = uiState.value.hiddenWord.lowercase()
        var lettersToDisplay : String = ""

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

    fun guessLetter(char: Char) : Boolean {
        var lettersGuessed : String = uiState.value.lettersGuessed.lowercase()
        val alphabetString : String = "abcdefghijklmnopqrstuvxyz"

        val letter = char.lowercase()[0]

        if (!(alphabetString.contains(letter) && !lettersGuessed.contains(letter)) )
            return false

        addGuessedLetter(letter)
        setGameState(GameState.IDLE)
        return true
    }

    private fun addGuessedLetter(letter: Char){
        // TODO Handle if the guess is incorrect and if everything is now guessed
        _uiState.update { currentState ->
            currentState.copy(
                lettersGuessed = uiState.value.lettersGuessed + letter
            )
        }

        if (isWordFound()) {
            // Game is Won
        }
    }

    private fun isWordFound() : Boolean {
        var lettersGuessed : String = uiState.value.lettersGuessed.lowercase()
        var hiddenWord : String = uiState.value.hiddenWord.lowercase()
        var numLettersFound = 0

        hiddenWord.forEach {
            if (lettersGuessed.contains(it))
                numLettersFound++
        }

        if (numLettersFound >= hiddenWord.length)
            return true

        return false
    }



    fun setGameState(state: GameState){
        _uiState.update { currentState ->
            currentState.copy(
                gameState = state
            )
        }
    }
}