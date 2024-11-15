import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private final int getBoardSize=8;
    private Player player1,player2;
    private Player lastPlayer;
    private Disc[][] boardDiscs=new Disc[getBoardSize][getBoardSize];
    private Stack<Move> moveHistory = new Stack<>();
    private Stack<Disc> flipHistory = new Stack<>();



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

        if (row >= 0 && row < getBoardSize() && col >= 0 && col < getBoardSize() && boardDiscs[row][col] == null && countFlips(a) > 0)
        {

            moveHistory.push(new Move(a, disc, boardDiscs[row][col]));

            
            boardDiscs[row][col] = disc;

            
            System.out.printf("Player %d placed a %s in (%d, %d)%n",
                    (disc.getOwner().isPlayerOne() ? 1 : 2),
                    disc.getType(),
                    a.getRow(),
                    a.getCol());

            
            flipDiscs(a, disc.getOwner());

            
            lastPlayer = disc.getOwner();

            return true;
        }
        return false;
    }
    public void flipDiscs(Position position, Player currentPlayer) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] direction : directions) {
            int rowDelta = direction[0];
            int colDelta = direction[1];

            if (getCountFlips(position, rowDelta, colDelta) > 0) {
                flipInDirection(position, rowDelta, colDelta, currentPlayer);
            }
        }
    }
    private List<Position> flipInDirection(Position position, int rowDelta, int colDelta, Player currentPlayer) {
        List<Position> flippedDiscs = new ArrayList<>();
        int row = position.getRow() + rowDelta;
        int col = position.getCol() + colDelta;

        while (row >= 0 && row < getBoardSize() && col >= 0 && col < getBoardSize()) {
            Disc disc = boardDiscs[row][col];

            if (disc == null) {
                return flippedDiscs;
            } else if (disc instanceof UnflippableDisc) {
                return flippedDiscs;
            } else if (disc.getOwner() == currentPlayer) {
                return flippedDiscs;
            } else {
                disc.setOwner(currentPlayer);
                flippedDiscs.add(new Position(row, col));
            }

            row += rowDelta;
            col += colDelta;
        }
        return flippedDiscs;
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
    private List<Position> getValidMoves(Player player) {
        List<Position> validMoves = new ArrayList<>();
        for (int row = 0; row < getBoardSize(); row++) {
            for (int col = 0; col < getBoardSize(); col++) {
                Position position = new Position(row, col);
                if (boardDiscs[row][col] == null && countFlipsForPlayer(position, player) > 0) {
                    validMoves.add(position);
                }
            }
        }
        return validMoves;
    }
    private int countFlipsForPlayer(Position a, Player player) {
        int totalFlips = 0;
        totalFlips += getCountFlips(a, -1, 0);    // UP
        totalFlips += getCountFlips(a, 1, 0);     // DOWN
        totalFlips += getCountFlips(a, 0, 1);     // RIGHT
        totalFlips += getCountFlips(a, 0, -1);    // LEFT
        totalFlips += getCountFlips(a, -1, 1);    // UP-RIGHT
        totalFlips += getCountFlips(a, -1, -1);   // UP-LEFT
        totalFlips += getCountFlips(a, 1, -1);    // DOWN-LEFT
        totalFlips += getCountFlips(a, 1, 1);     // DOWN-RIGHT
        return totalFlips;
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

        
        while (row >= 0 && row < getBoardSize() && col >= 0 && col < getBoardSize()) {
            Disc disc = boardDiscs[row][col];

            if (disc == null) {
                return 0;  
            } else if (disc.getOwner() == currentPlayer) {
                return countFlips;  
            } else {
                countFlips++;
            }

            row += rowDelta;
            col += colDelta;
        }
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

    private int countDiscs(Player player) {
        int count = 0;
        for (int row = 0; row < getBoardSize(); row++) {
            for (int col = 0; col < getBoardSize(); col++) {
                Disc disc = boardDiscs[row][col];
                if (disc != null && disc.getOwner() == player) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < getBoardSize(); row++) {
            for (int col = 0; col < getBoardSize(); col++) {
                if (boardDiscs[row][col] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateWins() {
        int player1Count = countDiscs(player1);
        int player2Count = countDiscs(player2);

        if (player1Count > player2Count) {
            player1.addWin(); 
            System.out.printf("Player 1 wins with %d discs! Player 2 had %d discs.%n", player1Count, player2Count);
        } else if (player2Count > player1Count) {
            player2.addWin(); 
            System.out.printf("Player 2 wins with %d discs! Player 1 had %d discs.%n", player2Count, player1Count);
        } else {
            System.out.printf("It's a tie! Both players have %d discs.%n", player1Count);
        }
    }

    @Override
    public boolean isGameFinished() {
        boolean player1HasMoves = !getValidMoves(player1).isEmpty();
        boolean player2HasMoves = !getValidMoves(player2).isEmpty();

        
        if ((!player1HasMoves && !player2HasMoves) || isBoardFull()) {
            updateWins(); 
            return true;
        }

        return false;
    }


    @Override
    public void reset() {
        initBoard();
    }

    @Override
    public void undoLastMove() {
        if (!moveHistory.isEmpty() ) {
            Move lastMove = moveHistory.pop();


            boardDiscs[lastMove.getPosition().getRow()][lastMove.getPosition().getCol()] = lastMove.getPreviousDisc();


            lastPlayer = (lastPlayer == player1) ? player2 : player1;

            System.out.printf("Undoing last move:%n");
            System.out.printf("\tUndo: removing %s from (%d, %d)%n",
                    lastMove.getDisc().getType(),
                    lastMove.getPosition().getRow(),
                    lastMove.getPosition().getCol());

            if (lastMove.getPreviousDisc() != null) {
                System.out.printf("\tUndo: flipping back %s in (%d, %d)%n",
                        lastMove.getPreviousDisc().getType(),
                        lastMove.getPosition().getRow(),
                        lastMove.getPosition().getCol());
            }
        } else {
            System.out.println("\tNo previous move available to undo.");
        }


    }
}
