class Queen(color: Color) : Piece(color, 9, "♛") {
    override fun getValidMoves(board: Board, position: Position): List<List<Event>> {
        return listOf();
    }
}