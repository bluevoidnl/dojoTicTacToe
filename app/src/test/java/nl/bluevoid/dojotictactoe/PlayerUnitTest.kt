package nl.bluevoid.dojotictactoe

import nl.bluevoid.dojotictactoe.model.Board
import nl.bluevoid.dojotictactoe.model.Bot
import org.junit.Test

import org.junit.Assert.*


class PlayerUnitTest {
    @Test
    fun assure_bot_plays_allowed_move() {
        val board = Board()
        val p1 = Bot(Board.CellState.Cross)
        p1.doMove(board)
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