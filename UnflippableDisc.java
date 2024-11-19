public class UnflippableDisc implements Disc {
    private Player player;
    private final boolean flipped=false;
    private boolean bomb=false;


    public UnflippableDisc(Player currentPlayer) {
        player = currentPlayer;
        currentPlayer.reduce_unflippedable();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String getType() {
        return "⭕";
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
    }

    @Override
    public boolean getFliiped() {
        return false;
    }

    @Override
    public void resetFlags() {
    }
}
