package pieces

import Board
import Color
import Piece
import Position
import Tempo

class Knight(color: Color) : Piece(color, 3, "♞") {
    override fun getValidMoves(board: Board, position: Position, checkValidation: Boolean): List<Tempo> {
        return board.getAllKnightMoves(position, checkValidation);
    }

    override fun couldBeCheck(board: Board, from: Position, to: Position): Boolean {
        val distance = from.distanceFrom(to)
        return 2 < distance && distance < 2.5
    }
}