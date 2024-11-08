public class UnflippableDisc implements Disc {
    Player player;
    public UnflippableDisc(Player currentPlayer) {
        player = currentPlayer;
        currentPlayer.reduce_unflippedable();
    }

    @Override
    public Player getOwner() {
        return player;
    }

    @Override
    public void setOwner(Player player) {
        this.player = player;
    }

    @Override
    public String getType() {
        return "⭕";
    }
}
