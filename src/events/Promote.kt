package events

import Board
import Event
import Piece
import Position
import pieces.Pawn
import pieces.Queen

class Promote(private val at: Position, private val to: Piece) : Event {
    override fun doToBoard(board: Board) {
        board.replaceAtPosition(at, Queen(board.atPosition(at).color))
    }

    override fun undoToBoard(board: Board) {
        board.replaceAtPosition(at, Pawn(board.atPosition(at).color))
    }
}