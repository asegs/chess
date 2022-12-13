package pieces

import Board
import Color
import Piece
import Position
import Tempo
import kotlin.math.sqrt

class King(color: Color) : Piece(color, 999, "♚") {
    override fun getValidMoves(board: Board, position: Position): List<Tempo> {
        return board.getAllKingMoves(position);
    }

    override fun couldBeCheck(board: Board, from: Position, to: Position): Boolean {
        return from.distanceFrom(to) <= 1.5
    }
}