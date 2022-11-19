package com.example.wheeloffortune

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


////////// Hidden Word //////////
@Composable
fun WordDisplay(letters: String){
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

@Composable
fun Letter(letter: String){
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

////////// Top Bar //////////
@Composable
fun TopBar(points: Int, lives: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = Color.Cyan),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Points: $points",
            modifier = Modifier.padding(5.dp),
            fontSize = 20.sp
        )
        Text(
            text = "Lives: $lives",
            modifier = Modifier.padding(5.dp),
            fontSize = 20.sp
        )
    }
}