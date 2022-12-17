package pieces

import Board
import Color
import Piece
import Position
import Tempo

class Empty : Piece(Color.EMPTY, 0, " ") {
    override fun getValidMoves(board: Board, position: Position, checkValidation: Boolean): List<Tempo> {
        return listOf();
    }
}