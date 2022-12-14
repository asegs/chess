package pieces

import Board
import Color
import Piece
import Position
import Tempo

class Rook(color: Color) : Piece(color, 5, "♜") {
    //Handle castling
    override fun getValidMoves(board: Board, position: Position, checkValidation: Boolean): List<Tempo> {
        return board.getAllStraightMoves(position, checkValidation)
    }

    override fun couldBeCheck(board: Board, from: Position, to: Position): Boolean {
        return from.row == to.row || from.col == to.col
    }
}