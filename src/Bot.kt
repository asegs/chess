class Bot {
    fun level0 (board: Board, color: Color): Tempo? {
        val moves = board.getAllMoves(color)
        return moves.randomOrNull()
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

    fun levelN(board: Board, color: Color, depth: Int): Tempo? {
        if (depth == 0) {
            return level0(board, color)
        }
        val moves = board.getAllMoves(color)
        for (move in moves) {

        }
    }
}