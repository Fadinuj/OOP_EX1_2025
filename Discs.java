public class Discs implements Disc{
    private Player owner;
    public Discs() {}
    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Player player) {
        this.owner=player;
    }

    @Override
    public String getType() {
        return "";
    }
}
