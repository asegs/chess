package pieces

import Board
import Color
import Piece
import Position
import Tempo

class King(color: Color) : Piece(color, 999, "♚") {
    override fun getValidMoves(board: Board, position: Position): List<Tempo> {
        return board.getAllKingMoves(position);
    }
}