public class BombDisc implements Disc{

    Player owner;

    public BombDisc(Player currentPlayer) {
        this.owner = currentPlayer;
        owner.reduce_bomb();
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Player player)
    {
        this.owner = player;
    }

    @Override
    public String getType() {
        return "💣";
    }
}
