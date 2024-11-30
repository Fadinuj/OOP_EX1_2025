/**
 * The Player class serves as an abstract base class for representing players in the game.
 * It provides common properties and methods for managing a player's state, such as the number of wins,
 * bomb discs, and unflippable discs. Subclasses can extend this class to implement specific behavior
 * for human players or AI players.
 */
public abstract class Player {
    protected boolean isPlayerOne; // Indicates whether the player is Player One or Player Two
    protected int wins;// Tracks the number of games won by the player
    protected static final int initial_number_of_bombs = 3; // Initial number of bomb discs
    protected static final int initial_number_of_unflippedable = 2; // Initial number of unflippable discs

    // Tracks the current number of special discs available to the player
    protected int number_of_bombs;
    protected int number_of_unflippedable;
    /**
     * Constructs a new Player instance, initializing the player's state with the default
     * number of bombs and unflippable discs, and setting the win count to zero.
     *
     * @param isPlayerOne true if the player is Player One, false if the player is Player Two.
     */
    public Player(boolean isPlayerOne) {
        this.isPlayerOne = isPlayerOne;
        reset_bombs_and_unflippedable();
        wins = 0;
    }

    /**
     * Determines whether this player is Player 1.
     *
     * @return true if the player is Player 1, false if the player is Player 2 (or any other player).
     */
    public boolean isPlayerOne() {
        return isPlayerOne;
    }

    /**
     * Retrieves the number of wins accumulated by this player over the course of the game.
     *
     * @return The total number of wins achieved by the player.
     */
    public int getWins() {
        return wins;
    }

    /**
     * Increment the win counter by one when the player wins a round or match.
     */
    public void addWin() {
        this.wins++;
    }
    /**
     * Determines whether this player is human.
     *
     * @return true if the player is human.
     */
    abstract boolean isHuman();

    /**
     * Retrieves the current number of bomb discs available to the player.
     *
     * @return The number of bomb discs available to the player.
     */
    public int getNumber_of_bombs() {
        return number_of_bombs;
    }
    /**
     * Retrieves the current number of unflippable discs available to the player.
     *
     * @return The number of unflippable discs available to the player.
     */
    public int getNumber_of_unflippedable() {
        return number_of_unflippedable;
    }
    /**
     * Reduces the number of bomb discs available to the player by one.
     * This method should be called when the player uses a bomb disc.
     */
    public void reduce_bomb() {
        number_of_bombs--;
    }
    /**
     * Increases the number of bomb discs available to the player by one.
     * This method can be used for game mechanics that reward the player with extra bomb discs.
     */
    public void increase_bomb(){
        number_of_bombs++;
    }
    /**
     * Increases the number of unflippable discs available to the player by one.
     * This method can be used for game mechanics that reward the player with extra unflippable discs.
     */
    public void increase_unflippedable(){
        number_of_unflippedable++;
    }

    public void reduce_unflippedable() {
        number_of_unflippedable--;
    }

    public void reset_bombs_and_unflippedable() {
        this.number_of_bombs = initial_number_of_bombs;
        this.number_of_unflippedable = initial_number_of_unflippedable;
    }

}
