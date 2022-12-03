import java.util.ArrayList;
import java.util.List;

public class Empty extends Piece{

    public Empty () {
        super(Color.EMPTY, 0," "," ");
    }

    @Override
    public List<List<Event>> getValidMoves(Board board, Position position) {
        return new ArrayList<>();
    }
}
