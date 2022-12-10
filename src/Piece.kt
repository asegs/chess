abstract class Piece(val color: Color, val value: Int, private val repr: String) {
    abstract fun getValidMoves(board: Board, position: Position): List<Tempo>
    fun repr(): String {
        return repr
    }
}