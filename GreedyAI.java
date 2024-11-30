import java.util.List;
/**
 * The GreedyAI class implements a greedy strategy for the AI player.
 * This AI attempts to maximize the number of flipped discs with every move.
 * It selects the move that will result in the highest number of opponent discs being flipped.
 */
public class GreedyAI extends AIPlayer{
    /**
     * Constructs a GreedyAI instance with the specified player role.
     *
     * @param isPlayerOne true if the AI is Player One, false if it is Player Two.
     */
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }
    /**
     * Determines the optimal move for the AI based on the greedy strategy.
     * The AI evaluates all valid moves and selects the one that flips the maximum number of opponent discs.
     *
     * @param gameStatus The current game state encapsulated in a {@link PlayableLogic} object.
     * @return A {@link Move} object representing the AI's chosen move, or null if no valid moves are available.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validMoves = gameStatus.ValidMoves();    // Retrieve all valid moves for the current game state
        // If no valid moves are available, return null
        if (validMoves.isEmpty()) {
            return null;
        }

        Position bestPosition = null; // Stores the position of the best move
        int maxFlips = -1;            // Tracks the maximum number of flips achievable

        // Iterate through all valid moves to find the one with the most flips
        for (Position position : validMoves) {
            int flips = gameStatus.countFlips(position); // Count potential flips for this move

            // Update the best move if this position flips more discs
            if (flips > maxFlips) {
                maxFlips = flips;
                bestPosition = position;
            }
        }


        // If a valid position is found, create a move with a SimpleDisc
        if (bestPosition != null) {
            Disc disc = new SimpleDisc(this);
            return new Move(bestPosition, disc);
        }

        // Return null if no valid move was selected (should not happen with validMoves check above)
        return null;
    }
}
