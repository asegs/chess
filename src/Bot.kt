import java.util.concurrent.Callable
import java.util.concurrent.Executors

class Bot {
    fun level0 (board: Board, color: Color): Tempo? {
        val moves = board.getAllMoves(color)
        return moves.randomOrNull()
    }

    fun scoreMove(board:Board, move:Tempo, color: Color): Float {
        board.makeMove(move)
        val score = board.scoreBoard(color)
        board.undoMove()
        return score.toFloat()
    }

    //hardcoded to black
    fun level1 (board: Board, color: Color): Tempo? {
        val moves = board.getAllMoves(color)
        var bestTempo: Tempo? = null
        var bestScore = board.scoreBoard()

        for (move in moves) {
            board.makeMove(move)
            val score = board.scoreBoard()
            if ((color == Color.BLACK && score <= bestScore) || (color == Color.WHITE && score >= bestScore)) {
                bestTempo = move
                bestScore = score
            }
            board.undoMove()
        }
        return bestTempo
    }

    fun level2 (board: Board, color: Color): Tempo? {
        val moves = board.getAllMoves(color)
        var hasWorstResponse: Tempo? = null
        var min = Float.MAX_VALUE

        for (move in moves) {
            board.makeMove(move)
            val opponentMoves = board.getAllMoves(board.otherColor(color))
            var max = Float.MIN_VALUE
            for (opponentMove in opponentMoves) {
                val opponentMoveScore = scoreMove(board, opponentMove, board.otherColor(color))
                if (opponentMoveScore >= max) {
                    max = opponentMoveScore
                }
            }
            if (max <= min) {
                min = max
                hasWorstResponse = move
            }
            board.undoMove()
        }
        return hasWorstResponse
    }

    fun minimax(board: Board, depth: Int, scoreFor: Color, turn: Color = scoreFor, top: Boolean=true): Pair<Tempo?, Float> {
        if (depth == 0) {
            return Pair(null, board.scoreBoard(scoreFor).toFloat())
        }
        val children = board.getAllMoves(turn)
        val minimize = scoreFor != turn
        val scored = children.map {
                board.makeMove(it)
                val scoredChild = minimax(board, depth - 1, scoreFor, board.otherColor(turn), false)
                val scoredWithMove = Pair(it, scoredChild.second)
                board.undoMove()
                scoredWithMove
            }

//        var scored: List<Pair<Tempo?, Float>>? = null
//
//        if (top) {
//            val callables: List<Callable<Pair<Tempo?, Float>>> = children.map {
//                board.makeMove(it)
//                val scoredChild = minimax(board, depth - 1, scoreFor, board.otherColor(turn), false)
//                val scoredWithMove = Pair(it, scoredChild.second)
//                board.undoMove()
//                scoredWithMove }.map { Callable<Pair<Tempo?, Float>> {it} }
//            val executor = Executors.newFixedThreadPool(callables.size)
//            scored = executor.invokeAll(callables).map { it.get() }
//        } else {
//            scored = children.map {
//                board.makeMove(it)
//                val scoredChild = minimax(board, depth - 1, scoreFor, board.otherColor(turn), false)
//                val scoredWithMove = Pair(it, scoredChild.second)
//                board.undoMove()
//                scoredWithMove
//            }
//        }

        return (if (minimize) scored.minByOrNull { it.second } else scored.maxByOrNull { it.second })
                ?: return if (minimize) Pair(null, Float.MIN_VALUE) else Pair(null, Float.MAX_VALUE)


    }

}