public class Capture implements Event {
    private final Position removedPosition;
    private Piece removedPiece;
    public Capture (Position removedPosition) {
        this.removedPosition = removedPosition;
    }

    @Override
    public void doToBoard(Board board) {
        removedPiece = board.atPosition(removedPosition);
        board.replaceAtPosition(removedPosition, new Empty());
    }

    @Override
    public void undoToBoard(Board board) {
        board.replaceAtPosition(removedPosition, removedPiece);
    }
}
