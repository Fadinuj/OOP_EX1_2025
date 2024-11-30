public class Move {
    private final Position position;  // המיקום על הלוח שבו המהלך מתבצע
    private final Disc disc;          // הדיסק שיונח במיקום זה
    private final Disc previousDisc;  // הדיסק שהיה קודם במקום זה (לשימוש בפעולות undo)

    /**
     * בנאי שמקבל מיקום, דיסק חדש, ודיסק קודם (לשימוש במנגנון undo).
     *
     * @param position     המיקום שבו המהלך יתבצע.
     * @param disc         הדיסק שיונח במיקום.
     * @param previousDisc הדיסק שהיה במקום זה לפני המהלך (יכול להיות null).
     */
    public Move(Position position, Disc disc, Disc previousDisc) {
        this.position = position;
        this.disc = disc;
        this.previousDisc = previousDisc;
    }

    /**
     * בנאי שמקבל מיקום ודיסק, ללא התייחסות לדיסק הקודם.
     *
     * @param position המיקום שבו המהלך יתבצע.
     * @param disc     הדיסק שיונח במיקום.
     */
    public Move(Position position, Disc disc) {
        this(position, disc, null); // קריאה לבנאי הראשי עם null לדיסק הקודם
    }

    // גטרים

    /**
     * מחזיר את המיקום של המהלך.
     *
     * @return אובייקט {@link Position} המייצג את מיקום המהלך.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * מחזיר את הדיסק ששויך למהלך.
     *
     * @return אובייקט {@link Disc} המייצג את הדיסק.
     */
    public Disc getDisc() {
        return disc;
    }

    /**
     * מחזיר את הדיסק שהיה קודם במקום המהלך (לשימוש במנגנון undo).
     *
     * @return אובייקט {@link Disc} המייצג את הדיסק הקודם, או null אם לא היה.
     */
    public Disc getPreviousDisc() {
        return previousDisc;
    }
}
