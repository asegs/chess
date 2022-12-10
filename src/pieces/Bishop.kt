package pieces

import Board
import Color
import Piece
import Position
import Tempo

class Bishop(color: Color) : Piece(color, 3, "♝") {
    override fun getValidMoves(board: Board, position: Position): List<Tempo> {
        return board.getAllDiagonalMoves(position)
    }
}