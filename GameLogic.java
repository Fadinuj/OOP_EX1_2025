import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private final int getBoardSize=8;
    private Player player1,player2;
    private Player lastPlayer;
    private boolean flag;
    private Disc[][] boardDiscs;
    private Boolean[][] countedArr;
    private Stack<Position> moveHistory;
    private Stack<Position> flipPositions1;
    private final Stack<Disc> flipHistory; // Collects data of setOwner.
    private final Stack<Integer> undoCountStack;
    Stack<Position> discsFlipStackerCopy;


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
    public Player getCurrentPlayer(){
        if (lastPlayer ==player1)
            return player2;
        return player1;
    }
    private int getNumOfPlayer(Player p) {
        if (p.equals(p)) {
            return 1;
        } else {
            return 2;
        }
    }
    private void resetCountArr()
    {
        for (int i = 0; i < getBoardSize; i++) {
            for (int j = 0; j < getBoardSize; j++) {
                countedArr[i][j]=false;
            }

        }
    }

    public void initBoard()
    {
        boardDiscs = new Disc[getBoardSize][getBoardSize];
        int middle = getBoardSize / 2;
        boardDiscs[middle][middle] = new SimpleDisc(player1);
        boardDiscs[middle - 1][middle - 1] = new SimpleDisc(player1);
        boardDiscs[middle][middle - 1] = new SimpleDisc(player2);
        boardDiscs[middle - 1][middle] = new SimpleDisc(player2);
    }
    private void flagReset() {
        // נניח שבדיסק יש דגל שנקרא flagBomb, עלינו להחזיר אותו למצב ברירת המחדל שלו
        for (int i = 0; i < getBoardSize(); i++) {
            for (int j = 0; j < getBoardSize(); j++) {
                Disc disc = boardDiscs[i][j];
                if (disc != null) {
                    disc.setFlagBomb(false);  // איפוס הדגל לכל הדיסקים על הלוח
                }
            }
        }
    }

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

    public boolean locate_disc_v(Position a, Disc disc) {
        Player player = getCurrentPlayer();


        if (disc.getType().equals("💣"))
        {
            if (player.getNumber_of_bombs() > 0) {
                player.reduce_bomb();
                boardDiscs[a.getRow()][a.getCol()] = disc;
                moveHistory.addLast(new Position(a.getRow(), a.getCol()));
                flipDiscs(a, disc.getOwner());
                //System.out.printf("Player %d placed a %s in (%d,%d)\n No. of Bombs discs left: %d\n", no, disc.getType(), a.row(), a.col(), p1.getNumber_of_bombs());
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
                //System.out.printf("Player %d placed a %s in (%d,%d)" + "\n No. of Unflippable discs left: %d\n", no, disc.getType(), a.row(), a.col(), p1.getNumber_of_unflippedable());
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
                        if (disc.getType().equals("💣"))
                        {
                            discsFlipStackerCheck= flipBomb(row , col , discsFlipStackerCheck);
                        }
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
    public Stack<Position> flipBomb(int row , int col ,Stack<Position> discsFlipStackerCheck ) {
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
                if (boardDiscs[i][j] == null) {
                    if (countFlips(new Position(i, j)) > 0) {
                        validMoves.add(new Position(i, j));
                    }
                }
            }
        }
        return validMoves;
    }



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
        return totalFlips;
    }
    public int getCountFlips(Position a,int rowDelta,int colDelta)
    {
        int flipCounter = 0; // מונה את כמות ההפיכות
        List<Position> validFlips = new ArrayList<>();
        Stack<Position> flipPositionStack = new Stack<>();
        int row = a.getRow() + rowDelta;
        int col = a.getCol() + colDelta;

        // סריקה בכיוון הנתון כל עוד אנחנו בתוך גבולות הלוח
        while (isWithinBoardBounds(row, col)) {
            Disc disc = boardDiscs[row][col];

            if (disc == null) {
                // אם נתקלנו בריבוע ריק, אין אפשרות להפוך בכיוון זה
                flipCounter=0;
                break;
            }

            if (disc.getOwner().equals(lastPlayer)) {
                // אם הדיסק שייך לשחקן היריב
                if (disc.getType().equals("💣")) {
                    validFlips.add(new Position(row, col));
                    countedArr[row][col]=true;
                    // טיפול בפיצוץ
                    //flipCounter+= handleExplosion(new Position(row,col),validFlips);
                    validFlips = handleExplosion(new Position(row,col),validFlips);
                    flipCounter+=validFlips.size();
                    for (int i = 0; i < getBoardSize; i++) {
                        for (int j = 0; j < getBoardSize; j++) {
                            if(boardDiscs[i][j] != null) {
                                boardDiscs[i][j].resetFlags();
                            }
                        }
                    }

                }
                else if (disc.getType().equals("⬤")) {
                    System.out.println(validFlips.size() + "get");

                    // אם מדובר בדיסק רגיל
                    if (!validFlips.contains(new Position(row, col)) && countedArr[row][col]==false) {
                        validFlips.add(new Position(row, col));
                        countedArr[row][col]=true;
                        System.out.println(validFlips.size() + "get1");
                    }
                    flipPositionStack.add(new Position(row,col));
                }
            } else if (disc.getOwner().equals(getCurrentPlayer())) {
                if (disc.getType().equals("💣")) {
                    validFlips = handleExplosion(new Position(row,col),validFlips);
                }
                // אם מצאנו דיסק ששייך לשחקן הנוכחי
                flipCounter = validFlips.size();
                resetCountArr();
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
        resetCountArr();
        return flipCounter;
    }


    public List<Position> handleExplosion(Position a, List<Position> validFlips) {
        int flipCounter = 0;

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
                if(boardDiscs[newRow][newCol].getType().equals("💣")){
                    // Adds the bomb to the list and starting recursion
                    if (!validFlips.contains(new Position(newRow, newCol)) && countedArr[newRow][newCol]==false) {
                        validFlips.add(new Position(newRow, newCol));
                        countedArr[newRow][newCol]=true;
                        boardDiscs[newRow][newCol].setFlagBomb(true);
                        validFlips = handleExplosion(new Position(newRow, newCol), validFlips);

                    }
                }
                // אם הדיסק של היריב, ניתן להפוך אותו
                else if (boardDiscs[newRow][newCol].getOwner().equals(lastPlayer) && boardDiscs[newRow][newCol].getType().equals("⬤")) {
                    //Position flipPosition = new Position(newRow, newCol);
                    if (!validFlips.contains(new Position(newRow, newCol)) && countedArr[newRow][newCol]==false) {
                        validFlips.add(new Position(newRow, newCol));
                        countedArr[newRow][newCol]=true;
                        currentDisc.setFlagBomb(true);
                        //flipCounter++; // הוספת הפתיחה הנכונה
                    }
                }
            }
        }
        //flipCounter+= validFlips.size();
        System.out.println(validFlips.size());

        return validFlips; // מחזיר את מספר ההפיכות מהפיצוץ
    }


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
        // שמירת השחקן הנוכחי באופן זמני
        Player currentPlayerTemp = lastPlayer;

        // בדיקה אם יש מהלכים חוקיים לשחקן הראשון
        lastPlayer = player2; // הפיכת `lastPlayer` כך שיתייחס ל-player1
        boolean player1HasMoves = !ValidMoves().isEmpty();

        // בדיקה אם יש מהלכים חוקיים לשחקן השני
        lastPlayer = player1; // הפיכת `lastPlayer` כך שיתייחס ל-player2
        boolean player2HasMoves = !ValidMoves().isEmpty();

        // שיחזור `lastPlayer` לשחקן האחרון שהיה
        lastPlayer = currentPlayerTemp;

        // אם אין מהלכים חוקיים לשני השחקנים או אם הלוח מלא
        if ((!player1HasMoves && !player2HasMoves) || isBoardFull()) {
            updateWins(); // מחשב את הניקוד ומעדכן את המנצח
            return true;  // המשחק הסתיים
        }

        return false; // המשחק עדיין לא הסתיים
    }
    private boolean isBoardFull() {
        // מעבר על כל התאים בלוח
        for (int row = 0; row < getBoardSize; row++) {
            for (int col = 0; col < getBoardSize; col++) {
                // בדיקה אם התא ריק
                if (boardDiscs[row][col] == null) {
                    return false; // נמצא תא ריק בלוח
                }
            }
        }
        return true; // כל התאים בלוח מלאים
    }


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

    @Override
    public void undoLastMove() {
        if (!moveHistory.isEmpty()) {
            // החזרת המיקום האחרון
            Position lastMove = moveHistory.pop();
            Disc removedDisc = boardDiscs[lastMove.getRow()][lastMove.getCol()];
            boardDiscs[lastMove.getRow()][lastMove.getCol()] = null; // מסיר את הדיסק מהמיקום

            System.out.printf("Undo: removed %s from (%d, %d)\n",
                    removedDisc.getType(),
                    lastMove.getRow(), lastMove.getCol());

            if (!undoCountStack.isEmpty()) {
                int flipsToUndo = undoCountStack.pop();

                for (int i = 0; i < flipsToUndo; i++) {
                    if (!flipHistory.isEmpty()) {
                        Position flippedPos = discsFlipStackerCopy.pop();
                        Disc flippedDisc = boardDiscs[flippedPos.getRow()][flippedPos.getCol()];
                        // משחזר את הבעלות הקודמת
                        //flippedDisc.setOwner(lastPlayer);
                        flippedDisc.setOwner(getCurrentPlayer());

                        System.out.printf("Undo: flipped back %s in (%d, %d)\n",
                                flippedDisc.getType(),
                                flippedPos.getRow(), flippedPos.getCol());
                    }
                }
            }
            lastPlayer = (lastPlayer == player1) ? player2 : player1;
        } else {
            System.out.println("No moves to undo!");
        }
    }

}
