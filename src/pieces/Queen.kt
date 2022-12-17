package pieces

import Board
import Color
import Piece
import Position
import Tempo
import kotlin.math.abs

class Queen(color: Color) : Piece(color, 9, "♛") {
    override fun getValidMoves(board: Board, position: Position, checkValidation: Boolean): List<Tempo> {
        return board.getAllDiagonalMoves(position, checkValidation) + board.getAllStraightMoves(position, checkValidation)
    }

    override fun couldBeCheck(board: Board, from: Position, to: Position): Boolean {
        return (abs(from.row - to.row) == abs(from.col - to.col)) || from.row == to.row || from.col == to.col
    }

    override fun isCheckFast(board: Board, from: Position, to: Position): Boolean {
        return board.isRookCheck(board, from, to) || board.isBishopCheck(board, from, to)
    }
}