package nl.bluevoid.dojotictactoe

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import nl.bluevoid.dojotictactoe.model.Board
import nl.bluevoid.dojotictactoe.model.CellState
import nl.bluevoid.dojotictactoe.model.Game
import nl.bluevoid.dojotictactoe.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test

class GameUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun assure_2_bots_play_with_different_colors() {
        val game = Game()
        assertEquals(2, game.players.size)
        assertEquals(1, game.players.count { it.color == CellState.Cross })
        assertEquals(1, game.players.count { it.color == CellState.Circle })
    }

    @Test
    fun assure_bots_take_turns() {
        val game = Game()
        val board = game.boardFlow.value
        repeat(2) {
            assertEquals(CellState.Cross, board.getTurn())
            game.nextTurn()
            assertEquals(CellState.Circle, board.getTurn())
            game.nextTurn()
        }
    }

    @Test
    fun assure_2_bots_play_until_win_or_draw() {
        runTest {
            val game = Game(0L)
            game.run()

            while (!game.isFinished()) {
                delay(10)
            }
            assertNotEquals(game.boardFlow.value.gameStateFlow.value, Board.GameState.Undecided)
        }
    }

    @Test
    fun assure_game_can_be_restarted() {
        runTest {
            val game = Game(0L)
            assertEquals(3, game.boardFlow.value.size)
            game.run()

            game.restart(4)
            assertEquals(4, game.boardFlow.value.size)
            game.run()

            while (!game.isFinished()) {
                delay(10)
            }
            assertNotEquals(game.boardFlow.value.gameStateFlow.value, Board.GameState.Undecided)
        }
    }
}