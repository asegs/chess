interface Event {
    fun doToBoard(board: Board)
    fun undoToBoard(board: Board)
}