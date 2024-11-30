/**
 * The UnflippableDisc class represents a special type of disc that cannot be flipped
 * during the game, even if it is surrounded by the opponent's discs. It is owned by
 * a specific player and has additional properties such as being flagged as part of a bomb effect.
 */
public class UnflippableDisc implements Disc {
    private Player player; // The player who owns this disc
    private final boolean flipped = false; // This type of disc is never flipped
    private boolean bomb = false; // Indicates if the disc is flagged as part of a bomb effect

    /**
     * Constructs an UnflippableDisc object associated with a specific player.
     *
     * @param currentPlayer The player who owns this disc.
     */
    public UnflippableDisc(Player currentPlayer) {
        player = currentPlayer;
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
     * @return The string "⭕", representing an unflippable disc.
     */
    @Override
    public String getType() {
        return "⭕";
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
     * This method is overridden but does nothing, as unflippable discs cannot be flipped.
     *
     * @param flag Ignored, as flipping is not applicable for this type of disc.
     */
    @Override
    public void setFliiped(boolean flag) {
    }
    /**
     * Always returns false, as this type of disc cannot be flipped.
     *
     * @return False, indicating the disc is not flipped.
     */
    @Override
    public boolean getFliiped() {
        return false;
    }
    /**
     * Resets the flags of this disc. This method does nothing for this type of disc.
     */
    @Override
    public void resetFlags() {
    }
}
