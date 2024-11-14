public class Move {
    private final Position position;  // מיקום המהלך
    private final Disc disc;          // הדיסק שיונח במיקום זה
    private final Disc previousDisc;  // הדיסק שהיה שם קודם, למקרה של undo

    // בנאי שמקבל מיקום, דיסק חדש, ודיסק קודם
    public Move(Position position, Disc disc, Disc previousDisc) {
        this.position = position;
        this.disc = disc;
        this.previousDisc = previousDisc;
    }

    // גטרים
    public Position getPosition() {
        return position;
    }

    public Disc getDisc() {
        return disc;
    }

    public Disc getPreviousDisc() {
        return previousDisc;
    }
}
