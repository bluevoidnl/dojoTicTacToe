package nl.bluevoid.dojotictactoe.model

data class BoardCell(val x: Int, val y: Int) {
    var state = Board.CellState.Empty

    fun isEmpty() = state == Board.CellState.Empty
}