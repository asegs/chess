import java.util.List;

public class Rook extends Piece{
    public Rook(Color color) {
        super(color, 5,"♖","♜");
    }

    //Handle castling
    @Override
    public List<List<Event>> getValidMoves(Board board, Position position) {
        return board.getAllStraightMoves(position);
    }
}
