/**
 * The HumanPlayer class represents a human player in the game.
 * This class extends the abstract { Player} class and implements
 * functionality specific to human players.
 */
public class HumanPlayer extends Player{
    /**
     * Constructs a HumanPlayer instance, specifying whether the player is Player One or Player Two.
     *
     * @param isPlayerOne true if this player is Player One, false if Player Two.
     */
    public HumanPlayer(boolean isPlayerOne) {
        super(isPlayerOne);
    }
    /**
     * Indicates whether this player is human.
     *
     * @return true, as this class represents a human player.
     */
    @Override
    boolean isHuman() {
        return true;
    }
}
