package pieces

import Board
import Color
import Piece
import Position
import Tempo
import kotlin.math.abs

class Knight(color: Color) : Piece(color, 3, "♞") {
    override fun getValidMoves(board: Board, position: Position, checkValidation: Boolean): List<Tempo> {
        return board.getAllKnightMoves(position, checkValidation);
    }

    override fun couldBeCheck(board: Board, from: Position, to: Position): Boolean {
        val distance = from.distanceFrom(to)
        return 2 < distance && distance < 2.5
    }

    override fun isCheckFast(board: Board, from: Position, to: Position): Boolean {
        val rowOffset = abs(from.row - to.row)
        val colOffset = abs(from.col - to.col)
        return ((rowOffset == 2 && colOffset == 1) || (rowOffset == 1 && colOffset == 2))
    }
}