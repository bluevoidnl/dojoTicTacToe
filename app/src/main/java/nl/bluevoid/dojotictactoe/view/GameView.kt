package nl.bluevoid.dojotictactoe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nl.bluevoid.dojotictactoe.model.Board
import nl.bluevoid.dojotictactoe.model.BoardCell
import nl.bluevoid.dojotictactoe.model.CellState
import nl.bluevoid.dojotictactoe.model.Game

@Composable
fun GameView(game: Game) {

    val board = game.boardFlow.collectAsState().value

    val cellRows = board.boardFlow.collectAsState(emptyList()).value
    val gameState = board.gameStateFlow.collectAsState(Board.GameState.Undecided).value
    val count = board.moveCounterFlow.collectAsState(0).value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()) {

        Text("Tic tact toe")

        val stateText = when (gameState) {
            Board.GameState.Undecided -> "Undecided"
            Board.GameState.WinCircle -> "Circle wins!"
            Board.GameState.WinCross -> "Cross wins!"
            Board.GameState.Draw -> "Its a draw!"
        }
        Text(stateText)

        BoxWithConstraints {
            Board(count, cellRows, this.maxWidth)
        }

        Row() {
            repeat(3) {
                val size = 3 + it
                Button({ game.restart(size) }) {
                    Text("Start $size")
                }
            }
        }
    }
}

@Composable
private fun Board(count: Int, cellRows: List<List<BoardCell>>, maxWidth: Dp) {
    Column() {
        val cells = cellRows.size
        val cellSize = (maxWidth - 40.dp) / cells
        key(count) {
            cellRows.forEachIndexed { index, row ->
                if (index > 0) Box(modifier = Modifier
                    .height(1.dp)
                    .width(cellSize * row.size)
                    .background(Color.Black))
                Row() {
                    row.forEachIndexed { index, cell ->
                        Cell(cell, index, cellSize)
                    }
                }
            }
        }
    }
}

@Composable
private fun Cell(cell: BoardCell, index: Int, cellSize: Dp) {
    val icon = when (cell.state) {
        CellState.Cross -> Icons.Filled.Close
        CellState.Circle -> Icons.Filled.Face
        CellState.Empty -> null
    }
    if (index > 0) Box(modifier = Modifier
        .width(1.dp)
        .height(cellSize)
        .background(Color.Black))
    if (icon == null) {
        Box(modifier = Modifier.size(cellSize))
    } else {
        Icon(
            imageVector = icon,
            contentDescription = "cell",
            tint = Color.Magenta,
            modifier = Modifier.size(cellSize)
            // .clickable { hexViewModel.soundLevelFlow.value = if (soundLevel == 0f) 1f else 0f }
        )
    }
}