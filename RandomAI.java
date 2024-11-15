import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer{

    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // קבלת רשימת המהלכים החוקיים מהלוח
        List<Position> validMoves = gameStatus.ValidMoves();

        // בדיקה אם יש מהלכים חוקיים זמינים
        if (validMoves.isEmpty()) {
            return null; // אם אין מהלכים חוקיים, החזרה של null
        }

        // יצירת אובייקט Random לבחירת מהלך אקראי
        Random random = new Random();
        Position chosenPosition = validMoves.get(random.nextInt(validMoves.size()));

        // יצירת דיסק פשוט עבור השחקן הנוכחי
        Disc disc = new SimpleDisc(this);

        // החזרת מהלך המכיל את המיקום שנבחר והדיסק החדש
        return new Move(chosenPosition, disc);
    }
}
