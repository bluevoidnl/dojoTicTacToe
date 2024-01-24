package nl.bluevoid.dojotictactoe.model

data class BoardCell(val x: Int, val y: Int, var state: CellState = CellState.Empty) {
    fun isEmpty() = state == CellState.Empty
}

enum class CellState { Empty, Cross, Circle }