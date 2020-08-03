package Controller;

/**
 * Enthält mögliche Aktionen die mit der Tastatur ausgelöst werden.
 * Ein Input muss in der GameView auf eine Taste gemapped werden.
 */
public enum Input {
    /**
     * Links.
     */
    LEFT,
    /**
     * Rechts.
     */
    RIGHT,
    /**
     * Springen.
     */
    JUMP,
    /**
     * Menü.
     */
    MENU,
    /**
     * Debugging-feature zum Zurücksetzen des Charakters.
     */
    RESET,
    /**
     * Runter.
     */
    DOWN,
    /**
     * Hoch.
     */
    UP,
    /**
     * Bestätigen.
     */
    ENTER
}
