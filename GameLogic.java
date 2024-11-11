import java.util.List;

public class GameLogic implements PlayableLogic {
    private final int getBoardSize=8;
    private Player player1,player2;
    private Player lastPlayer;
    private Disc[][] boardDiscs=new Disc[getBoardSize][getBoardSize];


    public void initBoard()
    {
        boardDiscs=new Disc[getBoardSize][getBoardSize];

        int middle=getBoardSize/2;
        boardDiscs[middle][middle]=new SimpleDisc(player1);
        boardDiscs[middle-1][middle-1]=new SimpleDisc(player1);
        boardDiscs[middle][middle-1]=new SimpleDisc(player2);
        boardDiscs[middle-1][middle]=new SimpleDisc(player2);
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        int row = a.getRow();
        int col = a.getCol();
        if(lastPlayer!=player1) {
            boardDiscs[row][col] = disc;
            boardDiscs[row][col].setOwner(player1);
            lastPlayer = player1;
            return true;
        }
        else {
            boardDiscs[row][col] = disc;
            boardDiscs[row][col].setOwner(player2);
            lastPlayer = player2;
            return true;
        }


    }


    @Override
    public Disc getDiscAtPosition(Position position) {
        return boardDiscs[position.getRow()][position.getCol()];
    }

    @Override
    public int getBoardSize() {
        return getBoardSize;
    }

    @Override
    public List<Position> ValidMoves() {
        return List.of();
    }

    @Override
    public int countFlips(Position a) {
        return 0;
    }

    @Override
    public Player getFirstPlayer() {
        return player1;
    }

    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public boolean isFirstPlayerTurn() {
        return lastPlayer!=player1;
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    @Override
    public void reset() {
        initBoard();
    }

    @Override
    public void undoLastMove() {

    }
}
