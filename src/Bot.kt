import kotlin.math.max
import kotlin.math.min

class Bot {

    fun alphaBetaRoot(board: Board, depth: Int, color: Color): Tempo? {
        val moves = board.getAllMoves(color)
        var bestMove = Float.NEGATIVE_INFINITY
        var bestMoveFound:Tempo? = null

        for (move in moves) {
            board.makeMove(move)
            val value = alphabeta(board, depth - 1, color, board.otherColor(color))
            board.undoMove()
            if (value > bestMove) {
                bestMove = value
                bestMoveFound = move
            }
            else if (value == bestMove) {
                bestMoveFound = if (bestMoveFound == null) {
                    move
                } else {
                    listOf(bestMoveFound, move).random()
                }
            }
        }
        return bestMoveFound
    }

    fun alphabeta(board: Board, depth: Int, scoreFor: Color, turn: Color=scoreFor, alpha:Float=Float.NEGATIVE_INFINITY, beta:Float=Float.POSITIVE_INFINITY): Float {
        var newAlpha = alpha
        var newBeta = beta
        if (depth == 0) {
            return board.scoreBoard(scoreFor).toFloat()
        }
        val children = board.getAllMoves(turn)
        val minimize = scoreFor != turn
        if (children.isEmpty()) {
            return if (minimize) Float.MAX_VALUE else Float.NEGATIVE_INFINITY
        }

        if (!minimize) {
            var value = Float.NEGATIVE_INFINITY
            for (child in children) {
                board.makeMove(child)
                value = max(value, alphabeta(board, depth - 1, scoreFor, board.otherColor(turn), newAlpha, newBeta))
                board.undoMove()
                newAlpha = max(newAlpha, value)
                if (newBeta <= newAlpha) {
                    break
                }
            }
            return value
        } else {
            var value = Float.POSITIVE_INFINITY
            for (child in children) {
                board.makeMove(child)
                value = min(value,alphabeta(board, depth - 1, scoreFor, board.otherColor(turn), newAlpha, newBeta))

                board.undoMove()
                newBeta = min(newBeta, value)
                if (newBeta <= newAlpha) {
                    break
                }

            }
            return value
        }
    }

    fun minimax(board: Board, depth: Int, scoreFor: Color, turn: Color = scoreFor): Pair<Tempo?, Float> {
        if (depth == 0) {
            return Pair(null, board.scoreBoard(scoreFor).toFloat())
        }
        val children = board.getAllMoves(turn)
        val minimize = scoreFor != turn
        if (children.isEmpty()) {
            return if (minimize) Pair(null, Float.MAX_VALUE) else Pair(null, Float.MIN_VALUE)
        }
        val scored = children.map {
                board.makeMove(it)
                val scoredChild = minimax(board, depth - 1, scoreFor, board.otherColor(turn))
                val scoredWithMove = Pair(it, scoredChild.second)
                board.undoMove()
                scoredWithMove
            }

        return randomExtreme(minimize, scored)


    }

    fun randomExtreme(minimize: Boolean, scored: List<Pair<Tempo?, Float>>): Pair<Tempo?, Float> {
        val extreme = if (minimize) scored.minByOrNull { it.second } else scored.maxByOrNull { it.second }
        return scored.filter { it.second == extreme!!.second }.random()
    }

}