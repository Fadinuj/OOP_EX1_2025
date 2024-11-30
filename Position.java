/**
 * The Position class represents a coordinate on a two-dimensional game board.
 * It is used to specify the row and column of a disc or a move in the game.
 */
public class Position {
    private int row; // The row coordinate of the position
    private int col; // The column coordinate of the position

    /**
     * Constructs a Position object with the specified row and column.
     *
     * @param row The row index of the position (zero-based).
     * @param col The column index of the position (zero-based).
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    /**
     * Constructs a Position object by copying another Position object.
     *
     * @param pos The Position object to copy.
     */
    public Position(Position pos) {
        this.row = pos.row;
        this.col = pos.col;
    }
    /**
     * Retrieves the row index of this position.
     *
     * @return The row index of the position (zero-based).
     */
    public int getRow() {
        return this.row;
    }
    /**
     * Retrieves the column index of this position.
     *
     * @return The column index of the position (zero-based).
     */
    public int getCol() {
        return this.col;
    }

}
