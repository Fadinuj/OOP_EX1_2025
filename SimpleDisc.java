/**
 * The SimpleDisc class represents a basic type of disc in the game.
 * It is owned by a specific player and can have different states such as flipped or flagged as a bomb.
 */
public class SimpleDisc implements Disc {
    private Player player; // The player who owns this disc
    private boolean bomb = false; // Indicates if the disc has been flagged as part of a bomb effect
    private boolean flipped; // Indicates if the disc has been flipped
    private boolean sFlagBomb; // Special flag for bomb-related operations
    /**
     * Constructs a SimpleDisc object associated with a specific player.
     *
     * @param currentPlayer The player who owns this disc.
     */
    public SimpleDisc(Player currentPlayer) {
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
     * @param player The new owner of this disc.
     */
    @Override
    public void setOwner(Player player) {
        this.player = player;
    }
    /**
     * Returns the type of this disc.
     *
     * @return The string "⬤", representing a simple disc.
     */
    @Override
    public String getType() {
        return "⬤";
    }
    /**
     * Checks if this disc is flagged as part of a bomb effect.
     *
     * @return True if this disc is flagged as a bomb; otherwise, false.
     */
    @Override
    public boolean getFlagBomb() {
        return bomb;
    }
    /**
     * Sets the bomb flag for this disc.
     *
     * @param flag True to flag the disc as part of a bomb effect; false to unflag it.
     */
    @Override
    public void setFlagBomb(boolean flag) {
        this.bomb=flag;
    }
    /**
     * Sets the flipped state for this disc.
     *
     * @param flag True to mark the disc as flipped; false to mark it as not flipped.
     */
    @Override
    public void setFliiped(boolean flag) {
        this.flipped=flag;
    }
    /**
     * Checks if this disc has been flipped.
     *
     * @return True if the disc is flipped; otherwise, false.
     */
    @Override
    public boolean getFliiped() {
        return flipped;
    }
    /**
     * Resets the flags of this disc to their default states.
     * This includes resetting the flipped and bomb flags.
     */
    @Override
    public void resetFlags() {
        this.flipped=false;
        this.bomb=false;
    }
    /**
     * A helper method to set the flipped state for this disc.
     * This method is equivalent to {@link #setFliiped (boolean)}.
     *
     * @param flag True to mark the disc as flipped; false otherwise.
     */
    public void setFlipped(boolean flag) {
        this.flipped=flag;
    }
    /**
     * A helper method to check if the disc has been flipped.
     * This method is equivalent to {@link #getFliiped()}.
     *
     * @return True if the disc is flipped; otherwise, false.
     */
    public boolean getFlipped() {
        return flipped;
    }
    /**
     * Checks if this disc has a special bomb flag set.
     *
     * @return True if the special bomb flag is set; otherwise, false.
     */
    public boolean getSFlagBomb() {
        return sFlagBomb;
    }
    /**
     * Sets a special bomb flag for this disc.
     *
     * @param sFlagBomb True to set the special bomb flag; false otherwise.
     */
    public void setSFlagBomb(boolean sFlagBomb) {
        this.sFlagBomb = sFlagBomb;
    }
}
