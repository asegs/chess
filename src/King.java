import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    public King(Color color) {
        super(color, 999,"♔","♚");
    }

    @Override
    public List<List<Event>> getValidMoves(Board board, Position position) {
        return new ArrayList<>();
    }
}
