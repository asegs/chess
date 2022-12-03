package pieces

import Board
import Color
import Event
import Piece
import Position

class Knight(color: Color) : Piece(color, 3, "♞") {
    override fun getValidMoves(board: Board, position: Position): List<List<Event>> {
        return listOf();
    }
}