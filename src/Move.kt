class Move(private val from: Position, private val to: Position) : Event {
    override fun doToBoard(board: Board) {
        board.replaceAtPosition(to, board.atPosition(from))
        board.replaceAtPosition(from, Empty())
    }

    override fun undoToBoard(board: Board) {
        board.replaceAtPosition(from, board.atPosition(to))
        board.replaceAtPosition(to, Empty())
    }
}