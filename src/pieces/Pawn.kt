package pieces

import Board
import Color
import Piece
import Position
import Tempo

class Pawn(color: Color) : Piece(color, 1, "♟︎") {
    override fun getValidMoves(board: Board, position: Position): List<Tempo> {
        return board.getAllPawnMoves(position)
    }
}