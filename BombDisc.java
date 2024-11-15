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

    public void explode(Disc[][] board, Position position) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] dir : directions) {
            int newRow = position.getRow() + dir[0];
            int newCol = position.getCol() + dir[1];

            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {
                board[newRow][newCol] = null; // מפנה את התא
            }
        }
    }
}
