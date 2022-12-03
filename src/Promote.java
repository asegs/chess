public class Promote implements Event{
    private final Position at;

    public Promote(Position at) {
        this.at = at;
    }

    @Override
    public void doToBoard(Board board) {
        board.replaceAtPosition(at, new Queen(board.atPosition(at).color));
    }

    @Override
    public void undoToBoard(Board board) {
        board.replaceAtPosition(at, new Pawn(board.atPosition(at).color));
    }
}
