package nl.bluevoid.dojotictactoe.model

abstract class Player(val color: CellState) {
   abstract suspend fun doMove(board:Board)
}