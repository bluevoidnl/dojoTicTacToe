package nl.bluevoid.dojotictactoe

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import nl.bluevoid.dojotictactoe.model.Board
import nl.bluevoid.dojotictactoe.model.Bot
import nl.bluevoid.dojotictactoe.model.CellState
import nl.bluevoid.dojotictactoe.model.Game
import nl.bluevoid.dojotictactoe.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PlayerUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun assure_bot_plays_allowed_move() {
        val board = Board()
        val p1 = Bot(CellState.Cross)
        p1.doMove(board)

        assertEquals(1, board.getNrMovesDone(p1.color))
        assertEquals(0, board.getNrMovesDone(CellState.Circle))
    }

    @Test
    fun assure_bot_plays_best_first_move() {
        val game = Game(3)
        game.nextTurn()
        // best move is center in 3x3 game
        val centerCell=game.boardFlow.value.boardFlow.value[1][1]
        assertEquals(CellState.Cross, centerCell.state)
    }

    @Test
    fun assure_bot_that_starts_wins() {
        runTest {
            val game = Game(0L)
            val startColor = game.boardFlow.value.getTurn()
            assertEquals(CellState.Cross, startColor)
            game.run()

            while (!game.isFinished()) {
                delay(10)
            }
            assertEquals(Board.GameState.WinCross, game.boardFlow.value.gameStateFlow.value)
        }
    }
}