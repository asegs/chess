class Capture(private val removedPosition: Position) : Event {
    private var removedPiece: Piece? = null
    override fun doToBoard(board: Board) {
        removedPiece = board.atPosition(removedPosition)
        board.replaceAtPosition(removedPosition, Empty())
    }

    override fun undoToBoard(board: Board) {
        board.replaceAtPosition(removedPosition, removedPiece!!)
    }
}