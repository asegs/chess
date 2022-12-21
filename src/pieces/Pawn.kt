package pieces

import Board
import Color
import Piece
import Position
import Tempo
import kotlin.math.abs

class Pawn(color: Color) : Piece(color, 1, "♟︎") {
    override fun getValidMoves(board: Board, position: Position, checkValidation: Boolean): List<Tempo> {
        return board.getAllPawnMoves(position, checkValidation)
    }

    //Could check if in front as well...
    override fun couldBeCheck(board: Board, from: Position, to: Position): Boolean {
        return from.distanceFrom(to) < 1.5
    }

    override fun isCheckFast(board: Board, from: Position, to: Position): Boolean {
        return (from.row + (if (color == Color.WHITE) -1 else 1) == to.row && (abs(from.col - to.col) == 1))
    }
}