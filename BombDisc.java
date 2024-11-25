public class BombDisc implements Disc{

    private Player player;
    private boolean fliiped;
    private boolean bomb=false;

    public BombDisc(Player currentPlayer) {
        this.player = currentPlayer;
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

    @Override
    public boolean getFlagBomb() {
        return bomb;
    }

    @Override
    public void setFlagBomb(boolean flag) {
        this.bomb=flag;
    }

    @Override
    public void setFliiped(boolean flag) {
        this.fliiped=flag;
    }

    @Override
    public boolean getFliiped() {
        return fliiped;
    }

    @Override
    public void resetFlags() {
        this.bomb=false;
        this.fliiped=false;
    }

}
