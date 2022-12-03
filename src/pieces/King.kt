package pieces

import Board
import Color
import Event
import Piece
import Position

class King(color: Color) : Piece(color, 999, "♚") {
    override fun getValidMoves(board: Board, position: Position): List<List<Event>> {
        return listOf();
    }
}