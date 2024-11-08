import java.util.List;

public class GameLogic implements PlayableLogic {
    public Player player1;
    public Player player2;
    @Override
    public boolean locate_disc(Position a, Disc disc) {
        return false;
    }

    @Override
    public Disc getDiscAtPosition(Position position) {
        return null;
    }

    @Override
    public int getBoardSize() {
        return 0;
    }

    @Override
    public List<Position> ValidMoves() {
        return List.of();
    }

    @Override
    public int countFlips(Position a) {
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
        return player1.isPlayerOne;
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    @Override
    public void reset() {


    }

    @Override
    public void undoLastMove() {

    }
}
