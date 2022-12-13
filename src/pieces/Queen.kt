package pieces

import Board
import Color
import Piece
import Position
import Tempo
import kotlin.math.abs

class Queen(color: Color) : Piece(color, 9, "♛") {
    override fun getValidMoves(board: Board, position: Position): List<Tempo> {
        return board.getAllDiagonalMoves(position) + board.getAllStraightMoves(position)
    }

    override fun couldBeCheck(board: Board, from: Position, to: Position): Boolean {
        return (abs(from.row - to.row) == abs(from.col - to.col)) || from.row == to.row || from.col == to.col
    }
}