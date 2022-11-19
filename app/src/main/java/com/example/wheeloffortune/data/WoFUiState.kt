package com.example.wheeloffortune.data

enum class GameState() {
    IDLE, INPUTTING,
}

data class WoFUiState(

    var lives: Int = 5,
    var points: Int = 0,

    val hiddenWord: String = "Chicken",
    var lettersGuessed: String = "",
    var lettersToShow: String = "",

    var gameState: GameState = GameState.IDLE,
)
