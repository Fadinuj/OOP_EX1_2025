/**
 * The BombDisc class represents a special type of disc in the game.
 * When placed on the board, it can trigger unique behavior such as "explosions"
 * that affect surrounding discs. This class implements the Disc interface.
 */
public class BombDisc implements Disc{
    private Player player;    // The player who owns this disc
    private boolean fliiped;    // Indicates whether the disc has been flipped
    private boolean bomb=false; // Indicates whether the disc has been flagged as part of an explosion

    /**
     * Constructs a BombDisc object, associating it with the player who owns it.
     *
     * @param currentPlayer The player who owns the BombDisc.
     */
    public BombDisc(Player currentPlayer) {
        this.player = currentPlayer;
    }
    /**
     * Retrieves the owner of this disc.
     *
     * @return The player who owns this disc.
     */
    @Override
    public Player getOwner() {
        return player;
    }
    /**
     * Sets the owner of this disc.
     *
     * @param player The player who will own this disc.
     */
    @Override
    public void setOwner(Player player)
    {
        this.player = player;
    }
    /**
     * Retrieves the type of this disc, represented as a string.
     *
     * @return A string representing the BombDisc ("💣").
     */
    @Override
    public String getType() {
        return "💣";
    }
    /**
     * Retrieves the flag status of the bomb.
     * Used to determine if this disc has been processed in an explosion.
     *
     * @return true if the disc is flagged; false otherwise.
     */
    @Override
    public boolean getFlagBomb() {
        return bomb;
    }
    /**
     * Sets the bomb flag for this disc.
     *
     * @param flag true to mark the disc as flagged; false otherwise.
     */
    @Override
    public void setFlagBomb(boolean flag) {
        this.bomb=flag;
    }
    /**
     * Sets whether this disc has been flipped.
     *
     * @param flag true if the disc has been flipped; false otherwise.
     */
    @Override
    public void setFliiped(boolean flag) {
        this.fliiped=flag;
    }
    /**
     * Checks if this disc has been flipped.
     *
     * @return true if the disc has been flipped; false otherwise.
     */
    @Override
    public boolean getFliiped() {
        return fliiped;
    }
    /**
     * Resets the flags for this disc.
     * Typically used to clear its state after an explosion or game reset.
     */
    @Override
    public void resetFlags() {
        this.bomb=false;
        this.fliiped=false;
    }

}
