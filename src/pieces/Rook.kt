package pieces

import Board
import Color
import Event
import Piece
import Position

class Rook(color: Color) : Piece(color, 5, "♜") {
    //Handle castling
    override fun getValidMoves(board: Board, position: Position): List<List<Event>> {
        return board.getAllStraightMoves(position)
    }
}