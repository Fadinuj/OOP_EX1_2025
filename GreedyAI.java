import java.util.List;

public class GreedyAI extends AIPlayer{

    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validMoves = gameStatus.ValidMoves();

        if (validMoves.isEmpty()) {
            return null;
        }

        Position bestPosition = null;
        int maxFlips = -1;


        for (Position position : validMoves) {
            int flips = gameStatus.countFlips(position);

            if (flips > maxFlips) {
                maxFlips = flips;
                bestPosition = position;
            }
        }


        if (bestPosition != null) {
            Disc disc = new SimpleDisc(this);
            return new Move(bestPosition, disc);
        }

        return null;
    }
}
