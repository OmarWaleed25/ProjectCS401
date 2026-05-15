package view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FreezeNotificationOverlay – shows a prominent banner when a monster's turn
 * is skipped due to the Dynamo's Screech Freeze powerup effect.
 *
 * Displayed as an overlay VBox injected into the root StackPane of the
 * GameView for a few seconds then auto-fades.
 *
 * Usage (from controller):
 *   FreezeNotificationOverlay.show(rootStackPane, "Mike Wazowski");
 *
 * No external CSS used.
 */
public class FreezeNotificationOverlay {

    /**
     * Displays a "TURN SKIPPED – FROZEN!" banner that auto-dismisses.
     *
     * @param root         The root StackPane of the GameView scene
     * @param monsterName  The name of the frozen monster
     */
    public static void show(StackPane root, String monsterName) {
        VBox banner = buildBanner(monsterName);
        StackPane.setAlignment(banner, Pos.TOP_CENTER);
        StackPane.setMargin(banner, new Insets(60, 0, 0, 0));
        banner.setMouseTransparent(true);
        root.getChildren().add(banner);

        // Auto-fade after 2 seconds
        FadeTransition fade = new FadeTransition(Duration.millis(600), banner);
        fade.setDelay(Duration.millis(2000));
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> root.getChildren().remove(banner));
        fade.play();
    }

    private static VBox buildBanner(String monsterName) {
        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(16, 30, 16, 30));
        box.setStyle("-fx-background-color: rgba(0,180,255,0.88); "
                + "-fx-background-radius: 8; -fx-border-color: #ffffff; "
                + "-fx-border-width: 2; -fx-border-radius: 8;");

        Text icon = new Text("❄");
        icon.setFont(Font.font("Arial", 32));
        icon.setFill(Color.WHITE);

        Text title = new Text("TURN SKIPPED – FROZEN!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.WHITE);

        Label detail = new Label(monsterName + " is frozen and loses their turn.");
        detail.setStyle("-fx-text-fill: #e8f8ff; -fx-font-size: 14;");

        box.getChildren().addAll(icon, title, detail);
        return box;
    }
}
