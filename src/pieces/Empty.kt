package pieces

import Board
import Color
import Event
import Piece
import Position

class Empty : Piece(Color.EMPTY, 0, " ") {
    override fun getValidMoves(board: Board, position: Position): List<List<Event>> {
        return listOf();
    }
}