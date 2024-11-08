public class SimpleDisc implements Disc {
    Player player;
    public SimpleDisc(Player currentPlayer) {
        this.player = currentPlayer;
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
        return "⬤";
    }
}
