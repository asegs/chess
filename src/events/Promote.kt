package events

import Board
import Event
import Position
import pieces.Pawn
import pieces.Queen

class Promote(private val at: Position) : Event {
    override fun doToBoard(board: Board) {
        board.replaceAtPosition(at, Queen(board.atPosition(at).color))
    }

    override fun undoToBoard(board: Board) {
        board.replaceAtPosition(at, Pawn(board.atPosition(at).color))
    }
}