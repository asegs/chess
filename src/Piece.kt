abstract class Piece(val color: Color, val value: Int, private val repr: String) {
    abstract fun getValidMoves(board: Board, position: Position, checkValidation: Boolean): List<Tempo>
    open fun couldBeCheck(board: Board, from: Position, to: Position) : Boolean {
        return true
    }
    fun repr(): String {
        return repr
    }
}