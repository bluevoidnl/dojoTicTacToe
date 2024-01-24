package nl.bluevoid.dojotictactoe.model

abstract class Player(val color: CellState) {
   abstract fun doMove(board:Board)
}