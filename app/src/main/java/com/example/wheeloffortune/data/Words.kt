package com.example.wheeloffortune.data

val categoryFood: Set<String> = setOf(
    "Icecream",
    "Lasagna",
    "Spaghetti",
    "Cookie",
    "Steak"
)

val categoryAnimal: Set<String> = setOf(
    "Cow",
    "Chicken",
    "Anteater",
    "Boar",
    "Weasel"
)

val words: HashMap<String, Set<String>> = hashMapOf(
    "Food" to categoryFood,
    "Animal" to categoryAnimal
)