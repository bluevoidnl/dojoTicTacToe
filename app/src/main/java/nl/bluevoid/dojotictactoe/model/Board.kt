package nl.bluevoid.dojotictactoe.model

import kotlinx.coroutines.flow.MutableStateFlow

class Board(val size: Int = 3) {

    enum class CellState { empty, cross, circle }

    enum class GameState { undecided, winCross, winCircle, pat }

    private var boardRows: MutableList<MutableList<CellState>> = MutableList(size) { y ->
        MutableList(size) { x -> CellState.empty }
    }

    val boardFlow = MutableStateFlow<List<List<CellState>>>(boardRows)

    fun setCell(x: Int, y: Int, state: CellState) {
        checkValidMove(x, y, state)
        boardRows[y][x] = state
    }

    private fun checkValidMove(x: Int, y: Int, state: CellState) {
        if (x < 0 || y < 0 || x >= size || y >= size) {
            throw IllegalArgumentException("cell is not in bounds 0..$size: $x, $y")
        }
        if (boardRows[y][x] != CellState.empty) {
            throw IllegalArgumentException("cell  $x, $y is not empty")
        }
        if (state == CellState.empty) {
            throw IllegalArgumentException("can not set cell to empty")
        }
    }


}