import kotlin.math.max
import kotlin.math.min

class Bot {

    fun scoreMove(board:Board, move:Tempo, color: Color): Float {
        board.makeMove(move)
        val score = board.scoreBoard(color)
        board.undoMove()
        return score.toFloat()
    }

    fun alphabeta(board: Board, depth: Int, scoreFor: Color, turn: Color=scoreFor, alpha:Float=Float.NEGATIVE_INFINITY, beta:Float=Float.POSITIVE_INFINITY): Pair<Tempo?, Float> {
        var newAlpha = alpha
        var newBeta = beta
        if (depth == 0) {
            return Pair(null, board.scoreBoard(scoreFor).toFloat())
        }
        val children = board.getAllMoves(turn)
        val minimize = scoreFor != turn
        if (children.isEmpty()) {
            return if (minimize) Pair(null, Float.MAX_VALUE) else Pair(null, Float.MIN_VALUE)
        }

        if (!minimize) {
            var value = Float.NEGATIVE_INFINITY
            var bestMove:Tempo? = null
            for (child in children) {
                board.makeMove(child)
                val scoredPair = alphabeta(board, depth - 1, scoreFor, board.otherColor(turn), newAlpha, newBeta)
                if (scoredPair.second > value) {
                    value = scoredPair.second
                    bestMove = child
                }
                if (scoredPair.second == value) {
                    bestMove = if (bestMove == null) {
                        child
                    } else {
                        listOf(bestMove, child).random()
                    }
                }
                board.undoMove()
                if (value >= newBeta) {
                    break
                }
                newAlpha = max(newAlpha, value)
            }
            return Pair(bestMove, value)
        } else {
            var value = Float.POSITIVE_INFINITY
            var bestMove:Tempo? = null
            for (child in children) {
                board.makeMove(child)
                val scoredPair = alphabeta(board, depth - 1, scoreFor, board.otherColor(turn), newAlpha, newBeta)
                if (scoredPair.second < value) {
                    value = scoredPair.second
                    bestMove = child
                }

                if (scoredPair.second == value) {
                    bestMove = if (bestMove == null) {
                        child
                    } else {
                        listOf(bestMove, child).random()
                    }
                }

                board.undoMove()
                if (value <= newAlpha) {
                    break
                }
                newBeta = min(newBeta, value)
            }
            return Pair(bestMove, value)
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