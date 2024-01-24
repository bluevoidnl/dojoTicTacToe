package nl.bluevoid.dojotictactoe.model

class Bot(color: Board.CellState) : Player(color) {
    override fun doMove(board: Board) {
        val emptyCell = board.getEmptyCells().firstOrNull()
        if (emptyCell == null) {
            throw IllegalStateException("can not do move, no empty cells")
        } else {
            board.setCell(emptyCell.x, emptyCell.y, color)
        }
    }
}