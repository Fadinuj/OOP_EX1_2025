import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private final int getBoardSize=8;
    private Player player1,player2;
    private Player lastPlayer;
    private boolean flag;
    private Disc[][] boardDiscs;
    private final Boolean[][] countedArr;
    private final Stack<Position> moveHistory;
    private final Stack<Position> flipPositions1;
    private final Stack<Disc> flipHistory; // Collects data of setOwner.
    private final Stack<Integer> undoCountStack;
    Stack<Position> discsFlipStackerCopy;

    /**
     * Initializes the game logic with an empty board and default states.
     */
    public GameLogic(){
        super();
        boardDiscs=new Disc[getBoardSize][getBoardSize];
        flipHistory=new Stack<>();
        countedArr=new Boolean[getBoardSize][getBoardSize];
        discsFlipStackerCopy = new Stack<>();
        flipPositions1 = new Stack<>();
        moveHistory = new Stack<>();
        undoCountStack = new Stack<>();
        this.lastPlayer = getSecondPlayer();

    }
    /**
     * @return The current player (Player 1 or Player 2) based on the last move.
     */
    public Player getCurrentPlayer(){
        if (lastPlayer ==player1)
            return player2;
        return player1;
    }
    /**
     * @param p The player whose numerical ID is requested.
     * @return 1 if the player is Player 1, 2 if the player is Player 2.
     */
    private int getNumOfPlayer(Player p) {
        if (p.equals(player1)) {
            return 1;
        } else {
            return 2;
        }
    }
    /**
     * Resets the boolean array tracking flipped positions during gameplay.
     */
    private void resetCountArr()
    {
        for (int i = 0; i < getBoardSize; i++) {
            for (int j = 0; j < getBoardSize; j++) {
                countedArr[i][j]=false;
            }

        }
    }
    /**
     * Initializes the board with the default starting discs for Player 1 and Player 2.
     */
    public void initBoard()
    {
        boardDiscs = new Disc[getBoardSize][getBoardSize];
        int middle = getBoardSize / 2;
        boardDiscs[middle][middle] = new SimpleDisc(player1);
        boardDiscs[middle - 1][middle - 1] = new SimpleDisc(player1);
        boardDiscs[middle][middle - 1] = new SimpleDisc(player2);
        boardDiscs[middle - 1][middle] = new SimpleDisc(player2);
    }
    /**
     * Attempts to place a disc at the given position on the board.
     *
     * @param a The position to place the disc.
     * @param disc The disc to be placed.
     * @return True if the move is valid and the disc is placed, otherwise false.
     */
    @Override
    public boolean locate_disc(Position a, Disc disc) {
        boolean f=false;
       if (boardDiscs[a.getRow()][a.getCol()] == null && countFlips(a) > 0)
        {
            flag=true;
            f = locate_disc_v(a, disc);
        }
       return f;
    }
    /**
     * Helper function for locate_disc. Places the given disc at the specified position
     * and flips the relevant discs.
     *
     * @param a The position to place the disc.
     * @param disc The disc to be placed.
     * @return True if the move is successful, otherwise false.
     */
    public boolean locate_disc_v(Position a, Disc disc) {
        Player player = getCurrentPlayer();


        if (disc.getType().equals("💣"))
        {
            if (player.getNumber_of_bombs() > 0) {
                player.reduce_bomb();
                boardDiscs[a.getRow()][a.getCol()] = disc;
                moveHistory.addLast(new Position(a.getRow(), a.getCol()));
                flipDiscs(a, disc.getOwner());
                System.out.printf("Player %d placed a %s in (%d,%d)\n", getNumOfPlayer(player), disc.getType(), a.getRow(), a.getCol());
                System.out.println();
                lastPlayer = player;
                return true;
            }
        } else if (disc.getType().equals( "⭕")) {
            if (player.getNumber_of_unflippedable() > 0) {
                player.reduce_unflippedable();
                boardDiscs[a.getRow()][a.getCol()] = disc;
                moveHistory.addLast(new Position(a.getRow(), a.getCol()));
                flipDiscs(a, disc.getOwner());
                System.out.printf("Player %d placed a %s in (%d,%d)" + "\n ", getNumOfPlayer(player), disc.getType(), a.getRow(), a.getCol());
                System.out.println();
                lastPlayer = player;
                return true;
            }

        } else if (disc.getType().equals("⬤")) {
            boardDiscs[a.getRow()][a.getCol()] = disc;
            moveHistory.addLast(new Position(a.getRow(), a.getCol()));
            flipDiscs(a, disc.getOwner());
            System.out.printf("Player %d placed a %s in (%d,%d)\n", getNumOfPlayer(player), disc.getType(), a.getRow(), a.getCol());
            System.out.println();
            lastPlayer = player;
            return true;
        }
        return false;
    }
    /**
     * Flips the discs in all directions based on the rules of the game.
     *
     * @param a The position where the last disc was placed.
     * @param currentPlayer The current player performing the flip.
     */
    public void flipDiscs(Position a, Player currentPlayer) {

        int undoCount = 0;
        Stack<Position> discsFlipStackerCheck = new Stack<>();

         //עבור כל כיוון (מעל, מתחת, ימינה, שמאלה, ובאלכסונים)
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, 1}, {0, -1}, // Up, Down, Right, Left
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonal directions
        };

        for (int[] direction : directions) {

            int rowDelta = direction[0];

           int colDelta = direction[1];

            // אם יש דיסקים שיכולים להתהפך בכיוון הזה
            if (getCountFlips(a, rowDelta, colDelta) > 0) {
                // התחלת סריקה בכיוון הזה
                int row = a.getRow() + rowDelta;
                int col = a.getCol() + colDelta;

                while (isWithinBoardBounds(row, col)) {
                    Disc disc = boardDiscs[row][col];

                    // אם הדיסק הוא של היריב וניתן להפוך אותו
                    if (disc != null && disc.getOwner().equals(lastPlayer) && !disc.getType().equals("⭕") && !disc.getType().equals("💣")) {
                        discsFlipStackerCheck.push(new Position(row, col));
                    }
                    else if (disc!=null && disc.getOwner().equals(lastPlayer) && disc.getType().equals("💣")) {
                        discsFlipStackerCheck= flipBomb(row , col , discsFlipStackerCheck);
                    }
                    // אם הדיסק שייך לשחקן הנוכחי, כל הדיסקים שהיו במסלול צריכים להתהפך
                    else if (disc != null && disc.getOwner().equals(currentPlayer)) {
                        while (!discsFlipStackerCheck.isEmpty()) {
                            Position pos = discsFlipStackerCheck.pop();
                            Disc d = boardDiscs[pos.getRow()][pos.getCol()];
                            d.setOwner(currentPlayer);  // הופך את בעלות הדיסק
                            d.setFliiped(true); // מסמן שהדיסק הפך
                            System.out.printf("Player %s flipped the %s in (%d,%d)\n",getNumOfPlayer(currentPlayer) , d.getType(), pos.getRow(), pos.getCol());
                            flipHistory.push(d); // הוסף את הדיסק להיסטוריית ההפיכות
                            discsFlipStackerCopy.push(pos); // שומר את המיקומים שהפכו
                            undoCount++; // עדכון מניין ההפיכות
                        }
                        break;  // יציאה אם הגענו לדיסק של השחקן הנוכחי
                    }
                    // אם מצאנו דיסק ריק או בלתי הפיך, יציאה מהלולאה
                    else {
                        break;
                    }

                    // המשך לכיוון הבא
                    row += rowDelta;
                    col += colDelta;
                }
            }
        }

        undoCountStack.add(undoCount); // הוסף את מספר ההפיכות למחסנית ההפיכות
        flag = false; // סיום ההפיכה
    }
    /**
     * Handles flipping discs caused by a bomb placed on the board.
     *
     * @param row The row of the bomb.
     * @param col The column of the bomb.
     * @param discsFlipStackerCheck A stack tracking the positions of discs to flip.
     * @return The updated stack containing all positions of discs to flip.
     */
    public Stack<Position> flipBomb(int row , int col ,Stack<Position> discsFlipStackerCheck ) {
        discsFlipStackerCheck.push(new Position(row, col));
        for (int i = -1; i <=1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isWithinBoardBounds(row+i, col+j)) {
                    Disc disc2 = boardDiscs[row+i][col+j];
                    if(disc2 != null && disc2.getOwner().equals(lastPlayer) && !disc2.getType().equals("⭕"))
                    {
                        if (disc2.getType().equals("💣"))
                        {
                           discsFlipStackerCheck= flipBombDouble(row+i, col+j, discsFlipStackerCheck);
                        }
                        else {
                        discsFlipStackerCheck.push(new Position(row+i, col+j));
                        }
                    }
                }
            }

        }
        return discsFlipStackerCheck;
    }
    /**
     * Helper function to handle recursive bomb explosions caused by another bomb.
     *
     * @param row The row of the secondary bomb.
     * @param col The column of the secondary bomb.
     * @param discsFlipStackerCheck A stack tracking positions of discs affected by the explosions.
     * @return The updated stack containing positions of affected discs.
     */
    public Stack<Position> flipBombDouble(int row , int col ,Stack<Position> discsFlipStackerCheck ) {
        discsFlipStackerCheck.push(new Position(row, col));
        for (int i = -1; i <=1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isWithinBoardBounds(row+i, col+j)) {
                    Disc disc2 = boardDiscs[row+i][col+j];
                    if(disc2 != null && disc2.getOwner().equals(lastPlayer) && !disc2.getType().equals("⭕"))
                    {
                        discsFlipStackerCheck.push(new Position(row+i, col+j));
                    }
                }
            }

        }
        return discsFlipStackerCheck;
    }
    /**
     * @param position The position on the board.
     * @return The disc located at the specified position.
     */
    @Override
    public Disc getDiscAtPosition(Position position) {
        return boardDiscs[position.getRow()][position.getCol()];
    }
    /**
     * @return The size of the board.
     */
    @Override
    public int getBoardSize() {
        return getBoardSize;
    }
    /**
     * Finds all valid positions where the current player can place a disc.
     *
     * @return A list of valid positions for the current move.
     */
    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoves = new ArrayList<>();
        for (int i = 0; i < getBoardSize; i++) {
            for (int j = 0; j < getBoardSize; j++) {
                if (boardDiscs[i][j] == null) {
                    if (countFlips(new Position(i, j)) > 0) {
                        validMoves.add(new Position(i, j));
                    }
                }
            }
        }
        return validMoves;
    }
    /**
     * Counts the total number of discs that would be flipped if a disc were placed at the given position.
     *
     * @param a The position to evaluate.
     * @return The number of discs that would be flipped.
     */
    @Override
    public int countFlips(Position a) {
        int totalFlips = 0;
        totalFlips += getCountFlips(a, -1, 0);    // UP
        totalFlips += getCountFlips(a, 1, 0);     // DOWN
        totalFlips += getCountFlips(a, 0, 1);     // RIGHT
        totalFlips += getCountFlips(a, 0, -1);    // LEFT
        totalFlips += getCountFlips(a, -1, 1);    // UP-RIGHT
        totalFlips += getCountFlips(a, -1, -1);   // UP-LEFT
        totalFlips += getCountFlips(a, 1, -1);    // DOWN-LEFT
        totalFlips += getCountFlips(a, 1, 1);     // DOWN-RIGHT
        resetCountArr();
        return totalFlips;
    }
    /**
     * Helper function to count discs to flip in a specific direction.
     *
     * @param a The position to evaluate.
     * @param rowDelta The row direction to check.
     * @param colDelta The column direction to check.
     * @return The number of discs to flip in the specified direction.
     */
    public int getCountFlips(Position a,int rowDelta,int colDelta)
    {
        int flipCounter = 0; // מונה את כמות ההפיכות
        ArrayList<Position> validFlips = new ArrayList<>();
        Stack<Position> flipPositionStack = new Stack<>();
        int row = a.getRow() + rowDelta;
        int col = a.getCol() + colDelta;
        // סריקה בכיוון הנתון כל עוד אנחנו בתוך גבולות הלוח
        while (isWithinBoardBounds(row, col)) {
            Disc disc = boardDiscs[row][col];

            // אם נתקלנו בריבוע ריק, אין אפשרות להפוך בכיוון זה
            if (disc == null) {
                flipCounter=0;
                while (!validFlips.isEmpty()) {
                    Position position = validFlips.removeFirst();
                    countedArr[position.getRow()][position.getCol()]=false;
                }
                break;
            }

            if (disc.getOwner()==lastPlayer ) {
                if (!(isWithinBoardBounds(row+rowDelta, col+colDelta)))
                {
                    return 0;
                }
                // אם הדיסק שייך לשחקן היריב
                if (disc.getType().equals("💣") && !countedArr[row][col]) {
                    validFlips.add(new Position(row, col));
                    countedArr[row][col]=true;
                    // טיפול בפיצוץ
                    validFlips = handleExplosion(new Position(row,col),validFlips);
                    flipCounter =validFlips.size();
                    for (int i = 0; i < getBoardSize; i++) {
                        for (int j = 0; j < getBoardSize; j++) {
                            if(boardDiscs[i][j] != null) {
                                boardDiscs[i][j].resetFlags();
                            }
                        }
                    }

                }
                else if (disc.getType().equals("⬤")) {
                    // אם מדובר בדיסק רגיל
                    if (!validFlips.contains(new Position(row, col)) && !countedArr[row][col]) {
                        validFlips.add(new Position(row, col));
                        countedArr[row][col]=true;
                    }
                    if (!flipPositionStack.contains(new Position(row, col))) {
                        flipPositionStack.push(new Position(row,col));
                    }
                }
            } else if (disc.getOwner()==getCurrentPlayer()) {
                    flipCounter = validFlips.size();
                    validFlips.clear();
                    return flipCounter;
            } else {
                // אם נתקלנו בדיסק שאינו הפיך (UnflippableDisc)
                break;
            }
            // המשך לסרוק בכיוון הנתון
            row += rowDelta;
            col += colDelta;
        }
        while (!flipPositionStack.isEmpty()) {
            flipPositions1.push(flipPositionStack.pop());
        }
        return flipCounter;
    }
    /**
     * Handles the explosion caused by a bomb disc. Flips the affected discs and propagates the explosion
     * to nearby discs, including bombs that trigger further explosions.
     *
     * @param a The position of the bomb that triggered the explosion.
     * @param validFlips A list of positions that represent the discs to be flipped as a result of the explosion.
     * @return The updated list of positions representing all affected discs, including further bomb explosions.
     */
    public ArrayList<Position> handleExplosion(Position a, ArrayList<Position> validFlips) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, 1}, {0, -1}, // Up, Down, Right, Left
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonal directions
        };

        for (int[] direction : directions) {
            int rowDelta = direction[0];
            int colDelta = direction[1];
            // התעלם מהכיוון המרכזי שבו יש את הפצצה

            int newRow = a.getRow() + rowDelta;
            int newCol = a.getCol() + colDelta;

            if (isWithinBoardBounds(newRow, newCol) && !(boardDiscs[newRow][newCol] == null) && !boardDiscs[newRow][newCol].getFlagBomb()) {
                Disc currentDisc = boardDiscs[newRow][newCol];
                if(boardDiscs[newRow][newCol].getType().equals("💣") && boardDiscs[newRow][newCol].getOwner()==lastPlayer) {
                    if (!validFlips.contains(new Position(newRow, newCol)) && !countedArr[newRow][newCol]) {
                        validFlips.add(new Position(newRow, newCol));
                        countedArr[newRow][newCol]=true;
                        boardDiscs[newRow][newCol].setFlagBomb(true);
                        validFlips = handleExplosion(new Position(newRow, newCol), validFlips);
                    }
                }
                // אם הדיסק של היריב, ניתן להפוך אותו
                else if (boardDiscs[newRow][newCol].getOwner().equals(lastPlayer) && boardDiscs[newRow][newCol].getType().equals("⬤")) {
                    if (!validFlips.contains(new Position(newRow, newCol)) && !countedArr[newRow][newCol]) {
                        validFlips.add(new Position(newRow, newCol));
                        countedArr[newRow][newCol]=true;
                        currentDisc.setFlagBomb(true);
                    }
                }
            }
        }
        return validFlips; // מחזיר את מספר ההפיכות מהפיצוץ
    }
    /**
     * Checks if the given row and column are within the board's bounds.
     *
     * @param row The row index.
     * @param col The column index.
     * @return True if the position is within bounds, otherwise false.
     */
    private boolean isWithinBoardBounds(int row, int col) {
        return (row >= 0 && row < getBoardSize() && col >= 0 && col < getBoardSize());
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
        return lastPlayer != player1;
    }
    /**
     * Checks if the game has ended and determines the winner or if the game is a tie.
     *
     * @return True if the game is finished, otherwise false.
     */
    @Override
    public boolean isGameFinished() {
        int player1_discs = 0, player2_discs = 0;

        // בדיקה אם אין מהלכים חוקיים זמינים
        if (this.ValidMoves().isEmpty()) {
            // ספירת הדיסקים של כל שחקן
            for (int i = 0; i < getBoardSize; i++) {
                for (int j = 0; j < getBoardSize; j++) {
                    if (boardDiscs[i][j] != null && boardDiscs[i][j].getOwner().equals(player1)) {
                        player1_discs++;
                    } else if (boardDiscs[i][j] != null && boardDiscs[i][j].getOwner().equals(player2)) {
                        player2_discs++;
                    }
                }
            }

            // עדכון מנצח או טיפול במצב תיקו
            if (player1_discs > player2_discs) {
                getFirstPlayer().addWin();
                System.out.printf("Player %s wins with %d discs! Player %s had %d discs.\n",
                        getNumOfPlayer(player1), player1_discs,
                        getNumOfPlayer(player2), player2_discs);
            } else if (player1_discs < player2_discs) {
                getSecondPlayer().addWin();
                System.out.printf("Player %s wins with %d discs! Player %s had %d discs.\n",
                        getNumOfPlayer(player2), player2_discs,
                        getNumOfPlayer(player1), player1_discs);
            } else {
                // במצב של תיקו, לא מוסיפים ניצחונות ומבצעים איפוס
                System.out.printf("It's a tie! Both players have %d discs.\n", player1_discs);
                reset(); // איפוס המשחק
            }
            return true; // המשחק הסתיים
        }

        return false; // המשחק לא הסתיים
    }
    /**
     * Resets the game state to its initial configuration.
     */
    @Override
    public void reset() {
        lastPlayer=player2;
        moveHistory.clear();
        undoCountStack.clear();
        flipHistory.clear();
        player1.reset_bombs_and_unflippedable();
        player2.reset_bombs_and_unflippedable();
        initBoard();
    }
    /**
     * Undoes the last move, including reverting any discs that were flipped.
     */
    @Override
    public void undoLastMove() {
        if (!moveHistory.isEmpty()) {
            // החזרת המיקום האחרון
            Position lastMove = moveHistory.pop();
            Disc removedDisc = boardDiscs[lastMove.getRow()][lastMove.getCol()];
            if (removedDisc.getType().equals("💣")) {
                removedDisc.getOwner().increase_bomb();
            } else if (removedDisc.getType().equals("⭕")) {
                removedDisc.getOwner().increase_unflippedable();
            }
            boardDiscs[lastMove.getRow()][lastMove.getCol()] = null; // מסיר את הדיסק מהמיקום
            System.out.println("Undoing last move:");
            System.out.printf("\tUndo: removing %s from (%d, %d)\n",
                    removedDisc.getType(),
                    lastMove.getRow(), lastMove.getCol());

            if (!undoCountStack.isEmpty()) {
                int flipsToUndo = undoCountStack.pop();

                for (int i = 0; i < flipsToUndo; i++) {
                    if (!flipHistory.isEmpty()) {
                        Position flippedPos = discsFlipStackerCopy.pop();
                        Disc flippedDisc = boardDiscs[flippedPos.getRow()][flippedPos.getCol()];
                        // משחזר את הבעלות הקודמת
                        flippedDisc.setOwner(getCurrentPlayer());
                        System.out.printf("\tUndo: flipping back %s in (%d, %d)\n",
                                flippedDisc.getType(),
                                flippedPos.getRow(), flippedPos.getCol());
                        System.out.println();
                    }
                }
            }
            lastPlayer = (lastPlayer == player1) ? player2 : player1;
        } else {
            System.out.println("Undoing last move:");
            System.out.println("\tNo previous move available to undo.");
            System.out.println();
        }
    }
}
