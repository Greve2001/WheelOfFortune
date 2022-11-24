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
import java.lang.Math.random
import java.util.*
import kotlin.random.Random

const val startLives = 5
var initial = true;

class WoFViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WoFUiState())
    val uiState: StateFlow<WoFUiState> = _uiState.asStateFlow()

    var correctGuess = false

    fun startGame(){
        _uiState.update { currentState ->
            currentState.copy(
                lives = startLives,
                points = 0,
                lettersGuessed = "",
                lettersToShow = "",
                categoryKey = "",
            )
        }
        initial = true

        fetchRandomWord()
        updateLettersToShow()
        setGameState(GameState.IDLE)
    }

    private fun fetchRandomWord(){
        val category: Set<String> = words[uiState.value.categoryKey]!!

        _uiState.update { currentState ->
            currentState.copy(
                hiddenWord = category.random(Random(Date().time))
            )
        }
    }

    fun selectCategory(category: String) {
        _uiState.update { currentState ->
            currentState.copy(
                categoryKey = category
            )
        }
    }

    fun spinTheWheel(){
        _uiState.update { currentState ->
            currentState.copy(
                pointsPerLetter = wheelResults.random(Random(Date().time))
            )
        }
        initial = false
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

    fun guessLetter(letter: String) : Boolean {
        var lettersGuessed : String = uiState.value.lettersGuessed.lowercase()
        val alphabetString : String = "abcdefghijklmnopqrstuvxyz"

        if (letter.isEmpty()) return false

        val letter = letter.first().lowercase()[0]

        if (!(alphabetString.contains(letter) && !lettersGuessed.contains(letter)) )
            return false

        setGameState(GameState.IDLE)
        addGuessedLetter(letter)
        addPoints(letter)

        Log.println(Log.DEBUG, "TEST", "State: ${uiState.value.gameState}")
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
            setGameState(GameState.WON)
        }
    }

    private fun addPoints(letter: Char){
        var occurences: Int = 0

        val pointsPerLetter: Int = uiState.value.pointsPerLetter
        val oldPoints: Int = uiState.value.points
        val hiddenWord : String = uiState.value.hiddenWord.lowercase()

        hiddenWord.forEach {
            if (it == letter) occurences++
        }

        _uiState.update { currentState ->
            currentState.copy(
                points = oldPoints + occurences * pointsPerLetter
            )
        }

        correctGuess = occurences != 0
        if (!correctGuess)
            removeLive()
    }

    private fun removeLive(){
        val newLives = uiState.value.lives-1
        _uiState.update { currentState ->
            currentState.copy(
                lives = newLives
            )
        }

        if (newLives <= 0){
            setGameState(GameState.LOST)
        }
    }

    private fun isWordFound() : Boolean {
        val lettersGuessed : String = uiState.value.lettersGuessed.lowercase()
        val hiddenWord : String = uiState.value.hiddenWord.lowercase()
        var numLettersFound = 0

        hiddenWord.forEach {
            if (lettersGuessed.contains(it))
                numLettersFound++
        }

        if (numLettersFound >= hiddenWord.length)
            return true

        return false
    }


    fun getSpinResultString() : String {
        var resultString: String = ""
        if (uiState.value.gameState == GameState.IDLE){
            if (correctGuess)
                resultString = "You guessed a correct letter!"
            if (!correctGuess && !initial)
                resultString = "You guessed wrong. You lost a life..."
        }
        if (uiState.value.gameState == GameState.INPUTTING){
            resultString = "You landed on ${uiState.value.pointsPerLetter} points!"
        }

        return resultString
    }

    fun setGameState(state: GameState){
        _uiState.update { currentState ->
            currentState.copy(
                gameState = state
            )
        }
    }
}