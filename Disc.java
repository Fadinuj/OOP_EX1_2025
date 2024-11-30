/**
 * The Disc interface defines the characteristics of a game in a chess-like game.
 * Implementing classes should provide information about the player who owns the Disc.
 */
public interface Disc {

    /**
     * Get the player who owns the Disc.
     *
     * @return The player who is the owner of this game disc.
     */
    Player getOwner();

    /**
     * Set the player who owns the Disc.
     *
     */
    void setOwner(Player player);

    /**
     * Get the type of the disc.
     * use the:
     *          "⬤",         "⭕"                "💣"
     *      Simple Disc | Unflippedable Disc | Bomb Disc |
     * respectively.
     */
    String getType();
    /**
     * Retrieves the current state of the bomb flag for this disc.
     * The bomb flag is used to track whether this disc is part of an explosion or has been processed.
     *
     * @return true if the disc has been flagged as part of a bomb effect; false otherwise.
     */
    boolean getFlagBomb();
    /**
     * Sets the bomb flag for this disc.
     * This is typically used during explosions or chain reactions to mark the disc as processed.
     *
     * @param flag true to flag the disc as part of a bomb effect; false to clear the flag.
     */
    void setFlagBomb(boolean flag);
    /**
     * Sets the flipped state of this disc.
     * A flipped state indicates whether the disc has been turned during gameplay.
     *
     * @param flag true to mark the disc as flipped; false otherwise.
     */
    void setFliiped(boolean flag);
    /**
     * Retrieves the flipped state of this disc.
     *
     * @return true if the disc has been flipped; false otherwise.
     */
    boolean getFliiped();
    /**
     * Resets all flags associated with this disc.
     * This is typically used during resets or to clear temporary states (e.g., flipped or bomb flags).
     */
    void resetFlags();

}