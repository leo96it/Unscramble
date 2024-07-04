package com.example.unscramble.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


/*
    A StateFlow can be exposed from the GameUiState so that the composables can
    listen for UI state updates and make the screen state survive configuration changes.
 */

class GameViewModel: ViewModel()
{
    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
//    val uiState: StateFlow<GameUiState>
//        get() = _uiState.asStateFlow()
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()


    init {
        //resetGame()
    }

    private lateinit var currentWord: String
    private var usedWords : MutableSet<String> = mutableSetOf()

    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        return if (usedWords.contains(currentWord)) {
            pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            shuffleWord(currentWord)
        }
    }

    private fun shuffleWord(word: String): String
    {
        var wordToShuffle = word.toCharArray()

        // Scramble the word
        while (String(wordToShuffle).equals(word)) {
            wordToShuffle.shuffle()
        }
        return String(wordToShuffle)
    }

    fun resetGame()
    {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }
}