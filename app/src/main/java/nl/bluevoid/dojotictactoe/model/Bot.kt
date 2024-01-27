package nl.bluevoid.dojotictactoe.model

import androidx.annotation.WorkerThread

class Bot(color: CellState) : Player(color) {

    @WorkerThread
    override fun doMove(board: Board) {
        movesChecked = 0
        val moveCell = if (board.getEmptyCells().size == board.size*board.size)
            board.getEmptyCells().randomOrNull()
        else findBestMove(board.getMutableCopy(), color == CellState.Cross)
        println("RRR color $color moves, checked $movesChecked")
        if (moveCell == null) {
            throw IllegalStateException("can not do move, no empty cells")
        } else {
            board.setCell(moveCell.x, moveCell.y, color)
        }
    }

    private fun findBestMove(board: Board, isMaximizing: Boolean): BoardCell {
        var bestMove = BoardCell(-1, -1)
        var bestScore = if (isMaximizing) Int.MIN_VALUE else Int.MAX_VALUE

        for (cell in board.getEmptyCells()) {
            val x = cell.x
            val y = cell.y

            movesChecked++
            // Make the move
            board.setCell(x, y, if (isMaximizing) CellState.Cross else CellState.Circle)

            // Compute evaluation function for this move
            val score = minimax(board, !isMaximizing, 9)

            // Undo the move
            board.undoMove(x, y)

            // If the current move is better than the best, then update best
            if (isMaximizing && score > bestScore || !isMaximizing && score < bestScore) {
                bestScore = score
                bestMove = cell
            }
        }
        return bestMove
    }

    var movesChecked = 0

    private fun minimax(board: Board, isMaximizing: Boolean, depth:Int): Int {

        if(depth==0) return  if(isMaximizing) 10 else -10
        val state = board.gameStateFlow.value
        when (state) {
            Board.GameState.WinCross -> return 10
            Board.GameState.WinCircle -> return -10
            Board.GameState.Draw -> return 0
            else -> {

                var bestScore = if (isMaximizing) Int.MIN_VALUE else Int.MAX_VALUE

                for (cell in board.getEmptyCells()) {
                    val x = cell.x
                    val y = cell.y

                    movesChecked++
                    // Make the move
                    board.setCell(x, y, if (isMaximizing) CellState.Cross else CellState.Circle)

                    // Call minimax recursively and choose the maximum or minimum value
                    val score = minimax(board, !isMaximizing, depth -1)

                    // Undo the move
                    board.undoMove(x, y)

                    bestScore = if (isMaximizing) maxOf(bestScore, score) else minOf(bestScore, score)
                }
                return bestScore
            }
        }
    }
}
