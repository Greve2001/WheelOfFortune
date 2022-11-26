package com.example.wheeloffortune.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wheeloffortune.WoFViewModel
import com.example.wheeloffortune.data.categoryAnimal
import com.example.wheeloffortune.data.words
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

/**
 * Start Screen Composable. Start screen for choosing category before starting the game
 */
@Composable
fun StartScreen(
    viewModel: WoFViewModel,
    onNavigateToGameScreen: () -> Unit
){
    val uiState = viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Wheel of Fortune",
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Please select a category to play:")
            Spacer(modifier = Modifier.height(10.dp))
            
            words.forEach { category ->
                var selected: Boolean = false
                if (uiState.value.categoryKey == category.key) selected = true

                CategorySelectionBox(
                    category = category.key,
                    selected = selected,
                    onSelect = { viewModel.selectCategory(category.key) }
                )
            }
        }

        Spacer(modifier = Modifier.height(150.dp))

        // Make sure that a category is selected before enabling startGame Button
        var hasSelectedCategory = false
        if (uiState.value.categoryKey != "") hasSelectedCategory = true
        Button(
            onClick = onNavigateToGameScreen,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp),
            enabled = hasSelectedCategory
        ){
            Text(
                text = "Start Game",
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun CategorySelectionBox(
    category: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    val selectedColor = ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    )
    val notSelectedColor = ButtonDefaults.outlinedButtonColors()
    var color = notSelectedColor
    if (selected) color = selectedColor

    OutlinedButton(
        modifier = Modifier.fillMaxWidth(0.4f),
        onClick = onSelect,
        colors = color

    ) {
        Text(text = category)
    }
}