package pieces

import Board
import Color
import Piece
import Position
import Tempo
import kotlin.math.abs

class Bishop(color: Color) : Piece(color, 3, "♝") {
    override fun getValidMoves(board: Board, position: Position): List<Tempo> {
        return board.getAllDiagonalMoves(position)
    }

    override fun couldBeCheck(board: Board, from: Position, to: Position): Boolean {
        return abs(from.row - to.row) == abs(from.col - to.col)
    }
}