package com.example.unscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscramble.composable.GameScreen
import com.example.unscramble.ui.theme.UnscrambleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnscrambleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    UnscrambleApp()
                }
            }
        }
    }
}

@Composable
fun UnscrambleApp(modifier: Modifier = Modifier) {
    GameScreen()
}



@Preview(showBackground = true)
@Composable
fun UnscrambleAppPreview() {
    UnscrambleTheme {
        UnscrambleApp()
    }
}