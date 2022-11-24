package com.example.wheeloffortune.data

enum class GameState() {
    IDLE, INPUTTING, LOST, WON
}

data class WoFUiState(

    var lives: Int = 0,
    var points: Int = 0,

    val hiddenWord: String = "",
    var lettersGuessed: String = "",
    var lettersToShow: String = "",
    var pointsPerLetter: Int = 0,

    var gameState: GameState = GameState.IDLE,
    var categoryKey: String = "",
)
