package nl.bluevoid.dojotictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import nl.bluevoid.dojotictactoe.model.Game
import nl.bluevoid.dojotictactoe.view.GameView
import nl.bluevoid.myapplication.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    private val game by viewModels<Game>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                GameView(game)
            }
        }
        game.run()
    }
}