import java.util.List;
import java.util.Random;
/**
 * The RandomAI class represents an AI player that makes random moves during the game.
 * It randomly selects both the move position and the type of disc to place.
 */
public class RandomAI extends AIPlayer {
    private Player randAI;
    private Disc chosenDisc;

    /**
     * Constructs a RandomAI instance.
     *
     * @param isPlayerOne true if the AI is Player 1, false if it is Player 2.
     */
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * Executes a move for the AI. The move is randomly selected from the list of valid moves,
     * and the type of disc is also chosen randomly.
     *
     * @param gameStatus The current state of the game, represented by {PlayableLogic}.
     * @return A { Move} object representing the chosen move, or null if no valid moves are available.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // Determine the current player (Player 1 or Player 2)
        randAI = isPlayerOne ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();

        // Get the list of valid moves
        List<Position> availableMoves = gameStatus.ValidMoves();

        // Check if there are no valid moves
        if (availableMoves.isEmpty()) {
            // Check if the game has finished
            if (gameStatus.isGameFinished()) {
                System.out.println("Game over. No valid moves available.");
            }
            return null; // Return null if no moves are available
        }

        // Select a random move from the list of valid moves
        Random random = new Random();
        Position nextMove = availableMoves.get(random.nextInt(availableMoves.size()));

        // Randomly choose a disc type
        int discType = random.nextInt(3); // Generate a value between 0 and 2
        switch (discType) {
            case 0 -> chosenDisc = new SimpleDisc(randAI);  // Simple disc
            case 1 -> {
                if (randAI.getNumber_of_bombs() > 0) {  // Bomb disc if available
                    chosenDisc = new BombDisc(randAI);
                } else {
                    chosenDisc = new SimpleDisc(randAI);    // Default to simple disc
                }
            }
            case 2 -> {
                if (randAI.getNumber_of_unflippedable() > 0) { //Unflippable disc if available
                    chosenDisc = new UnflippableDisc(randAI);
                } else {
                    chosenDisc = new SimpleDisc(randAI);    // Default to simple disc
                }
            }
            default -> chosenDisc = new SimpleDisc(randAI); // Default case
        }
        // Return the selected move
        return new Move(nextMove, chosenDisc);
    }
}