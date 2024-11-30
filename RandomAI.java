import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {
    private Player randAI;
    private Disc chosenDisc;

    /**
     * בונה אובייקט RandomAI, המייצג שחקן מחשב שמשחק בצורה רנדומלית.
     *
     * @param isPlayerOne true אם ה-AI הוא שחקן 1, אחרת false.
     */
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * מבצע מהלך עבור ה-AI.
     * המהלך נבחר בצורה רנדומלית מתוך המהלכים החוקיים האפשריים.
     *
     * @param gameStatus מצב המשחק הנוכחי, המיוצג על ידי {@link PlayableLogic}.
     * @return אובייקט {@link Move} שמייצג את המהלך שנבחר, או null אם אין מהלכים זמינים.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // קביעת השחקן הנוכחי (Player 1 או Player 2)
        randAI = isPlayerOne ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();

        // קבלת רשימת המהלכים החוקיים
        List<Position> availableMoves = gameStatus.ValidMoves();

        // בדיקה אם אין מהלכים חוקיים
        if (availableMoves.isEmpty()) {
            // בדיקה אם המשחק הסתיים
            if (gameStatus.isGameFinished()) {
                System.out.println("Game over. No valid moves available.");
            }
            return null; // מחזיר null אם אין מהלכים חוקיים
        }

        // בחירת מהלך רנדומלי מתוך הרשימה
        Random random = new Random();
        Position nextMove = availableMoves.get(random.nextInt(availableMoves.size()));

        // בחירת סוג דיסק רנדומלי
        int discType = random.nextInt(3); // ערך בין 0 ל-2
        switch (discType) {
            case 0 -> chosenDisc = new SimpleDisc(randAI);
            case 1 -> {
                if (randAI.getNumber_of_bombs() > 0) {
                    chosenDisc = new BombDisc(randAI);
                } else {
                    chosenDisc = new SimpleDisc(randAI);
                }
            }
            case 2 -> {
                if (randAI.getNumber_of_unflippedable() > 0) {
                    chosenDisc = new UnflippableDisc(randAI);
                } else {
                    chosenDisc = new SimpleDisc(randAI);
                }
            }
            default -> chosenDisc = new SimpleDisc(randAI); // ברירת מחדל
        }

        // החזרת המהלך שנבחר
        return new Move(nextMove, chosenDisc);
    }
}