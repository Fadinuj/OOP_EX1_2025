import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private final int getBoardSize=8;
    private Player player1,player2;
    private Player lastPlayer;
    private boolean flag;
    private Disc[][] boardDiscs;
    private Stack<Position> moveHistory;
    private Stack<Position> positions;
    private final Stack<Disc> flipHistory; // Collects data of setOwner.
    private final Stack<Integer> undoCountStack;


    public GameLogic(){
        super();
        boardDiscs=new Disc[getBoardSize][getBoardSize];
        flipHistory=new Stack<>();
        positions = new Stack<>();
        moveHistory = new Stack<>();
        undoCountStack = new Stack<>();
        this.lastPlayer = getSecondPlayer();

    }
    public Player getCurrentPlayer(){
        if (lastPlayer==player1)
            return player2;
        return player1;
    }

    public void initBoard()
    {
        boardDiscs = new Disc[getBoardSize][getBoardSize];
        int middle = getBoardSize / 2;
        boardDiscs[middle][middle] = new SimpleDisc(player1);
        boardDiscs[middle - 1][middle - 1] = new SimpleDisc(player1);
        boardDiscs[middle][middle - 1] = new SimpleDisc(player2);
        boardDiscs[middle - 1][middle] = new SimpleDisc(player2);
        lastPlayer=player2;
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
        int num=0;
        if (player.equals(player1))
        {
            num=1;
        }
        else {
            num=2;
        }

        switch (disc.getType()) {
            case "💣":
                if (player.getNumber_of_bombs() > 0) {
                    player.reduce_bomb();
                    boardDiscs[a.getRow()][a.getCol()] = disc;
                    moveHistory.addLast(new Position(a.getRow(), a.getCol()));
                    flipDiscs(a, disc.getPlayer());
                    //System.out.printf("Player %d placed a %s in (%d,%d)\n No. of Bombs discs left: %d\n", no, disc.getType(), a.row(), a.col(), p1.getNumber_of_bombs());
                    System.out.printf("Player %d placed a %s in (%d,%d)\n", num, disc.getType(), a.getRow(), a.getCol());
                    System.out.println();
                    lastPlayer = player;
                }
                return true;
            case "⭕":
                if (player.getNumber_of_unflippedable() > 0) {
                    player.reduce_unflippedable();
                    boardDiscs[a.getRow()][a.getCol()] = disc;
                    moveHistory.addLast(new Position(a.getRow(), a.getCol()));
                    flipDiscs(a, disc.getPlayer());
                    //System.out.printf("Player %d placed a %s in (%d,%d)" + "\n No. of Unflippable discs left: %d\n", no, disc.getType(), a.row(), a.col(), p1.getNumber_of_unflippedable());
                    System.out.printf("Player %d placed a %s in (%d,%d)" + "\n ", num, disc.getType(), a.getRow(), a.getCol());
                    System.out.println();
                    lastPlayer = player;
                }
                return true;

            case "⬤":
                boardDiscs[a.getRow()][a.getCol()] = disc;
                moveHistory.addLast(new Position(a.getRow(), a.getCol()));
                flipDiscs(a, disc.getPlayer());
                System.out.printf("Player %d placed a %s in (%d,%d)\n", num, disc.getType(), a.getRow(), a.getCol());
                System.out.println();
                lastPlayer = player;
                return true;
            default:
                return false;
        }
    }

    public void flipDiscs(Position a, Player currentPlayer) {
        int undoCount = 0;
        Stack<Position> discsFlipStackerCheck = new Stack<>();
        Stack<Position> discsFlipStackerCopy = new Stack<>();

        // עבור כל כיוון (מעל, מתחת, ימינה, שמאלה, ובאלכסונים)
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
                int num=0;
                if (currentPlayer.equals(player1))
                {
                    num=1;
                }
                else {
                    num=2;
                }


                while (isWithinBoardBounds(row, col)) {
                    Disc disc = boardDiscs[row][col];

                    // אם הדיסק הוא של היריב וניתן להפוך אותו
                    if (disc != null && disc.getPlayer().equals(lastPlayer) && !disc.getType().equals("⭕")) {
                        discsFlipStackerCheck.push(new Position(row, col));
                    }
                    // אם הדיסק שייך לשחקן הנוכחי, כל הדיסקים שהיו במסלול צריכים להתהפך
                    else if (disc != null && disc.getPlayer().equals(currentPlayer)) {
                        while (!discsFlipStackerCheck.isEmpty()) {
                            Position pos = discsFlipStackerCheck.pop();
                            Disc d = boardDiscs[pos.getRow()][pos.getCol()];
                            d.setPlayer(currentPlayer);  // הופך את בעלות הדיסק
                            d.setFliiped(true); // מסמן שהדיסק הפך
                            System.out.printf("Player %s flipped the %s in %s\n", num, d.getType(), pos);
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
//    private List<Position> getValidMoves(Player player) {
//        List<Position> validMoves = new ArrayList<>();
//        for (int i = 0; i < getBoardSize; i++) {
//            for (int j = 0; j < getBoardSize; j++) {
//                if (boardDiscs[i][j] == null) {
//                    if (countFlips(new Position(i, j)) > 0) {
//                        validMoves.add(new Position(i, j));
//                    }
//                }
//            }
//        }
//        return validMoves;
//    }



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
        int flipCounter = 0;
        Stack<Position> tempFlipStack = new Stack<>();
        List<Position> validFlips = new ArrayList<>();
        int row = a.getRow() + rowDelta;
        int col = a.getCol() + colDelta;

        while (isWithinBoardBounds(row, col)) {
            Disc disc = boardDiscs[row][col];

            // אם נתקלנו בדיסק של היריב
            if (disc != null && disc.getPlayer().equals(lastPlayer)) {
                // אם הדיסק הוא פצצה, עלינו לבצע את ההתפוצצות
                if (disc.getType().equals("💣")) {
                    // לחשב את הפיצוץ
                    flipCounter += handleExplosion(a, row, col , validFlips);
                    break; // סיום הסריקה
                }
                // אם הדיסק הוא דיסק שניתן להפוך אותו
                else if (disc.getType().equals("⬤") && !disc.getFlagBomb()) {
                    Position flipPosition = new Position(row, col);
                    if (!validFlips.contains(flipPosition)) {
                        validFlips.add(flipPosition);
                    }
                }
            }
            // אם מצאנו דיסק ששייך לשחקן הנוכחי, סיימנו
            else if (disc != null && disc.getPlayer().equals(getCurrentPlayer())) {
                flipCounter += validFlips.size();
                tempFlipStack.addAll(validFlips);
                break;
            } else {
                break;
            }


        }

        return flipCounter;

    }


    public int handleExplosion(Position a, int row, int col, List<Position> validFlips) {
        int flipCounter = 0;
        // כאן נחשב את ההתפוצצות לפי כל הכיוונים מסביב לפצצה
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // התעלם מהכיוון המרכזי שבו יש את הפצצה
                if (i == 0 && j == 0) continue;
                int newRow = row + i;
                int newCol = col + j;
                if (isWithinBoardBounds(newRow, newCol)) {
                    Disc disc = boardDiscs[newRow][newCol];
                    // אם הדיסק של היריב, ניתן להפוך אותו
                    if (disc != null && disc.getPlayer().equals(lastPlayer) && disc.getType().equals("⬤") && !disc.getFlagBomb()) {
                        Position flipPosition = new Position(newRow, newCol);
                        if (!validFlips.contains(flipPosition)) {
                            validFlips.add(flipPosition);
                            flipCounter++; // הוספת הפתיחה הנכונה
                        }
                    }
                }
            }
        }
        return flipCounter; // מחזיר את מספר ההפיכות מהפיצוץ
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
                if (disc != null && disc.getPlayer() == player) {
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
        if (ValidMoves().isEmpty())
        {
            System.out.println("debug");
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


    }
}
