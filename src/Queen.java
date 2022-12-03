import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    public Queen(Color color) {
        super(color, 9,"♕","♛");
    }

    @Override
    public List<List<Event>> getValidMoves(Board board, Position position) {
        return new ArrayList<>();
    }
}
