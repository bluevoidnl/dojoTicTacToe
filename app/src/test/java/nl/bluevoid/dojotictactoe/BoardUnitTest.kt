package nl.bluevoid.dojotictactoe

import nl.bluevoid.dojotictactoe.model.Board
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        val cells = board.boardFlow.value.flatten()
        cells.forEach {
            assertEquals(Board.CellState.empty, it)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun assure_a_cell_can_be_filled_only_once() {
        val board = Board()
        board.setCell(1, 1, Board.CellState.cross)
        board.setCell(1, 1, Board.CellState.cross)
    }

    @Test(expected = IllegalArgumentException::class)
    fun assure_a_cell_can_be_set_to_empty() {
        val board = Board()
        board.setCell(0, 0, Board.CellState.empty)
    }

    @Test
    fun assure_win_situation_is_determined() {
        assertTrue(false)
    }

    @Test
    fun assure_pat_situation_is_determined() {
        assertTrue(false)
    }
}