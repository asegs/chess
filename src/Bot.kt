class Bot {

    fun scoreMove(board:Board, move:Tempo, color: Color): Float {
        board.makeMove(move)
        val score = board.scoreBoard(color)
        board.undoMove()
        return score.toFloat()
    }

    fun minimax(board: Board, depth: Int, scoreFor: Color, turn: Color = scoreFor, top: Boolean=true): Pair<Tempo?, Float> {
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
                val scoredChild = minimax(board, depth - 1, scoreFor, board.otherColor(turn), false)
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