class Bot {
    fun level0 (board: Board, color: Color): Tempo? {
        val moves = board.getAllMoves(color)
        return moves.randomOrNull()
    }

    fun scoreMove(board:Board, move:Tempo, color: Color): Int {
        board.makeMove(move)
        val score = board.scoreBoard(color)
        board.undoMove()
        return score
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
        var min = 9999

        for (move in moves) {
            board.makeMove(move)
            val opponentMoves = board.getAllMoves(board.otherColor(color))
            var max = -9999
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

}