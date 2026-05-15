package view;

/**
 * GameViewConstants – centralised style/color constants used across all view classes.
 *
 * Controllers and views reference these instead of hardcoding strings,
 * making theme changes trivial.
 *
 * No external CSS used – all styles are inline JavaFX -fx- properties.
 */
public final class GameViewConstants {

    private GameViewConstants() {}

    // ── Background colors ──────────────────────────────────────────────────
    public static final String BG_DARK         = "#1a1a2e";
    public static final String BG_PANEL        = "#16213e";
    public static final String BG_DEEP         = "#0f1923";
    public static final String BORDER_PANEL    = "#0f3460";

    // ── Cell background colors ─────────────────────────────────────────────
    public static final String CELL_NORMAL     = "#3d3500";   // Normal – yellow-dark
    public static final String CELL_SCARER     = "#3d0060";   // SCARER door – purple
    public static final String CELL_LAUGHER    = "#003d28";   // LAUGHER door – green
    public static final String CELL_MONSTER    = "#002060";   // Monster cell – blue
    public static final String CELL_CARD       = "#400000";   // Card cell – red
    public static final String CELL_CONVEYOR   = "#003300";   // Conveyor belt – bright green
    public static final String CELL_CONTAM     = "#402000";   // Contamination sock – orange-dark

    // ── Activated / exhausted door overlay ───────────────────────────────
    /** Opacity for an activated (exhausted) door cell. */
    public static final double DOOR_ACTIVATED_OPACITY = 0.45;

    // ── Monster token colors ───────────────────────────────────────────────
    public static final String TOKEN_PLAYER   = "#00d4ff";   // Cyan – human player
    public static final String TOKEN_OPPONENT = "#e94560";   // Red  – opponent

    // ── Text colors ────────────────────────────────────────────────────────
    public static final String TEXT_WHITE      = "#ffffff";
    public static final String TEXT_MUTED      = "#aaaaaa";
    public static final String TEXT_GOLD       = "#ffd700";
    public static final String TEXT_CYAN       = "#00d4ff";
    public static final String TEXT_GREEN      = "#00ff88";
    public static final String TEXT_RED        = "#ff6b6b";
    public static final String TEXT_ACCENT     = "#e94560";
    public static final String TEXT_TEAL       = "#4ecdc4";
    public static final String TEXT_CONFUSED   = "#ff4444";  // Role label when confused

    // ── Button colors ──────────────────────────────────────────────────────
    public static final String BTN_POWERUP     = "#e94560";
    public static final String BTN_ROLL        = "#4ecdc4";
    public static final String BTN_MENU        = "#e94560";

    // ── Shared inline style fragments ─────────────────────────────────────
    public static final String CELL_BORDER     = "-fx-border-color: #0f3460; -fx-border-width: 1;";
    public static final String CELL_HIGHLIGHT  = "-fx-border-color: #ffd700; -fx-border-width: 2;";

    // ── Monster abbreviations for board tokens ─────────────────────────────
    /** Returns a short 2–3 char abbreviation for a monster name. */
    public static String abbrev(String monsterName) {
        if (monsterName == null || monsterName.trim().isEmpty()) return "?";
        String[] parts = monsterName.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, Math.min(3, parts[0].length())).toUpperCase();
        // First letter of first two words
        return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
    }

    // ── Role display helpers ───────────────────────────────────────────────
    public static String roleColor(String role, boolean confused) {
        if (confused) return TEXT_CONFUSED;
        return "SCARER".equalsIgnoreCase(role) ? "#b06eff" : "#00e676";
    }

    // ── Energy color (green when high, red when low) ───────────────────────
    public static String energyColor(int energy) {
        if (energy >= 700) return TEXT_GREEN;
        if (energy >= 300) return TEXT_GOLD;
        return TEXT_RED;
    }
}
