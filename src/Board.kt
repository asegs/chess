import events.Capture
import events.Move
import events.Promote
import pieces.*
import kotlin.math.abs
import kotlin.reflect.KClass

//Do check checking by checking if the king could capture any piece of a certain type by using its moveset.

class Board {
    private val board: List<MutableList<Piece>>
    private val history: MutableList<Tempo> = mutableListOf()
    val boardHeight = 8
    private val boardWidth = 8
    private val darkBrown = "\u001b[48;2;204;119;34m"
    private val lightBrown = "\u001b[48;2;225;193;110m"
    private val blackFg = "\u001b[38;2;0;0;0m"
    private val whiteFg = "\u001b[38;2;255;255;255m"
    private val bold = "\u001b[1m"
    private val reset = "\u001b[0m"

    private val moveWhite = "\u001b[48;2;122;225;110m"
    private val captureWhite = "\u001b[48;2;225;110;110m"

    private val moveBlack = "\u001b[48;2;99;204;34m"
    private val captureBlack = "\u001b[48;2;204;68;34m"

    private val fromBlack = "\u001b[48;2;204;204;34m"
    private val fromWhite = "\u001b[48;2;212;196;74m"

    private val previousStates: MutableMap<Int, Int> = mutableMapOf()

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

    fun copyBoard(): Board {
        val pieces: MutableList<MutableList<Piece>> = mutableListOf()
        for (row in 0 until boardHeight) {
            val rowList:MutableList<Piece> = mutableListOf()
            for (col in 0 until boardWidth) {
                rowList.add(board[row][col])
            }
            pieces.add(rowList)
        }
        return Board(pieces)
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

    fun tintEvent(color: Color, status: MoveStatus): String {
        if (color == Color.WHITE) {
            if (status == MoveStatus.VALID) {
                return moveWhite
            }
            if (status == MoveStatus.CAPTURE) {
                return captureWhite
            }
            return lightBrown
        } else {
            if (status == MoveStatus.VALID) {
                return moveBlack
            }
            if (status == MoveStatus.CAPTURE) {
                return captureBlack
            }
            return darkBrown
        }
    }

    fun printSquare(background: String, piece: Piece) {
        print(background)
        print(bold)
        if (piece.color == Color.WHITE) {
            print(whiteFg)
        } else {
            print(blackFg)
        }
        print(piece.repr())
        print(reset)
    }

    fun printBoard() {
        printBoard(listOf())
    }

    fun printFlippedBoard() {
        printFlippedBoard(listOf())
    }

    fun printBoard(moves: List<Tempo>) {
        val sparseMoves = temposToSparseArray(moves)
        println(getGameCondition(Color.WHITE))
        println(scoreBoard())
        println(" abcdefgh")
        for (row in 0 until boardHeight) {
            print(boardHeight - row)
            for (col in 0 until boardWidth) {
                if (moves.isNotEmpty() && moves.first().start.equals(Position(row, col))) {
                    printSquare(if (col % 2 == row % 2) fromWhite else fromBlack, board[row][col])
                } else {
                    val validity = tempoEvents(sparseMoves[row][col])
                    printSquare(tintEvent(if (col % 2 == row % 2) Color.WHITE else Color.BLACK, validity), board[row][col])
                }

            }
            print(boardHeight - row)
            println()
        }
        println(" abcdefgh")
    }

    fun printFlippedBoard(moves: List<Tempo>) {
        val sparseMoves = temposToSparseArray(moves)
        println(getGameCondition(Color.BLACK))
        println(scoreBoard())
        println(" hgfedcba")
        for (row in boardHeight - 1 downTo 0) {
            print(boardHeight - row)
            for (col in boardWidth - 1 downTo 0) {
                if (moves.isNotEmpty() && moves.first().start.equals(Position(row, col))) {
                    printSquare(if (col % 2 == row % 2) fromWhite else fromBlack, board[row][col])
                } else {
                    val validity = tempoEvents(sparseMoves[row][col])
                    printSquare(tintEvent(if (col % 2 == row % 2) Color.WHITE else Color.BLACK, validity), board[row][col])
                }
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

    fun scoreBoard(color: Color): Int {
        return if (color == Color.WHITE) scoreBoard() else scoreBoard() * -1
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

    fun makeMove(sequence: Tempo) {
        applySequence(sequence)
        history.add(sequence)
//        previousStates.putIfAbsent(board.hashCode(), 0)
//        previousStates[board.hashCode()] = previousStates[board.hashCode()]!! + 1
    }

    fun undoMove() {
        val move = history.removeLast()
//        previousStates[board.hashCode()] = previousStates.getOrDefault(board.hashCode(), 1) - 1
        undoSequence(move)
    }

    fun moveOccurredTwice():Boolean {
        return previousStates.getOrDefault(board.hashCode(), 0) >= 2
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
        val fromPiece = atPosition(from)
        val moves = if (at.color == Color.EMPTY) {
            listOf(Move(from, to))
        } else {
            listOf(Capture(to), Move(from, to))
        }
        if (fromPiece is Pawn) {
            if ((fromPiece.color == Color.WHITE && to.row == 0) || (fromPiece.color == Color.BLACK && to.row == boardHeight - 1)) {
                return Tempo(moves + Promote(to, Queen(fromPiece.color)), from, to)
            }
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

    //Currently no en passant.
    fun getAllPawnMoves(position: Position, validateCheck: Boolean): List<Tempo> {
        val color = atPosition(position).color
        val moves: MutableList<Tempo> = mutableListOf();
        val direction = if (color ==  Color.WHITE) 1 else -1
        for (i in -1..1) {
            val moveTo = Position(position.row - (1 * direction), position.col + i)
            val validity = moveIsValid(position, moveTo)
            if (validity == MoveStatus.CAPTURE && i != 0) {
                moves.add(handlePosition(position, moveTo))
            } else if (validity == MoveStatus.VALID && i == 0) {
                moves.add(handlePosition(position, moveTo))
            }
        }

        if ((color == Color.WHITE && position.row == boardHeight - 2) || (color == Color.BLACK && position.row == 1)) {
            val moveTo = Position(position.row - (2 * direction), position.col)
            val validity = moveIsValid(position, moveTo)
            if (validity == MoveStatus.VALID) {
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


    fun reverseKingCheck(color: Color, kingLocation: Position, enemies:List<Pair<Piece, Position>>): Boolean {
        val enemyColor = otherColor(color)
        val remainingClasses: Set<KClass<out Piece>> = enemies.map { it.first::class }.toSet()
        if (remainingClasses.contains(Pawn::class)) {
            if (Pawn(color).getValidMoves(this, kingLocation, false).any { atPosition(it.end).color == enemyColor && atPosition(it.end) is Pawn}) {
                return true
            }
        }
        if (remainingClasses.contains(Queen::class)) {
            if (Queen(color).getValidMoves(this, kingLocation, false).any { atPosition(it.end).color == enemyColor && atPosition(it.end) is Queen}) {
                return true
            }
        }
        if (remainingClasses.contains(Rook::class)) {
            if (Rook(color).getValidMoves(this, kingLocation, false).any { atPosition(it.end).color == enemyColor && atPosition(it.end) is Rook}) {
                return true
            }
        }
        if (remainingClasses.contains(Knight::class)) {
            if (Knight(color).getValidMoves(this, kingLocation, false).any { atPosition(it.end).color == enemyColor && atPosition(it.end) is Knight}) {
                return true
            }
        }
        if (remainingClasses.contains(Bishop::class)) {
            if (Bishop(color).getValidMoves(this, kingLocation, false).any { atPosition(it.end).color == enemyColor && atPosition(it.end) is Bishop}) {
                return true
            }
        }
        if (remainingClasses.contains(King::class)) {
            if (King(color).getValidMoves(this, kingLocation, false).any { atPosition(it.end).color == enemyColor && atPosition(it.end) is King}) {
                return true
            }
        }
        return false
    }

    fun inCheck(color: Color): Boolean {
        val enemyPieces = getAllOfColor(otherColor(color))
        val kingLocation = findKing(color)

        val possibleThreats = enemyPieces.filter { it.first.couldBeCheck(this, it.second, kingLocation) }
        //return possibleThreats.any { it.first.isCheckFast(this, it.second, kingLocation) }
        return reverseKingCheck(color, kingLocation, possibleThreats)
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
        //val willCauseRepetition = moveOccurredTwice()
        undoSequence(sequence)
        return inCheck //|| willCauseRepetition
    }

    fun filterToSafeMoves(moves: List<Tempo>, color: Color): List<Tempo> {
        return moves.filter { !inCheckAfterMove(color, it) }
    }

    fun getAllMoves(color: Color): List<Tempo> {
        return getAllOfColor(color).fold(listOf()) { moves, pair -> moves + pair.first.getValidMoves(this, pair.second, true) }
    }

    fun getGameCondition(color: Color): GameCondition {
        val whitePieces = getAllOfColor(Color.WHITE)
        val blackPieces = getAllOfColor(Color.BLACK)

        if (whitePieces.size == 1 && blackPieces.size == 1) {
            return GameCondition.DRAW
        }

        if (whitePieces.size <= 2 && blackPieces.size <= 2) {
            if (whitePieces.any { it.first is Bishop || it.first is Knight } && blackPieces.any { it.first is Bishop || it.first is Knight }) {
                return GameCondition.DRAW
            }
        }

        val allMoves = getAllMoves(color)
        val inCheck = inCheck(color)

        return if (inCheck) {
            if (allMoves.isNotEmpty()) {
                GameCondition.CHECK
            } else {
                GameCondition.CHECKMATE
            }
        } else {
            if (allMoves.isNotEmpty()) {
                GameCondition.NORMAL
            } else {
                GameCondition.STALEMATE
            }
        }
    }

    fun tempoEvents(tempo: Tempo): MoveStatus {
        if (tempo.events.any { it is Capture }) {
            return MoveStatus.CAPTURE
        }
        if (tempo.events.any { it is Move }) {
            return MoveStatus.VALID
        }

        return MoveStatus.INVALID
    }

    fun temposToSparseArray(tempos: List<Tempo>): MutableList<MutableList<Tempo>> {
        val sparse: MutableList<MutableList<Tempo>> = mutableListOf()
        for (row in 0..boardHeight) {
            val rowList: MutableList<Tempo> = mutableListOf()
            for (col in 0..boardWidth) {
                rowList.add(Tempo(listOf(),Position(-1,-1),Position(-1,-1)))
            }
            sparse.add(rowList)
        }

        for (tempo in tempos) {
            sparse[tempo.end.row][tempo.end.col] = tempo
        }

        return sparse
    }

    fun isBishopCheck(board: Board, from:Position, to:Position): Boolean {
        if (from.row == to.row || from.col == to.col) {
            return false
        }
        val diff = abs(from.row - to.row)
        val rowRate = diff / (from.row - to.row)
        val colRate = abs(from.col - to.col) / (from.col - to.col)
        for (i in 1 until diff) {
            if (board.atPosition(Position(to.row + (i * rowRate), to.col + (i * colRate))).color != Color.EMPTY) {
                return false
            }
        }
        return true
    }

    fun isRookCheck(board: Board, from:Position, to:Position): Boolean {
        if (from.row == to.row) {
            val diff = from.col - to.col
            val direction = abs(diff) / diff
            for (i in 1 until abs(diff) - 1) {
                if (board.atPosition(Position(from.row, to.col + (i * direction))).color != Color.EMPTY) {
                    return false
                }
            }
            return true
        } else {
            val diff = from.row - to.row
            val direction = abs(diff) / diff
            for (i in 1 until abs(diff) - 1) {
                if (board.atPosition(Position(from.col, to.row + (i * direction))).color != Color.EMPTY) {
                    return false
                }
            }
            return true
        }
    }
}