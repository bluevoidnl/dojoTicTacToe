package nl.bluevoid.dojotictactoe.model

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class Bot(color: CellState) : Player(color) {

    override suspend fun doMove(board: Board) {
        movesChecked = 0

        val moveCell = if (board.getEmptyCells().size == board.size * board.size) {
            // For the first move take a random cell for variation and less calculations
            // This also shows that when playing bot to bot on 3x3 it does not matter how to start:
            // it is always a draw.
            board.getEmptyCells().random()
        } else {
            findBestMoveParallel(board, color == CellState.Cross)
        }
        println("RRR color $color moves, checked $movesChecked")

        board.setCell(moveCell.x, moveCell.y, color)
    }

    var movesChecked = 0

    private suspend fun findBestMoveParallel(board: Board, isMaximizing: Boolean): BoardCell = coroutineScope {
        var bestMove = BoardCell(-1, -1)
        var bestScore = if (isMaximizing) Int.MIN_VALUE else Int.MAX_VALUE

        // Create a list to hold all the deferred results
        val deferredResults = mutableListOf<Deferred<Pair<BoardCell, Int>>>()

        for (cell in board.getEmptyCells()) {
            val x = cell.x
            val y = cell.y

            // Launch a coroutine for each move
            val deferred = async(Dispatchers.Default) {
                //println("RRR start check cell $cell")
                val boardCopy = board.getMutableCopy()
                movesChecked++
                // Make the move
                boardCopy.setCell(x, y, if (isMaximizing) CellState.Cross else CellState.Circle)

                // Compute the evaluation function for this move
                val score = minimax(boardCopy, !isMaximizing, 20)

                // Undo the move
                boardCopy.undoMove(x, y)
               // println("RRR end check cell $cell")
                // Return the cell and its score
                Pair(cell, score)
            }

            deferredResults.add(deferred)
        }

        // Await all coroutines and process the results
        deferredResults.forEach { deferred ->
            val (cell, score) = deferred.await()

            // If the current move is better than the best, then update the best
            if (isMaximizing && score > bestScore || !isMaximizing && score < bestScore) {
                bestScore = score
                bestMove = cell
            }
        }

        bestMove
    }


    private fun minimax(board: Board, isMaximizing: Boolean, depth: Int): Int {

        if (depth == 0) return if (isMaximizing) 10 else -10

        val state = board.gameStateFlow.value
        return when (state) {
            Board.GameState.WinCross -> 10
            Board.GameState.WinCircle -> -10
            Board.GameState.Draw -> 0
            else -> {
                var bestScore = if (isMaximizing) Int.MIN_VALUE else Int.MAX_VALUE

                for (cell in board.getEmptyCells()) {
                    val x = cell.x
                    val y = cell.y

                    movesChecked++
                    // Make the move
                    board.setCell(x, y, if (isMaximizing) CellState.Cross else CellState.Circle)

                    // Call minimax recursively and choose the maximum or minimum value
                    val score = minimax(board, !isMaximizing, depth - 1)

                    // Undo the move
                    board.undoMove(x, y)

                    bestScore = if (isMaximizing) maxOf(bestScore, score) else minOf(bestScore, score)
                }
                bestScore
            }
        }
    }
}
