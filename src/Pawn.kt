class Pawn(color: Color) : Piece(color, 1, "♟︎") {
    override fun getValidMoves(board: Board, position: Position): List<List<Event>> {
        return listOf();
    }
}