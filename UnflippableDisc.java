public class UnflippableDisc implements Disc {
    Player owner;
    public UnflippableDisc(Player currentPlayer) {
        owner = currentPlayer;
        currentPlayer.reduce_unflippedable();
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    @Override
    public String getType() {
        return "⭕";
    }
}
