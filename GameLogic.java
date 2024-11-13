import java.util.ArrayList;
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

        // Check if the position is within bounds and the cell is empty
        if (row >= 0 && row < getBoardSize() && col >= 0 && col < getBoardSize() && boardDiscs[row][col] == null) {
            boardDiscs[row][col] = disc; // Place the disc at the specified position
            lastPlayer = disc.getOwner(); // Update lastPlayer to the current disc's owner
            return true; // Return true to indicate successful placement
        }

        return false; // Return false if the position is out of bounds or already occupied

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
        List<Position> validMoves = new ArrayList<>();
        for (int i = 0; i < getBoardSize; i++) {
            for (int j = 0; j < getBoardSize; j++) {
                if (boardDiscs[i][j]==null)
                {
                    if (countFlips(new Position(i,j))>0)
                    {
                        validMoves.add(new Position(i,j));
                    }
                }

            }

        }
        return validMoves;

    }



    @Override
    public int countFlips(Position a) {
       int totalFlips=0;
       totalFlips+=getCountFlips(a,-1,0);    //UP
        totalFlips+=getCountFlips(a,1,0);   //DOWN
        totalFlips+=getCountFlips(a,0,1);   //RIGHT
        totalFlips+=getCountFlips(a,0,-1);  //LEFT
        totalFlips+=getCountFlips(a,-1,1);  //UP-RIGHT
        totalFlips+=getCountFlips(a,-1,-1); //UP-LEFT
        totalFlips+=getCountFlips(a,1,-1);  //DOWN-LEFT
        totalFlips+=getCountFlips(a,1,1);   //DOWN-RIGHT
        return totalFlips;
    }
    public int getCountFlips(Position a,int row,int col)
    {
        int countFlips=0,retunCounter=0;
        for (int i =a.getCol()+col, j=a.getRow()+row; i <getBoardSize && j>=0 ; i++,j--) {
            if (boardDiscs[i][j]!=null &&boardDiscs[i][j].getOwner().equals(lastPlayer))
            {
                countFlips++;
            } else if (boardDiscs[i][j]!=null &&boardDiscs[i][j].getOwner().equals()) {
                retunCounter+=countFlips;
                break;
            }
            else {
                break;
            }
        }
        return retunCounter;
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
