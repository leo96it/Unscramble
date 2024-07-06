package com.example.unscramble.data

data class GameUiState(
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val wordCount: Int = 1,
    val score: Int = 0,
    val isGameOver: Boolean = false
)
