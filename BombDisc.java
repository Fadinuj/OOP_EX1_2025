public class BombDisc implements Disc{

    Player player;

    public BombDisc(Player currentPlayer) {
        this.player = currentPlayer;
        player.reduce_bomb();
    }

    @Override
    public Player getOwner() {
        return player;
    }

    @Override
    public void setOwner(Player player)
    {
        this.player = player;
    }

    @Override
    public String getType() {
        return "💣";
    }
}
