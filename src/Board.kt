import events.Capture
import events.Move
import pieces.*

//Do check checking by checking if the king could capture any piece of a certain type by using its moveset.

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

    fun applySequence(sequence: Tempo) {
        for (t in sequence.events) {
            t.doToBoard(this)
        }
    }

    fun undoSequence(sequence: Tempo) {
        for (t in sequence.events.asReversed()) {
            t.undoToBoard(this)
        }
    }

    fun moveIsValid(start: Position, end: Position): MoveStatus {
        if ((end.row < 0 || end.row >= boardHeight || end.col < 0 || end.col >= boardWidth)) {
            return MoveStatus.INVALID
        }
        if (atPosition(end).color == Color.EMPTY) {
            return MoveStatus.VALID
        }
        return if (atPosition(start).color == atPosition(end).color) {
            MoveStatus.INVALID
        } else {
            MoveStatus.CAPTURE
        }

    }

    fun handlePosition(from: Position, to: Position): Tempo {
        //Check if this tempo places you in check
        val at = atPosition(to)
        val moves = if (at.color == Color.EMPTY) {
            listOf(Move(from, to))
        } else {
            listOf(Capture(to), Move(from, to))
        }
        return Tempo(moves, from, to)
    }

    fun getAllStraightMoves(position: Position, validateCheck: Boolean): List<Tempo> {
        val moves: MutableList<Tempo> = mutableListOf();
        //Left
        for (offset in listOf(-1,1)) {
            var i = 1
            while (true) {
                val to = Position(position.row, position.col + (i * offset))
                val validity = moveIsValid(position, to)
                if (validity == MoveStatus.INVALID) {
                    break
                }
                moves.add(handlePosition(position, to))
                if (validity == MoveStatus.CAPTURE) {
                    break
                }
                i++
            }
            i = 1
            while (true) {
                val to = Position(position.row + (i * offset), position.col)
                val validity = moveIsValid(position, to)
                if (validity == MoveStatus.INVALID) {
                    break
                }
                moves.add(handlePosition(position, to))
                if (validity == MoveStatus.CAPTURE) {
                    break
                }
                i++
            }

        }
        return if (validateCheck) filterToSafeMoves(moves, atPosition(position).color) else moves
    }

    fun getAllDiagonalMoves(position: Position, validateCheck: Boolean): List<Tempo> {
        val moves: MutableList<Tempo> = mutableListOf();
        for (rowMod in listOf(-1,1)) {
            for (colMod in listOf(-1,1)) {
                var i = 1
                while (true) {
                    val to = Position(position.row + (i * rowMod), position.col + (i * colMod))
                    val validity = moveIsValid(position, to)
                    if (validity == MoveStatus.INVALID) {
                        break
                    }
                    moves.add(handlePosition(position, to))
                    if (validity == MoveStatus.CAPTURE) {
                        break
                    }
                    i++
                }
            }
        }
        return if (validateCheck) filterToSafeMoves(moves, atPosition(position).color) else moves
    }

    //Currently no en passant or promotion or first move.
    fun getAllPawnMoves(position: Position, validateCheck: Boolean): List<Tempo> {
        val moves: MutableList<Tempo> = mutableListOf();
        val direction = if (atPosition(position).color ==  Color.WHITE) 1 else -1
        for (i in -1..1) {
            val moveTo = Position(position.row - (1 * direction), position.col + i)
            val validity = moveIsValid(position, moveTo)
            if (validity != MoveStatus.INVALID) {
                moves.add(handlePosition(position, moveTo))
            }
        }
        return if (validateCheck) filterToSafeMoves(moves, atPosition(position).color) else moves
    }

    fun getAllKnightMoves(position: Position, validateCheck: Boolean): List<Tempo> {
        val moves: MutableList<Tempo> = mutableListOf()
        for (rowMod in listOf(-1,1)) {
            for (colMod in listOf(-1,1)) {
                val long = Position(position.row + (2 * rowMod), position.col + (1 * colMod))
                val short = Position(position.row + (1 * rowMod), position.col + (2 * colMod))
                val validityLong = moveIsValid(position, long)
                if (validityLong != MoveStatus.INVALID) {
                    moves.add(handlePosition(position, long))
                }
                val validityShort = moveIsValid(position, short)
                if (validityShort != MoveStatus.INVALID) {
                    moves.add(handlePosition(position, short))
                }
            }
        }
        return if (validateCheck) filterToSafeMoves(moves, atPosition(position).color) else moves
    }

    fun getAllKingMoves(position: Position, validateCheck: Boolean): List<Tempo> {
        val moves: MutableList<Tempo> = mutableListOf()
        for (rowMod in -1..1) {
            for (colMod in -1..1) {
                if (rowMod == 0 && colMod == 0) {
                    continue
                }
                val to = Position(position.row + rowMod, position.col + colMod)
                val validity = moveIsValid(position, to)
                if (validity != MoveStatus.INVALID) {
                    moves.add(handlePosition(position, to))
                }
            }
        }
        return if (validateCheck) filterToSafeMoves(moves, atPosition(position).color) else moves
    }

    fun otherColor(color: Color): Color {
        if (color == Color.WHITE) {
            return Color.BLACK
        }
        if (color == Color.BLACK) {
            return Color.WHITE
        }

        return Color.EMPTY
    }

    fun inCheck(color: Color): Boolean {
        val enemyPieces = getAllOfColor(otherColor(color))
        val kingLocation = findKing(color)

        val possibleThreats = enemyPieces.filter { it.first.couldBeCheck(this, it.second, kingLocation) }
        return possibleThreats.any { piece -> piece.first.getValidMoves(this, piece.second, false).any { it.end.equals(kingLocation) } }
    }

    fun getAllOfColor(color: Color): MutableList<Pair<Piece, Position>> {
        val pieces: MutableList<Pair<Piece, Position>> = mutableListOf()
        for ((rowIdx, row) in board.withIndex()) {
            for ((colIdx, piece) in row.withIndex()) {
                if (piece.color == color) {
                    pieces.add(Pair(piece, Position(rowIdx, colIdx)))
                }
            }
        }
        return pieces
    }

    fun findKing(color: Color): Position {
        for ((rowIdx, row) in board.withIndex()) {
            for ((colIdx, piece) in row.withIndex()) {
                if (piece is King && piece.color == color) {
                    return Position(rowIdx, colIdx)
                }
            }
        }
        return Position(-1,-1)
    }

    fun inCheckAfterMove(color: Color, sequence: Tempo): Boolean {
        applySequence(sequence)
        val inCheck = inCheck(color)
        undoSequence(sequence)
        return inCheck
    }

    fun filterToSafeMoves(moves: List<Tempo>, color: Color): List<Tempo> {
        return moves.filter { !inCheckAfterMove(color, it) }
    }
}