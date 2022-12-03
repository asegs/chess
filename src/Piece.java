import java.util.List;

public abstract class Piece {
    public final Color color;
    public final int value;
    private final String whiteRepresentation;
    private final String blackRepresentation;

    public Piece(Color color, int value, String whiteRepresentation, String blackRepresentation) {
        this.color = color;
        this.value = value;
        this.whiteRepresentation = whiteRepresentation;
        this.blackRepresentation = blackRepresentation;
    }

    public abstract List<List<Event>> getValidMoves(Board board, Position position);

    public String repr() {
        return whiteRepresentation;
    }
}
