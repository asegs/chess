import kotlin.math.max
import kotlin.math.min
import kotlin.streams.toList

class Bot {

    fun scoreMove(board: Board, move:Tempo, depth:Int, color: Color):Float {
        val copy = board.copyBoard()
        copy.makeMove(move)
        val value = alphabeta(copy, depth - 1, color, copy.otherColor(color))
        copy.undoMove()
        return value
    }

    fun alphaBetaRoot(board: Board, depth: Int, color: Color, random: Boolean=true): Tempo? {
        val moves = board.getAllMoves(color)
        val scoredMoves = moves.parallelStream().map { Pair(it, scoreMove(board, it, depth, color)) }.toList()
        val maxScore = scoredMoves.maxByOrNull { it.second } ?: return null
        val tiedMoves = scoredMoves.filter { it.second == maxScore.second }
        return if (random) tiedMoves.random().first else tiedMoves.first().first
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

    fun minimax(board: Board, depth: Int, scoreFor: Color, random: Boolean=true, turn: Color = scoreFor): Pair<Tempo?, Float> {
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
                val scoredChild = minimax(board, depth - 1, scoreFor, random, board.otherColor(turn))
                val scoredWithMove = Pair(it, scoredChild.second)
                board.undoMove()
                scoredWithMove
            }

        return if (random) randomExtreme(minimize, scored) else firstOrNullAlt(scored)


    }

    fun randomExtreme(minimize: Boolean, scored: List<Pair<Tempo?, Float>>): Pair<Tempo?, Float> {
        val extreme = if (minimize) scored.minByOrNull { it.second } else scored.maxByOrNull { it.second }
        return scored.filter { it.second == extreme!!.second }.random()
    }

    fun firstOrNullAlt(scored: List<Pair<Tempo, Float>>): Pair<Tempo?, Float> {
        if (scored.isEmpty()) {
            return Pair(null, 0F)
        }
        return scored.first()
    }

    fun confirmEqual(board:Board, depth: Int, color: Color) {
        val alphaMove = alphaBetaRoot(board, depth, color, false)
        val miniMove = minimax(board, depth, color, false)
        if (alphaMove == null && miniMove.first == null) {
            return
        }

        if (!alphaMove!!.start.equals(miniMove.first!!.start) || !alphaMove.end.equals(miniMove.first!!.end)) {
            println(board.atPosition(alphaMove.start)::class.simpleName)
            println(alphaMove.toString())
            println(board.atPosition(miniMove.first!!.start)::class.simpleName)
            println(miniMove.first.toString())
            //exitProcess(1)
        }
    }

}