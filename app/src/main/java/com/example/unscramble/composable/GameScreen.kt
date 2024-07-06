package com.example.unscramble.composable

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unscramble.data.GameViewModel
import com.example.unscramble.R
import com.example.unscramble.ui.theme.UnscrambleTheme
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
)
{
    /*
    This approach ensures that whenever there is a change in the uiState value,
    recomposition occurs for the composables using the gameUiState value.
     */
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    )
    {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        GameLayout(
            wordCount = gameUiState.wordCount,
            wordToUnscramble = gameUiState.currentScrambledWord,
            wordScrambled = gameViewModel.wordScrambled,
            updateWord = { gameViewModel.updateUserWord(it) },
            onKeyboardDone = { gameViewModel.submit() },
            guessWrong = gameUiState.isGuessedWordWrong,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        GameButtons(
            onSkip = { gameViewModel.skip() },
            onSubmit = { gameViewModel.submit() },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        GameStatus(
            score = gameUiState.score,
            modifier = modifier
                .padding(20.dp)
        )
    }
    if (gameUiState.isGameOver)
        FinalScoreDialog(
            onPlayAgain = { gameViewModel.resetGame() },
            score = gameUiState.score
        )
}


@Composable
private fun GameLayout(
    wordCount: Int,
    wordToUnscramble: String,
    wordScrambled: String,
    updateWord: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    guessWrong: Boolean,
    modifier: Modifier = Modifier
)
{
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = modifier
    )
    {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        )
        {
            Text(
                text = stringResource(id = R.string.word_count, wordCount),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(vertical = 4.dp, horizontal = 10.dp)
                    .align(Alignment.End)
            )
            Text(
                text = wordToUnscramble,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = stringResource(id = R.string.instructions),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
            OutlinedTextField(
                value = wordScrambled,
//                placeholder = {
//                    Text(text = stringResource(id = R.string.enter_your_word))
//                },
                label = {
                    if (guessWrong) {
                        Text(stringResource(R.string.wrong_guess))
                    } else {
                        Text(stringResource(R.string.enter_your_word))
                    }
                },
                onValueChange = { updateWord(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() },
                ),
                shape = MaterialTheme.shapes.large,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
//                  focusedIndicatorColor =  Color.Transparent, //hide the indicator
                ),
                isError = guessWrong,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
private fun GameButtons(
    onSubmit: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
)
{
    Column(
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_medium)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    )
    {
        ElevatedButton(
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.surfaceTint
            ),
            onClick = onSubmit ,
            modifier = Modifier.fillMaxWidth()
        ) {
                Text(
                    text = stringResource(id = R.string.submit),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
        }
        OutlinedButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.skip),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        }
    }
}


@Composable
private fun GameStatus(
    score: Int,
    modifier: Modifier = Modifier
)
{
    Card(modifier = modifier)
    {
        Text(
            text = stringResource(id = R.string.score, score),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .padding(8.dp)
        )
    }
}


@Composable
private fun FinalScoreDialog(
    onPlayAgain: () -> Unit,
    score: Int,
    modifier: Modifier = Modifier
)
{
    val activity = (LocalContext.current as Activity)
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(id = R.string.play_again))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                activity.finish()
            }) {
                Text(text = stringResource(id = R.string.exit))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.congratulations))
        },
        text = {
            Text(text = stringResource(id = R.string.you_scored, score))
        },
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    UnscrambleTheme {
        GameScreen()
    }
}


@Preview(showBackground = true)
@Composable
fun GameLayoutPreview() {
    UnscrambleTheme {
        GameLayout(
            wordCount= 0,
            wordToUnscramble = "scrambleun",
            wordScrambled = "hello",
            updateWord = {},
            onKeyboardDone = {},
            guessWrong = false,
            modifier = Modifier)
    }
}


@Preview(showBackground = true)
@Composable
fun GameButtonsPreview() {
    UnscrambleTheme {
        GameButtons({}, {})
    }
}


@Preview(showBackground = true)
@Composable
fun GameStatusPreview() {
    UnscrambleTheme {
        GameStatus(0)
    }
}


@Preview(showBackground = true)
@Composable
fun FinalScoreDialogPreview() {
    UnscrambleTheme {
        FinalScoreDialog({},7)
    }
}

