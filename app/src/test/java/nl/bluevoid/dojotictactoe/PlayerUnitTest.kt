package nl.bluevoid.dojotictactoe

import nl.bluevoid.dojotictactoe.model.Board
import nl.bluevoid.dojotictactoe.model.Bot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class PlayerUnitTest {
    @Test
    fun assure_bot_plays_allowed_move() {
        val board = Board()
        val p1 = Bot(Board.CellState.Cross)
        p1.doMove(board)

        assertEquals(1, board.getNrMovesDone(p1.color))
        assertEquals(0, board.getNrMovesDone(Board.CellState.Circle))
    }

    @Test
    fun assure_bot_plays_best_first_move() {
        assertTrue(false)
    }

    @Test
    fun assure_bot_that_starts_wins() {
        assertTrue(false)
    }
}