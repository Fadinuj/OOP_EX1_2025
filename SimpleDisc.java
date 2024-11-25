public class SimpleDisc implements Disc {
    private Player player;
    private boolean bomb=false;
    private boolean flipped;
    private boolean sFlagBomb;
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
        this.flipped=flag;
    }

    @Override
    public boolean getFliiped() {
        return flipped;
    }

    @Override
    public void resetFlags() {
        this.flipped=false;
        this.bomb=false;
    }
    public void setFlipped(boolean flag) {
        this.flipped=flag;
    }
    public boolean getFlipped() {
        return flipped;
    }

    public boolean getSFlagBomb() {
        return sFlagBomb;
    }

    public void setSFlagBomb(boolean sFlagBomb) {
        this.sFlagBomb = sFlagBomb;
    }
}
