package pieces

import Board
import Color
import Piece
import Position
import Tempo
import kotlin.math.abs

class King(color: Color) : Piece(color, 999, "♚") {
    override fun getValidMoves(board: Board, position: Position, checkValidation: Boolean): List<Tempo> {
        return board.getAllKingMoves(position, checkValidation);
    }

    override fun couldBeCheck(board: Board, from: Position, to: Position): Boolean {
        return from.distanceFrom(to) <= 1.5
    }

    override fun isCheckFast(board: Board, from: Position, to: Position): Boolean {
        val rowDiff = abs(from.row - to.row)
        val colDiff = abs(from.col - to.col)
        return ((rowDiff != colDiff && rowDiff + colDiff == 1) || (rowDiff == colDiff && rowDiff + colDiff == 2))
    }
}