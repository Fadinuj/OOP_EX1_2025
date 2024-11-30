/**
 * The Move class represents a single move in the game, including the position of the move,
 * the disc being placed, and the previous disc at that position (if applicable).
 * It is used to manage moves, including operations like undo.
 */
public class Move {
    private final Position position;  // The position on the board where the move takes place
    private final Disc disc;          // The disc being placed at the specified position
    private final Disc previousDisc;  // The disc that was previously at this position (used for undo operations)

    /**
     * Constructs a Move object with the specified position, disc, and previous disc.
     *
     * @param position     The position on the board where the move is made.
     * @param disc         The disc being placed at the position.
     * @param previousDisc The disc that was previously at this position, or null if the position was empty.
     */
    public Move(Position position, Disc disc, Disc previousDisc) {
        this.position = position;
        this.disc = disc;
        this.previousDisc = previousDisc;
    }

    /**
     * Constructs a Move object with the specified position and disc, without considering the previous disc.
     *
     * @param position The position on the board where the move is made.
     * @param disc     The disc being placed at the position.
     */
    public Move(Position position, Disc disc) {
        this(position, disc, null); // קריאה לבנאי הראשי עם null לדיסק הקודם
    }

    // Getters

    /**
     * Retrieves the position of the move.
     *
     * @return A {Position} object representing the position of the move.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Retrieves the disc associated with the move.
     *
     * @return A {Disc} object representing the disc being placed.
     */
    public Disc getDisc() {
        return disc;
    }

    /**
     * Retrieves the disc that was previously at the move's position (used for undo operations).
     *
     * @return A { Disc} object representing the previous disc, or null if there was none.
     */
    public Disc getPreviousDisc() {
        return previousDisc;
    }
}
