import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Board {
    private final List<List<Piece>> board;
    private final int boardHeight = 8;
    private final int boardWidth = 8;

    private final String darkBrown = "\033[48;2;204;119;34m";
    private final String lightBrown = "\033[48;2;225;193;110m";
    private final String blackFg = "\033[38;2;0;0;0m";
    private final String whiteFg = "\033[38;2;255;255;255m";
    private final String reset = "\033[0m";

    public Board (List<List<Piece>> board) {
        this.board = board;
    }

    public Board() {
        //Assign starting board
        this.board = Arrays.asList(
                pieceRow(Color.BLACK),
                pawnRow(Color.BLACK),
                blankRow(),
                blankRow(),
                blankRow(),
                blankRow(),
                pawnRow(Color.WHITE),
                pieceRow(Color.WHITE)
        );
    }

    public List<Piece> pawnRow(Color color) {
        List<Piece> row = new ArrayList<>();
        for (int i = 0 ; i < boardWidth ; i++) {
            row.add(new Pawn(color));
        }
        return row;
    }

    public List<Piece> pieceRow(Color color) {
        return Arrays.asList(
                new Rook(color),
                new Knight(color),
                new Bishop(color),
                new Queen(color),
                new King(color),
                new Bishop(color),
                new Knight(color),
                new Rook(color)
        );
    }

    public void printSquare(Color background, Piece piece) {
        if (background == Color.WHITE) {
            System.out.print(lightBrown);
        } else {
            System.out.print(darkBrown);
        }
        if (piece.color == Color.WHITE) {
            System.out.print(whiteFg);
        } else {
            System.out.print(blackFg);
        }

        System.out.print("\u2000"+piece.repr() + "\u2000");
        System.out.print(reset);
    }

    public void printBoard () {
        for (int row = 0 ; row < boardHeight ; row ++) {
            for (int col = 0 ; col < boardWidth ; col ++) {
                printSquare(col % 2 == row % 2 ? Color.WHITE : Color.BLACK, board.get(row).get(col));
            }
            System.out.println();
        }
    }

    public List<Piece> blankRow() {
        List<Piece> row = new ArrayList<>();
        for (int i = 0 ; i < boardWidth ; i++) {
            row.add(new Empty());
        }
        return row;
    }


    private Board copyBoard() {
        List<List<Piece>> rows = new ArrayList<>();
        for (List<Piece> row: board) {
            rows.add(new ArrayList<>(row));
        }
        return new Board(rows);
    }

    public Board newBoardAfterEvents(List<Event> events) {
        Board copy = copyBoard();
        for (Event event: events) {
            event.doToBoard(copy);
        }
        return copy;
    }

    public void replaceAtPosition(Position position, Piece newPiece) {
        board.get(position.row).set(position.col, newPiece);
    }

    public Piece atPosition(Position position) {
        return board.get(position.row).get(position.col);
    }

    public boolean moveIsValid(Position start, Position end) {
        if (end.row < 0 || end.row >= boardHeight || end.col < 0 || end.col >= boardWidth) {
            return false;
        }
        return board.get(start.row).get(start.col).color != board.get(end.row).get(end.col).color;
    }

    public List<Event> handlePosition(Position from, Position to) {
        Piece at = atPosition(to);
        if (at.color == Color.EMPTY) {
            return Collections.singletonList(new Move(from, to));
        } else {
            return Arrays.asList(new Capture(to), new Move(from, to));
        }
    }

    public List<List<Event>> getAllStraightMoves(Position position) {
        List<List<Event>> moves = new ArrayList<>();
        //Left
        int i = 1;
        while (moveIsValid(position, new Position(position.row, position.col - i))) {
            Position to = new Position(position.row, position.col - i);
            moves.add(handlePosition(position, to));
            i++;
        }
        //Right
        i = 1;
        while (moveIsValid(position, new Position(position.row, position.col + i))) {
            Position to = new Position(position.row, position.col + i);
            moves.add(handlePosition(position, to));
            i++;
        }
        //Up
        i = 1;
        while (moveIsValid(position, new Position(position.row - i, position.col))) {
            Position to = new Position(position.row - i, position.col);
            moves.add(handlePosition(position, to));
            i++;
        }
        //Down
        i = 1;
        while (moveIsValid(position, new Position(position.row + i, position.col))) {
            Position to = new Position(position.row + i, position.col);
            moves.add(handlePosition(position, to));
            i++;
        }
        return moves;
    }

}
