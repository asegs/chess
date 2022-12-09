package pieces

import Board
import Color
import Event
import Piece
import Position

class Queen(color: Color) : Piece(color, 9, "♛") {
    override fun getValidMoves(board: Board, position: Position): List<List<Event>> {
        return board.getAllDiagonalMoves(position) + board.getAllStraightMoves(position)
    }
}