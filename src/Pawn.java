import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{
    public Pawn(Color color) {
        super(color, 1, "♙","♟︎");
    }

    @Override
    public List<List<Event>> getValidMoves(Board board, Position position) {
        return new ArrayList<>();
    }
}
