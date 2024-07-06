package com.example.unscramble.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


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

    private lateinit var currentWord: String //word already unscrambled
    private var usedWords : MutableSet<String> = mutableSetOf()

    var wordScrambled by mutableStateOf("")
        private set



    init {
        resetGame()
    }

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
        _uiState.value = GameUiState(
            currentScrambledWord = pickRandomWordAndShuffle(),
        )
        updateUserWord(word = "")
    }


    fun updateUserWord(word: String)
    {
        wordScrambled = word
    }


    private fun updateScore()
    {
        // User's guess is correct, increase the score
        _uiState.update { score ->
            score.copy(
                score = _uiState.value.score.plus(SCORE_INCREASE)
            )
        }
    }


    private fun updateGame()
    {
        skip()
        updateScore()
    }


    /*
        Use the copy() function to copy an object, allowing you to alter some
        of its properties while keeping the rest unchanged
     */
    fun submit() {
        if (wordScrambled.equals(currentWord, ignoreCase = true))
            updateGame()
        else
            _uiState.update { guessState ->
                guessState.copy(
                    isGuessedWordWrong = true
                )
            }
    }

    fun skip()
    {
        val wordCount = _uiState.value.wordCount
        if(usedWords.size < MAX_NO_OF_WORDS && wordCount < 10)
        {
            _uiState.update { skip ->
                skip.copy(
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    isGuessedWordWrong = false,
                    wordCount = skip.wordCount.inc()
                )
            }
            updateUserWord("")
        }
        else
        {
            _uiState.update { gameOver ->
                gameOver.copy(
                    isGuessedWordWrong = false,
                    isGameOver = true
                )
            }
        }

    }
}