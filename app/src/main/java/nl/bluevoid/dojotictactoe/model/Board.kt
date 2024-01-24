package nl.bluevoid.dojotictactoe.model

import kotlinx.coroutines.flow.MutableStateFlow

class Board(val size: Int = 3) {

    enum class GameState { Undecided, WinCross, WinCircle, Draw }

    private val boardRows: MutableList<MutableList<BoardCell>> = MutableList(size) { y ->
        MutableList(size) { x -> BoardCell(x, y) }
    }

    val boardFlow = MutableStateFlow<List<List<BoardCell>>>(boardRows)

    // hack to make gameview display new board at every turn
    val moveCounterFlow = MutableStateFlow<Int>(0)

    val gameStateFlow = MutableStateFlow<GameState>(GameState.Undecided)

    fun setCell(x: Int, y: Int, state: CellState) {
        checkValidMove(x, y, state)
        boardRows[y][x].state = state
        boardFlow.value = deepCopyBoard()
        gameStateFlow.value = getGameState()
        moveCounterFlow.value = moveCounterFlow.value + 1
    }

    fun deepCopyBoard() = boardRows.map { row -> row.map { cell -> cell.copy() } }

    fun getNrMovesDone(state: CellState) = boardRows.flatten().count { it.state == state }

    fun getTurn(): CellState {
        val crosses = getNrMovesDone(CellState.Cross)
        val circles = getNrMovesDone(CellState.Circle)
        return if (crosses <= circles) CellState.Cross else CellState.Circle
    }

    fun getEmptyCells() = boardRows.flatten().filter { it.isEmpty() }

    private fun checkValidMove(x: Int, y: Int, state: CellState) {
        if (x < 0 || y < 0 || x >= size || y >= size) {
            throw IllegalArgumentException("cell is not in bounds 0..$size: $x, $y")
        }
        if (boardRows[y][x].state != CellState.Empty) {
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
        return if (boardRows.flatten().find { it.state == CellState.Empty } == null) {
            GameState.Draw
        } else {
            GameState.Undecided
        }
    }

    private fun checkForWin(): CellState? {
        // Check rows
        boardRows.forEach { row ->
            if (row.all { cell -> cell.state == row[0].state && !cell.isEmpty() }) {
                return row[0].state
            }
        }

        // Check columns
        for (col in 0 until size) {
            val column = (0 until size).map { row -> boardRows[row][col] }
            if (column.all { it.state == column[0].state && !it.isEmpty() }) {
                return column[0].state
            }
        }

        // Check diagonal (top-left to bottom-right)
        val diagonal = (0 until size).map { i -> boardRows[i][i] }
        if (diagonal.all { it.state == diagonal[0].state && !it.isEmpty() }) {
            return diagonal[0].state
        }

        // Check anti-diagonal (top-right to bottom-left)
        val antiDiagonal = (0 until size).map { i -> boardRows[i][size - 1 - i] }
        if (antiDiagonal.all { it.state == antiDiagonal[0].state && !it.isEmpty() }) {
            return antiDiagonal[0].state
        }

        // No winner
        return null
    }

    override fun toString(): String {
        return boardRows.joinToString(separator = "\n") {
            it.joinToString(separator = "  ") {
                when (it.state) {
                    CellState.Cross -> "X"
                    CellState.Circle -> "0"
                    CellState.Empty -> " "
                }
            }
        }
    }
}