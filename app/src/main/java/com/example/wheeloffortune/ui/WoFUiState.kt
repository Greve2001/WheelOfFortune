package com.example.wheeloffortune.ui

data class WoFUiState(
    var lives: Int = 5,
    var points: Int = 0,

    val hiddenWord: String = "Chickens",
    var lettersGuessed: String = "n",
)
