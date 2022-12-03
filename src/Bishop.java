import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{
    public Bishop(Color color) {
        super(color, 3,"♗","♝");
    }

    @Override
    public List<List<Event>> getValidMoves(Board board, Position position) {
        return new ArrayList<>();
    }
}
