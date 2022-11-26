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

/**
 * Wheel of Fortune ViewModel
 */
class WoFViewModel : ViewModel() {
    // Setup UIStateFlows
    private val _uiState = MutableStateFlow(WoFUiState())
    val uiState: StateFlow<WoFUiState> = _uiState.asStateFlow()

    // Used internally.
    private var correctGuess = false

    /**
     * Start Game by initializing variables and fetching a random word.
     */
    fun startGame(){
        _uiState.update { currentState ->
            currentState.copy(
                lives = startLives,
                points = 0,
                lettersGuessed = "",
            )
        }
        initial = true

        fetchRandomWord()
        updateLettersToShow()
        setGameState(GameState.IDLE)
    }

    /**
     * Fetch a random word and set in uiState.
     */
    private fun fetchRandomWord(){
        val category: Set<String> = words[uiState.value.categoryKey]!!

        // Save random word in uiState
        _uiState.update { currentState ->
            currentState.copy(
                hiddenWord = category.random(Random(Date().time))
            )
        }
    }

    /**
     * Set category in uiState.
     */
    fun selectCategory(category: String) {
        _uiState.update { currentState ->
            currentState.copy(
                categoryKey = category
            )
        }
    }

    /**
     * Spin the wheel and set a random amount of points per letter in uiState.
     * If hitting '0' treat is a being bankrupt.
     * Update letters to show.
     */
    fun spinTheWheel(){
        // Set points per letter in uiState
        _uiState.update { currentState ->
            currentState.copy(
                pointsPerLetter = wheelResults.random(Random(Date().time))
            )
        }
        // Check if outcome is bankrupt.
        if (uiState.value.pointsPerLetter == 0) { // Bankrupt
            _uiState.update { currentState ->
                currentState.copy(
                    points = 0
                )
            }
        }

        initial = false
        setGameState(GameState.INPUTTING)
        updateLettersToShow()
    }

    /**
     * Update the letters to show on screen, by comparing internal uiState variables
     */
    fun updateLettersToShow() {
        var lettersGuessed : String = uiState.value.lettersGuessed.lowercase()
        var hiddenWord : String = uiState.value.hiddenWord.lowercase()
        var lettersToDisplay : String = ""

        // Calculate string to display on screen.
        // If the letter is guessed show it, if not show a whitespace
        hiddenWord.forEach {
            if (lettersGuessed.contains(it))
                lettersToDisplay += it
            else
                lettersToDisplay += " "
        }

        // Save it in uiState
        _uiState.update { currentState ->
            currentState.copy(
                lettersToShow = lettersToDisplay
            )
        }
    }

    /**
     * Guess a letter. Is to be called when user presses a key
     * If the char is not a letter or already has been pressed, returns false.
     */
    fun guessLetter(letter: String) : Boolean {
        var lettersGuessed : String = uiState.value.lettersGuessed.lowercase()
        val alphabetString : String = "abcdefghijklmnopqrstuvxyz"

        // If nothing is inputted
        if (letter.isEmpty()) return false

        // Because we get a full string from textfield, we take first char and convert it to lower
        // case to avoid issues
        val letter = letter.first().lowercase()[0]

        // Verfiy that the char is in the alphabet and is not already guessed
        if (!(alphabetString.contains(letter) && !lettersGuessed.contains(letter)) )
            return false

        // The letter is valid, so...
        setGameState(GameState.IDLE)
        addGuessedLetter(letter)
        addPoints(letter)

        return true
    }

    /**
     * Called by guessLetter. Is solely responsible for putting the new letter into uiState
     * If all letters are guessed, it updates the game state to WON
     */
    private fun addGuessedLetter(letter: Char){
        // Save new guessed letter in uiState
        _uiState.update { currentState ->
            currentState.copy(
                lettersGuessed = uiState.value.lettersGuessed + letter
            )
        }

        // If word is found set game state to Won
        if (isWordFound()) {
            setGameState(GameState.WON)
        }
    }

    /**
     * Add points to the users point score.
     */
    private fun addPoints(letter: Char){
        var occurences: Int = 0

        // Fetch values for use
        val pointsPerLetter: Int = uiState.value.pointsPerLetter
        val oldPoints: Int = uiState.value.points
        val hiddenWord : String = uiState.value.hiddenWord.lowercase()

        // Count amount of times the letter occurs in the hidden word
        hiddenWord.forEach {
            if (it == letter) occurences++
        }

        // Set points
        _uiState.update { currentState ->
            currentState.copy(
                points = oldPoints + occurences * pointsPerLetter
            )
        }

        // If the guess was incorrect, remove a life
        correctGuess = occurences != 0
        if (!correctGuess)
            removeLive()
    }

    /**
     * Remove a single life from the user
     */
    private fun removeLive(){
        val newLives = uiState.value.lives-1
        _uiState.update { currentState ->
            currentState.copy(
                lives = newLives
            )
        }

        // If there are not lives left, set gamestate as LOST
        if (newLives <= 0){
            setGameState(GameState.LOST)
        }
    }

    /**
     * Checks if the word is found.
     */
    private fun isWordFound() : Boolean {
        val lettersGuessed : String = uiState.value.lettersGuessed.lowercase()
        val hiddenWord : String = uiState.value.hiddenWord.lowercase()
        var numLettersFound = 0

        // Count amount of letters found in hidden word
        hiddenWord.forEach {
            if (lettersGuessed.contains(it))
                numLettersFound++
        }

        // If all letters have been found
        if (numLettersFound >= hiddenWord.length)
            return true

        return false
    }

    /**
     * Funtion that provides a string to display on screen according to what have happened to the spin prior.
     */
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
            if (uiState.value.pointsPerLetter == 0)
                resultString += "\nYou lost all your points"
        }

        return resultString
    }

    /**
     * Set the GameState in uiState
     */
    fun setGameState(state: GameState){
        _uiState.update { currentState ->
            currentState.copy(
                gameState = state
            )
        }
    }

    /**
     * Reset variables used for the game, when the game is over.
     */
    fun resetGame(){
        _uiState.update { currentState ->
            currentState.copy(
                lettersToShow = "",
                categoryKey = ""
            )
        }
    }
}