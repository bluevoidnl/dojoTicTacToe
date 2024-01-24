package nl.bluevoid.dojotictactoe.model

class Bot(color: CellState) : Player(color) {
    override fun doMove(board: Board) {
        val emptyCell = board.getEmptyCells().randomOrNull()
        if (emptyCell == null) {
            throw IllegalStateException("can not do move, no empty cells")
        } else {
            board.setCell(emptyCell.x, emptyCell.y, color)
        }
    }
}