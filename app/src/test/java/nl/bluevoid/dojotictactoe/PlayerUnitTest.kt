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
        runTest {
            val board = Board()
            val p1 = Bot(CellState.Cross)
            p1.doMove(board)

            assertEquals(1, board.getNrMovesDone(p1.color))
            assertEquals(0, board.getNrMovesDone(CellState.Circle))
        }
    }
    
    @Test
    fun assure_bot_game_ends_in_a_draw_on_3x3() {
        runTest {
            val game = Game(0L)
            val startColor = game.boardFlow.value.getTurn()
            assertEquals(CellState.Cross, startColor)
            game.run()

            while (!game.isFinished()) {
                delay(100)
            }
            assertEquals(Board.GameState.Draw, game.boardFlow.value.gameStateFlow.value)
        }
    }
}