package com.example.wheeloffortune

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * Word Display of a box for each letter in a grid
 */
@Composable
fun WordDisplay(letters: String){
    // Calculate grid width to make it be centered on screen
    var gridWidth = letters.toList().count() * 40
    if (gridWidth > 380) gridWidth = 380

    LazyVerticalGrid(
        columns = GridCells.Adaptive(40.dp),
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.width(gridWidth.dp)
    ) {
        items(letters.toList()) {
            Letter(letter = it.toString())
        }
    }
}

/**
 * Letter box used by WordDisplay
 */
@Composable
private fun Letter(letter: String){
    var modifier = Modifier
        .height(50.dp)
        .width(40.dp)
        .padding(5.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(color = Color.LightGray)

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Text(text = letter)
    }
}

/**
 * Topbar to show game variables: points and lives.
 */
@Composable
fun TopBar(points: Int, lives: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        val color = MaterialTheme.colorScheme.onPrimary
        Text(
            text = "Points: $points",
            modifier = Modifier.padding(5.dp),
            fontSize = 20.sp,
            color = color
        )
        Text(
            text = "Lives: $lives",
            modifier = Modifier.padding(5.dp),
            fontSize = 20.sp,
            color = color
        )
    }
}

/**
 * Outlined textfield, that the user has to input a single letter into at a time.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessedLetters(
    focusRequester: FocusRequester,
    guessedLetters: String,
    onValueChange: (String) -> Unit
){
    TextField(
        value = guessedLetters,
        onValueChange = onValueChange,
        modifier = Modifier.focusRequester(focusRequester),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
        )
    )
    Text(text = "Guess a letter") // Textfield Description
}