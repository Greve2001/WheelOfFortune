package com.example.wheeloffortune.ui

data class WoFUiState(
    var lives: Int = 5,
    var points: Int = 0,

    val hiddenWord: String = "Chicken",
    var lettersGuessed: String = "cn",
)
