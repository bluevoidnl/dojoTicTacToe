package nl.bluevoid.dojotictactoe

import nl.bluevoid.dojotictactoe.model.Board
import nl.bluevoid.dojotictactoe.model.CellState
import org.junit.Assert.assertEquals
import org.junit.Test

class BoardUnitTest {

    @Test
    fun assure_board_is_square() {
        val board = Board(5)
        val rows = board.boardFlow.value
        assertEquals(rows.size, rows[0].size)
    }

    @Test
    fun assure_default_board_row_length_is_3() {
        val board = Board()
        val rows = board.boardFlow.value
        assertEquals(3, rows.size)
    }

    @Test
    fun assure_board_is_empty_at_start() {
        val board = Board()
        val emptyCells = board.getEmptyCells().distinct()
        assertEquals(board.size * board.size, emptyCells.size)
    }

    @Test
    fun assure_cells_can_be_filled_without_state_loss() {
        val board = Board()

        // first move
        board.setCell(1, 1, CellState.Cross)
        assertEquals(board.boardFlow.value[1][1].state, CellState.Cross)

        // second move
        board.setCell(2, 2, CellState.Circle)
        assertEquals(board.boardFlow.value[1][1].state, CellState.Cross)
        assertEquals(board.boardFlow.value[2][2].state, CellState.Circle)

        assertEquals(1, board.getNrMovesDone(CellState.Cross))
        assertEquals(1, board.getNrMovesDone(CellState.Circle))
    }

    @Test(expected = IllegalArgumentException::class)
    fun assure_a_cell_can_be_filled_only_once() {
        val board = Board()
        board.setCell(1, 1, CellState.Cross)
        board.setCell(1, 1, CellState.Cross)
    }

    @Test(expected = IllegalArgumentException::class)
    fun assure_a_cell_can_not_be_set_to_empty() {
        val board = Board()
        board.setCell(0, 0, CellState.Empty)
    }

    @Test(expected = IllegalArgumentException::class)
    fun assure_a_cell_can_not_be_set_out_of_bounds() {
        val board = Board()
        board.setCell(board.size, 0, CellState.Cross)
    }

    @Test(expected = IllegalArgumentException::class)
    fun assure_a_cell_can_not_be_set_out_of_bounds_2() {
        val board = Board()
        board.setCell(0, board.size, CellState.Cross)
    }

    @Test(expected = IllegalArgumentException::class)
    fun assure_a_cell_can_not_be_set_out_of_bounds_3() {
        val board = Board()
        board.setCell(0, -1, CellState.Cross)
    }

    @Test(expected = IllegalArgumentException::class)
    fun assure_a_cell_can_not_be_set_out_of_bounds_4() {
        val board = Board()
        board.setCell(-1, 0, CellState.Cross)
    }

    @Test
    fun assure_win_diagonal_is_determined() {
        val board = Board()
        board.setCell(0, 0, CellState.Cross)
        assertEquals(Board.GameState.Undecided, board.gameStateFlow.value)
        board.setCell(1, 1, CellState.Cross)
        assertEquals(Board.GameState.Undecided, board.gameStateFlow.value)
        board.setCell(2, 2, CellState.Cross)
        assertEquals(Board.GameState.WinCross, board.gameStateFlow.value)
    }

    @Test
    fun assure_win_anti_diagonal_is_determined() {
        val board = Board()
        board.setCell(0, 2, CellState.Circle)
        assertEquals(Board.GameState.Undecided, board.gameStateFlow.value)
        board.setCell(1, 1, CellState.Circle)
        assertEquals(Board.GameState.Undecided, board.gameStateFlow.value)
        board.setCell(2, 0, CellState.Circle)
        assertEquals(Board.GameState.WinCircle, board.gameStateFlow.value)
    }

    @Test
    fun assure_win_column_is_determined() {
        val board = Board()
        board.setCell(1, 0, CellState.Circle)
        assertEquals(Board.GameState.Undecided, board.gameStateFlow.value)
        board.setCell(1, 1, CellState.Circle)
        assertEquals(Board.GameState.Undecided, board.gameStateFlow.value)
        board.setCell(1, 2, CellState.Circle)
        assertEquals(Board.GameState.WinCircle, board.gameStateFlow.value)
    }

    @Test
    fun assure_win_row_is_determined() {
        val board = Board()
        board.setCell(0, 0, CellState.Cross)
        assertEquals(Board.GameState.Undecided, board.gameStateFlow.value)
        board.setCell(1, 0, CellState.Cross)
        assertEquals(Board.GameState.Undecided, board.gameStateFlow.value)
        board.setCell(2, 0, CellState.Cross)
        assertEquals(Board.GameState.WinCross, board.gameStateFlow.value)
    }

    @Test
    fun assure_pat_situation_is_determined() {
        val board = Board()

        // fill with no win pattern
        board.setCell(0, 0, CellState.Circle)
        board.setCell(1, 0, CellState.Circle)
        board.setCell(2, 1, CellState.Circle)
        board.setCell(0, 2, CellState.Circle)
        board.setCell(1, 2, CellState.Circle)
        val boardState = board.boardFlow.value
        boardState.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                if (cell.state == CellState.Empty) {
                    board.setCell(x, y, CellState.Cross)
                }
            }
        }
        println(board)

        assertEquals(Board.GameState.Draw, board.gameStateFlow.value)
    }
}