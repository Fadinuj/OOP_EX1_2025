import java.util.List;

public class GreedyAI extends AIPlayer{

    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validMoves = gameStatus.ValidMoves(); // קבלת רשימת המהלכים החוקיים

        if (validMoves.isEmpty()) {
            return null; // אם אין מהלכים חוקיים, נחזיר null
        }

        Position bestPosition = null;
        int maxFlips = -1;

        // לולאה שבודקת כל מהלך חוקי כדי למצוא את המהלך שמניב את מקסימום ההפיכות
        for (Position position : validMoves) {
            int flips = gameStatus.countFlips(position);

            if (flips > maxFlips) {
                maxFlips = flips;
                bestPosition = position;
            }
        }

        // אם נמצא מהלך מיטבי, ניצור את הדיסק המתאים ונחזיר אובייקט Move
        if (bestPosition != null) {
            Disc disc = new SimpleDisc(this);
            return new Move(bestPosition, disc);
        }

        return null; // במקרה שלא נמצא מהלך כלשהו, נחזיר null
    }
}
