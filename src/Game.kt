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

    fun processMove(move: String, board: Board): Boolean {
        if (move.length < 4) {
            return false
        }
        val fromCol = move[0] - 'a'
        val fromRow = board.boardHeight - (move[1] - '1') - 1
        val toCol = move[2] - 'a'
        val toRow = board.boardHeight - (move[3] - '1') - 1

        board.replaceAtPosition(Position(toRow, toCol), board.atPosition(Position(fromRow, fromCol)))
        board.replaceAtPosition(Position(fromRow, fromCol), Empty())
        return true
    }


}