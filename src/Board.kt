import events.Capture
import events.Move
import pieces.*

class Board {
    private val board: List<MutableList<Piece>>
    val boardHeight = 8
    private val boardWidth = 8
    private val darkBrown = "\u001b[48;2;204;119;34m"
    private val lightBrown = "\u001b[48;2;225;193;110m"
    private val blackFg = "\u001b[38;2;0;0;0m"
    private val whiteFg = "\u001b[38;2;255;255;255m"
    private val bold = "\u001b[1m"
    private val reset = "\u001b[0m"

    constructor(board: List<MutableList<Piece>>) {
        this.board = board
    }

    constructor() {
        //Assign starting board
        board = listOf(
                pieceRow(Color.BLACK),
                pawnRow(Color.BLACK),
                blankRow(),
                blankRow(),
                blankRow(),
                blankRow(),
                pawnRow(Color.WHITE),
                pieceRow(Color.WHITE)
        )
    }

    fun pawnRow(color: Color): MutableList<Piece> {
        val row: MutableList<Piece> = mutableListOf();
        for (i in 0 until boardWidth) {
            row.add(Pawn(color))
        }
        return row
    }

    fun pieceRow(color: Color): MutableList<Piece> {
        return mutableListOf(
                Rook(color),
                Knight(color),
                Bishop(color),
                Queen(color),
                King(color),
                Bishop(color),
                Knight(color),
                Rook(color)
        )
    }

    fun blankRow(): MutableList<Piece> {
        val row: MutableList<Piece> = mutableListOf();
        for (i in 0 until boardWidth) {
            row.add(Empty())
        }
        return row
    }

    fun printSquare(background: Color, piece: Piece?) {
        if (background == Color.WHITE) {
            print(lightBrown)
        } else {
            print(darkBrown)
        }
        print(bold)
        if (piece!!.color == Color.WHITE) {
            print(whiteFg)
        } else {
            print(blackFg)
        }
        print(piece.repr())
        print(reset)
    }

    fun printBoard() {
        println(scoreBoard())
        println(" abcdefgh")
        for (row in 0 until boardHeight) {
            print(boardHeight - row)
            for (col in 0 until boardWidth) {
                printSquare(if (col % 2 == row % 2) Color.WHITE else Color.BLACK, board[row][col])
            }
            print(boardHeight - row)
            println()
        }
        println(" abcdefgh")
    }

    fun printFlippedBoard() {
        println(scoreBoard())
        println(" hgfedcba")
        for (row in boardHeight - 1 downTo 0) {
            print(boardHeight - row)
            for (col in boardWidth - 1 downTo 0) {
                printSquare(if (col % 2 == row % 2) Color.WHITE else Color.BLACK, board[row][col])
            }
            print(boardHeight - row)
            println()
        }
        println(" hgfedcba")
    }

    fun scoreBoard():Int {
        return board.fold(0) {
            sum, row -> sum + row.fold(0) {
                subSum, piece -> subSum + piece.value * if (piece.color == Color.WHITE) 1 else -1
        }
        }
    }


    fun replaceAtPosition(position: Position, newPiece: Piece) {
        board[position.row][position.col] = newPiece
    }

    fun atPosition(position: Position): Piece {
        return board[position.row][position.col]
    }

    fun moveIsValid(start: Position, end: Position): Boolean {
        return if (end.row < 0 || end.row >= boardHeight || end.col < 0 || end.col >= boardWidth) {
            false
        } else board[start.row][start.col].color != board[end.row][end.col].color
    }

    fun handlePosition(from: Position, to: Position): List<Event> {
        val at = atPosition(to)
        return if (at.color == Color.EMPTY) {
            listOf(Move(from, to))
        } else {
            listOf(Capture(to), Move(from, to))
        }
    }

    fun getAllStraightMoves(position: Position): List<List<Event>> {
        val moves: MutableList<List<Event>> = mutableListOf();
        //Left
        var i = 1
        while (moveIsValid(position, Position(position.row, position.col - i))) {
            val to = Position(position.row, position.col - i)
            moves.add(handlePosition(position, to))
            i++
        }
        //Right
        i = 1
        while (moveIsValid(position, Position(position.row, position.col + i))) {
            val to = Position(position.row, position.col + i)
            moves.add(handlePosition(position, to))
            i++
        }
        //Up
        i = 1
        while (moveIsValid(position, Position(position.row - i, position.col))) {
            val to = Position(position.row - i, position.col)
            moves.add(handlePosition(position, to))
            i++
        }
        //Down
        i = 1
        while (moveIsValid(position, Position(position.row + i, position.col))) {
            val to = Position(position.row + i, position.col)
            moves.add(handlePosition(position, to))
            i++
        }
        return moves
    }

    fun getAllDiagonalMoves(position: Position): List<List<Event>> {
        val moves: MutableList<List<Event>> = mutableListOf();
        //Northeast
        var i = 1
        while (moveIsValid(position, Position(position.row - i, position.col + i))) {
            val to = Position(position.row - i, position.col + i)
            moves.add(handlePosition(position, to))
            i++
        }
        //Southeast
        i = 1
        while (moveIsValid(position, Position(position.row + i, position.col + i))) {
            val to = Position(position.row + i, position.col + i)
            moves.add(handlePosition(position, to))
            i++
        }
        i = 1
        //Southwest
        while (moveIsValid(position, Position(position.row + i, position.col - i))) {
            val to = Position(position.row + i, position.col - i)
            moves.add(handlePosition(position, to))
            i++
        }
        i = 1
        //Northwest
        while (moveIsValid(position, Position(position.row - i, position.col - i))) {
            val to = Position(position.row - i, position.col - i)
            moves.add(handlePosition(position, to))
            i++
        }
        return moves;
    }
}