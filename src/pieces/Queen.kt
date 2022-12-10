package pieces

import Board
import Color
import Piece
import Position
import Tempo

class Queen(color: Color) : Piece(color, 9, "♛") {
    override fun getValidMoves(board: Board, position: Position): List<Tempo> {
        return board.getAllDiagonalMoves(position) + board.getAllStraightMoves(position)
    }
}