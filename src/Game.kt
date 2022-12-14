import pieces.Empty
import kotlin.jvm.JvmStatic

object Game {
    @JvmStatic
    fun main(args: Array<String>) {
        var b = Board()
        var white = true
        for (i in 0 until 40) {
            println()
        }
        b.printBoard()
        while (true) {
            print("> ")
            val input = readLine()!!
            if (input == "reset") {
                b = Board()
                white = true
            } else{
                if (processMove(input, b)) {
                    white = !white
                }

            }
            for (i in 0 until 40) {
                println()
            }
            if (white) {
                b.printBoard()
            } else {
                b.printFlippedBoard()
            }
        }
    }

    fun validateMove(board: Board, from:Position, toPosition: Position): Tempo? {
        val moves = board.atPosition(from).getValidMoves(board, from, true)
        return moves.find { it.end.equals(toPosition) }
    }

    fun processMove(move: String, board: Board): Boolean {
        if (move.length < 4) {
            return false
        }
        val fromCol = move[0] - 'a'
        val fromRow = board.boardHeight - (move[1] - '1') - 1
        val toCol = move[2] - 'a'
        val toRow = board.boardHeight - (move[3] - '1') - 1

        val to  = Position(toRow, toCol)
        val from = Position(fromRow, fromCol)

        //Get promote type here if contains promote action
        val maybeMove = validateMove(board, from, to)
        return if (maybeMove == null) {
            false
        } else {
            board.makeMove(maybeMove)
            true
        }
    }


}