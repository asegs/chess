import pieces.*
import kotlin.jvm.JvmStatic

object Game {
    @JvmStatic
    fun main(args: Array<String>) {
        val p1Human = args[0] == "h"
        val p2Human = args[1] == "h"
        val b = Board()
        var condition: GameCondition
        while (true) {
            for (i in 0 until 40) {
                println()
            }
            b.printBoard()
            condition = b.getGameCondition(Color.WHITE)
            if (condition != GameCondition.NORMAL) {
                if (condition == GameCondition.CHECKMATE) {
                    println("Black wins by " + GameCondition.CHECKMATE)
                    break
                } else if (condition == GameCondition.DRAW || condition == GameCondition.STALEMATE) {
                    println("Draw by $condition")
                }
            }
            if (p1Human) {
                humanTurn(b, Color.WHITE)
            } else {
                botTurn(b, Color.WHITE, args[0].toInt())
            }

            for (i in 0 until 40) {
                println()
            }
            b.printBoard()
            condition = b.getGameCondition(Color.BLACK)
            if (condition != GameCondition.NORMAL) {
                if (condition == GameCondition.CHECKMATE) {
                    println("White wins by " + GameCondition.CHECKMATE)
                    break
                } else if (condition == GameCondition.DRAW || condition == GameCondition.STALEMATE) {
                    println("Draw by $condition")
                }
            }
            if (p2Human) {
                humanTurn(b, Color.BLACK)
            } else {
                botTurn(b, Color.BLACK, args[1].toInt())
            }
        }
    }

    fun validateMove(board: Board, from:Position, toPosition: Position): Tempo? {
        val moves = board.atPosition(from).getValidMoves(board, from, true)
        return moves.find { it.end.equals(toPosition) }
    }

    fun processMove(move: String, board: Board, color:Color): Boolean {
        if (move == "d") {
            return true
        }
        if (move.contains("=")) {
            val col = move[0] - 'a'
            val row = board.boardHeight - (move[1] - '1') - 1
            val pieceColor = if (move[4] == 'w') Color.WHITE else Color.BLACK
            val piece:Piece = when (move[3]) {
                'p' -> Pawn(pieceColor)
                'n' -> Knight(pieceColor)
                'b' -> Bishop(pieceColor)
                'r' -> Rook(pieceColor)
                'q' -> Queen(pieceColor)
                'k' -> King(pieceColor)
                else -> Empty()
            }
            board.board[row][col] = piece
            board.printBoard()
            return false
        }
        if (move.length < 4) {
            if (move.length == 2) {
                val fromCol = move[0] - 'a'
                val fromRow = board.boardHeight - (move[1] - '1') - 1
                val from = Position(fromRow, fromCol)
                val at = board.atPosition(from)
                val moves = at.getValidMoves(board, from, true)
                if (color == Color.WHITE) board.printBoard(moves) else board.printFlippedBoard(moves)
            }

            if (move == "h") {
                val bot = Bot()
                val suggestion = bot.alphaBetaRoot(board, if (board.getAllMoves(color).size > 100) 5 else 6, color)
                board.printBoard(listOf(suggestion!!))
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

    fun humanTurn(board: Board, color: Color) {
        var changed = false
        while (!changed) {
            print("> ")
            val input = readLine()!!
            if (processMove(input, board, color)) {
                changed = true
            }
        }
        for (i in 0 until 40) {
            println()
        }
        if (color == Color.WHITE) {
            board.printBoard()
        } else {
            board.printFlippedBoard()
        }
    }

    fun botTurn(board: Board, color: Color, level:Int) {
        val move = Bot().alphaBetaRoot(board, level, color)
        if (move != null) {
            board.makeMove(move)
        }
    }


}