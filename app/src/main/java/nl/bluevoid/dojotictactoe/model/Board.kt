package nl.bluevoid.dojotictactoe.model

import kotlinx.coroutines.flow.MutableStateFlow

class Board(val size: Int = 3) {

    enum class CellState { Empty, Cross, Circle }

    enum class GameState { Undecided, WinCross, WinCircle, Pat }

    private val boardRows: MutableList<MutableList<CellState>> = MutableList(size) { y ->
        MutableList(size) { x -> CellState.Empty }
    }

    val boardFlow = MutableStateFlow<List<List<CellState>>>(boardRows)

    val gameStateFlow = MutableStateFlow<GameState>(GameState.Undecided)

    fun setCell(x: Int, y: Int, state: CellState) {
        checkValidMove(x, y, state)
        boardRows[y][x] = state
        boardFlow.value = boardRows.toList()
        gameStateFlow.value = getGameState()
    }

    private fun checkValidMove(x: Int, y: Int, state: CellState) {
        if (x < 0 || y < 0 || x >= size || y >= size) {
            throw IllegalArgumentException("cell is not in bounds 0..$size: $x, $y")
        }
        if (boardRows[y][x] != CellState.Empty) {
            throw IllegalArgumentException("cell  $x, $y is not empty")
        }
        if (state == CellState.Empty) {
            throw IllegalArgumentException("can not set cell to empty")
        }
    }

    private fun getGameState(): GameState {
        val winner = checkForWin()
        if (winner == CellState.Circle) return GameState.WinCircle
        if (winner == CellState.Cross) return GameState.WinCross
        return if (boardRows.flatten().find { it == CellState.Empty } == null) {
            GameState.Pat
        } else {
            GameState.Undecided
        }
    }

    private fun checkForWin(): CellState? {
        // Check rows
        boardRows.forEach { row ->
            if (row.all { cell -> cell == row[0] && cell != CellState.Empty }) {
                return row[0]
            }
        }

        // Check columns
        for (col in 0 until size) {
            val column = (0 until size).map { row -> boardRows[row][col] }
            if (column.all { it == column[0] && it != CellState.Empty }) {
                return column[0]
            }
        }

        // Check diagonal (top-left to bottom-right)
        val diagonal = (0 until size).map { i -> boardRows[i][i] }
        if (diagonal.all { it == diagonal[0] && it != CellState.Empty }) {
            return diagonal[0]
        }

        // Check anti-diagonal (top-right to bottom-left)
        val antiDiagonal = (0 until size).map { i -> boardRows[i][size - 1 - i] }
        if (antiDiagonal.all { it == antiDiagonal[0] && it != CellState.Empty }) {
            return antiDiagonal[0]
        }

        // No winner
        return null
    }

    override fun toString(): String {
        return boardRows.joinToString(separator = "\n") {
            it.joinToString(separator = "  ") {
                when (it) {
                    CellState.Cross -> "X"
                    CellState.Circle -> "0"
                    CellState.Empty -> " "
                }
            }
        }
    }
}