abstract class Piece(val color: Color, val value: Int, private val repr: String) {
    abstract fun getValidMoves(board: Board, position: Position): List<Tempo>
    fun couldBeCheck(board: Board, from: Position, to: Position) : Boolean {
        return true
    }
    fun repr(): String {
        return repr
    }
}