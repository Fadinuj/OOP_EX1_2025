import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private final int getBoardSize=8;
    private Player player1,player2;
    private Player lastPlayer;
    private Disc[][] boardDiscs=new Disc[getBoardSize][getBoardSize];
    private Stack<Move> moveHistory = new Stack<>();


    public void initBoard()
    {
        boardDiscs = new Disc[getBoardSize][getBoardSize];
        int middle = getBoardSize / 2;
        boardDiscs[middle][middle] = new SimpleDisc(player1);
        boardDiscs[middle - 1][middle - 1] = new SimpleDisc(player1);
        boardDiscs[middle][middle - 1] = new SimpleDisc(player2);
        boardDiscs[middle - 1][middle] = new SimpleDisc(player2);
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        int row = a.getRow();
        int col = a.getCol();

        if (row >= 0 && row < getBoardSize && col >= 0 && col < getBoardSize && boardDiscs[row][col] == null) {
            // Save the current state before placing the new disc
            moveHistory.push(new Move(a, disc, boardDiscs[row][col]));
            boardDiscs[row][col] = disc; // Place the disc on the board
            lastPlayer = disc.getOwner(); // Update the last player
            return true;
        }
        return false;
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
    public int getCountFlips(Position a,int rowDelta,int colDelta)
    {
        int countFlips = 0;
        int row = a.getRow() + rowDelta;
        int col = a.getCol() + colDelta;
        Player currentPlayer = isFirstPlayerTurn() ? player1 : player2;

        // נמשיך בכיוון כל עוד אנחנו בתוך הגבולות
        while (row >= 0 && row < getBoardSize() && col >= 0 && col < getBoardSize()) {
            Disc disc = boardDiscs[row][col];

            if (disc == null) {
                return 0;  // נתקלנו ברווח, אין הפיכות בכיוון זה
            } else if (disc.getOwner() == currentPlayer) {
                return countFlips;  // חזרנו לשחקן הנוכחי, מחזירים את כמות ההפיכות
            } else {
                countFlips++;  // דיסק של היריב, ממשיכים לספור
            }

            row += rowDelta;
            col += colDelta;
        }
        return 0;  // לא נמצאה אפשרות הפיכה בכיוון זה
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
        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            boardDiscs[lastMove.getPosition().getRow()][lastMove.getPosition().getCol()] = lastMove.getPreviousDisc();
            lastPlayer = (lastPlayer == player1) ? player2 : player1;
        }
    }
}
