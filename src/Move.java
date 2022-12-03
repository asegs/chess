public class Move implements Event{
    private final Position from;
    private final Position to;

    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void doToBoard(Board board) {
        board.replaceAtPosition(to, board.atPosition(from));
        board.replaceAtPosition(from, new Empty());
    }

    @Override
    public void undoToBoard(Board board) {
        board.replaceAtPosition(from, board.atPosition(to));
        board.replaceAtPosition(to, new Empty());
    }
}
