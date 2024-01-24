package nl.bluevoid.dojotictactoe.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Game(val stepTime: Long = 600L) : ViewModel() {

    val boardFlow = MutableStateFlow(Board())
    val players = listOf(Bot(CellState.Cross), Bot(CellState.Circle))

    private var gameJob: Job? = null

    fun run() {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            while (boardFlow.value.gameStateFlow.value == Board.GameState.Undecided) {
                delay(stepTime)
                nextTurn()
            }
        }
    }

    fun isFinished() = boardFlow.value.gameStateFlow.value != Board.GameState.Undecided

    fun nextTurn() {
        val colorToPlay = boardFlow.value.getTurn()
        players.first { it.color == colorToPlay }.doMove(boardFlow.value)
    }

    fun restart(boardSize: Int) {
        boardFlow.value = Board(boardSize)
        run()
    }
}
