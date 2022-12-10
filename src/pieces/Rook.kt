package pieces

import Board
import Color
import Piece
import Position
import Tempo

class Rook(color: Color) : Piece(color, 5, "♜") {
    //Handle castling
    override fun getValidMoves(board: Board, position: Position): List<Tempo> {
        return board.getAllStraightMoves(position)
    }
}