package nl.bluevoid.dojotictactoe.model

abstract class Player(val color: Board.CellState) {
   abstract fun doMove(board:Board)
}