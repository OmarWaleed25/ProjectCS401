package view;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * EnergyChangeOverlay – renders a floating "+200" / "-100" / "SHIELD!" label
 * on top of a board cell whenever energy changes occur.
 *
 * Usage (from controller):
 *   EnergyChangeOverlay.flash(boardView.getCellPane(index), -100, false);
 *   EnergyChangeOverlay.flashShield(boardView.getCellPane(index));
 *
 * No external CSS used.
 */
public class EnergyChangeOverlay {

    /**
     * Shows a floating energy change indicator on the given cell pane.
     *
     * @param cellPane   The StackPane representing the board cell
     * @param delta      The energy change amount (positive = gain, negative = loss)
     * @param shielded   If true, the change was blocked by a shield
     */
    public static void flash(StackPane cellPane, int delta, boolean shielded) {
        if (shielded) {
            flashShield(cellPane);
            return;
        }

        String text  = (delta >= 0 ? "+" : "") + delta;
        String color = delta >= 0 ? "#00ff88" : "#ff4444";

        Label lbl = buildLabel(text, color, 13);
        showFloating(cellPane, lbl);
    }

    /**
     * Shows a "🛡 BLOCKED!" indicator when a shield absorbs an energy hit.
     */
    public static void flashShield(StackPane cellPane) {
        Label lbl = buildLabel("🛡 BLOCKED!", "#4ecdc4", 11);
        showFloating(cellPane, lbl);
    }

    /**
     * Shows a card effect label (e.g. "SWAP!", "START OVER").
     */
    public static void flashCardEffect(StackPane cellPane, String effectText) {
        Label lbl = buildLabel(effectText, "#ffd700", 11);
        showFloating(cellPane, lbl);
    }

    // ── Internals ──────────────────────────────────────────────────────────

    private static Label buildLabel(String text, String hexColor, int fontSize) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        lbl.setStyle(String.format(
                "-fx-text-fill: %s; -fx-background-color: rgba(0,0,0,0.65); "
                + "-fx-padding: 2 5; -fx-background-radius: 4;", hexColor));
        lbl.setMouseTransparent(true);
        return lbl;
    }

    private static void showFloating(StackPane cellPane, Label lbl) {
        StackPane.setAlignment(lbl, Pos.CENTER);
        cellPane.getChildren().add(lbl);

        // Float upward and fade out
        TranslateTransition move = new TranslateTransition(Duration.millis(800), lbl);
        move.setByY(-30);

        FadeTransition fade = new FadeTransition(Duration.millis(800), lbl);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        ParallelTransition anim = new ParallelTransition(move, fade);
        anim.setOnFinished(e -> cellPane.getChildren().remove(lbl));
        anim.play();
    }
}
