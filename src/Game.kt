import kotlin.jvm.JvmStatic

object Game {
    @JvmStatic
    fun main(args: Array<String>) {
        var b = Board()
        var bot = Bot()
        var white = true
        for (i in 0 until 40) {
            println()
        }
        b.printBoard()
        while (true) {
            var changed = false
            print("> ")
            val input = readLine()!!
            if (input == "reset") {
                b = Board()
                white = true
            } else{
                if (processMove(input, b, white)) {
                    //white = !white
                    changed = true
                }

            }

            if (changed) {
                for (i in 0 until 40) {
                    println()
                }
                if (white) {
                    b.printBoard()
                    val move = bot.level1(b, Color.BLACK)
                    if (move != null) {
                        b.makeMove(move)
                    } else {
                        println(b.getGameCondition(Color.BLACK))
                    }
                    b.printBoard()
                } else {
                    //b.printFlippedBoard()
                }
            }
        }
    }

    fun validateMove(board: Board, from:Position, toPosition: Position): Tempo? {
        val moves = board.atPosition(from).getValidMoves(board, from, true)
        return moves.find { it.end.equals(toPosition) }
    }

    fun processMove(move: String, board: Board, white:Boolean): Boolean {
        if (move.length < 4) {
            if (move.length == 2) {
                val fromCol = move[0] - 'a'
                val fromRow = board.boardHeight - (move[1] - '1') - 1
                val from = Position(fromRow, fromCol)
                val at = board.atPosition(from)
                val moves = at.getValidMoves(board, from, true)
                if (white) board.printBoard(moves) else board.printFlippedBoard(moves)
            }
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