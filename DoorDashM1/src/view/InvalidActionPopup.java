package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * InvalidActionPopup – shown whenever a player attempts an invalid action.
 *
 * Requirements satisfied:
 *  - Shows the reason the action could not be performed.
 *  - Game is NOT stopped; closing this popup does NOT terminate the game.
 *  - Displayed using a separate non-blocking (modal) window.
 *
 * Usage (from a controller):
 *   InvalidActionPopup popup = new InvalidActionPopup(primaryStage);
 *   popup.show("Out of Energy", "You need 500 energy to activate your powerup.");
 *
 * No external CSS used.
 */
public class InvalidActionPopup {

    private final Stage owner;

    public InvalidActionPopup(Stage owner) {
        this.owner = owner;
    }

    /**
     * Shows a modal popup with the given title and reason.
     * The popup blocks input to the owner window but does not terminate the game on close.
     *
     * @param title  Short error title (e.g. "Invalid Move")
     * @param reason Detailed explanation shown to the player
     */
    public void show(String title, String reason) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.initOwner(owner);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.setTitle("Invalid Action");
        popupStage.setResizable(false);

        VBox root = new VBox(16);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30, 40, 30, 40));
        root.setStyle("-fx-background-color: #1e0000; -fx-border-color: #e94560; "
                + "-fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");

        // Warning icon + title
        Text icon = new Text("⚠");
        icon.setFont(Font.font("Arial", 40));
        icon.setFill(Color.web("#e94560"));

        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleText.setFill(Color.web("#ff6b6b"));

        // Reason label
        Label reasonLabel = new Label(reason);
        reasonLabel.setWrapText(true);
        reasonLabel.setMaxWidth(320);
        reasonLabel.setStyle("-fx-text-fill: #dddddd; -fx-font-size: 13; -fx-text-alignment: center;");
        reasonLabel.setAlignment(Pos.CENTER);

        // Dismiss button – closing NEVER terminates the game
        Button btnOk = new Button("OK  –  Try Another Action");
        btnOk.setStyle(
                "-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 13; "
                + "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand; "
                + "-fx-background-radius: 6;");
        btnOk.setOnAction(e -> popupStage.close());

        // X button override: close popup only, do NOT exit application
        popupStage.setOnCloseRequest(e -> {
            e.consume();   // prevent default close behaviour
            popupStage.close();
        });

        root.getChildren().addAll(icon, titleText, reasonLabel, btnOk);

        Scene scene = new Scene(root, 420, 230, Color.TRANSPARENT);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    // ── Convenience factory methods for common error types ─────────────────

    public void showOutOfEnergy(int required, int current) {
        show("Out of Energy",
                String.format("You need %d energy to activate your powerup, but you only have %d.",
                        required, current));
    }

    public void showInvalidMove(String reason) {
        show("Invalid Move", reason);
    }

    public void showCellOccupied() {
        show("Cell Occupied",
                "Your destination is occupied by your opponent! Roll the dice again.");
    }

    public void showFrozen(String monsterName) {
        show("Turn Skipped – Frozen!",
                monsterName + " is frozen and must skip this turn. Control passes to your opponent.");
    }
}
