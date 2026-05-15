package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * WinScreen – displayed when a monster reaches cell 99 with ≥ 1000 energy.
 *
 * Shows:
 *  - "GAME WON" header
 *  - Winning monster name and role
 *  - Final energy of both monsters
 *  - Button to return to start window
 *
 * No external CSS used.
 */
public class WinScreen {

    private Label lblWinnerName;
    private Label lblWinnerRole;
    private Label lblWinnerEnergy;
    private Label lblLoserName;
    private Label lblLoserEnergy;

    private Button btnReturnToStart;

    private Scene scene;
    private Stage stage;

    public WinScreen(Stage stage) {
        this.stage = stage;
        buildUI();
    }

    private void buildUI() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #0a0a1a;");

        // Dark overlay card
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(50, 70, 50, 70));
        card.setMaxWidth(520);
        card.setStyle("-fx-background-color: #16213e; -fx-border-color: #ffd700; "
                + "-fx-border-width: 3; -fx-background-radius: 12; -fx-border-radius: 12;");

        // Trophy / header
        Text trophy = new Text("🏆");
        trophy.setFont(Font.font("Arial", 64));

        Text gameWon = new Text("GAME WON!");
        gameWon.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        gameWon.setFill(Color.web("#ffd700"));

        Separator sep1 = new Separator();

        // Winner block
        Text winnerHeader = new Text("WINNER");
        winnerHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        winnerHeader.setFill(Color.web("#aaaaaa"));

        lblWinnerName = new Label("Winner: -");
        lblWinnerName.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        lblWinnerName.setStyle("-fx-text-fill: #00ff88;");

        lblWinnerRole = new Label("Role: -");
        lblWinnerRole.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblWinnerRole.setStyle("-fx-text-fill: #4ecdc4;");

        lblWinnerEnergy = new Label("Final Energy: -");
        lblWinnerEnergy.setFont(Font.font("Arial", 14));
        lblWinnerEnergy.setStyle("-fx-text-fill: #00ff88;");

        Separator sep2 = new Separator();

        // Loser block
        Text loserHeader = new Text("OPPONENT");
        loserHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        loserHeader.setFill(Color.web("#aaaaaa"));

        lblLoserName = new Label("Opponent: -");
        lblLoserName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblLoserName.setStyle("-fx-text-fill: #ff6b6b;");

        lblLoserEnergy = new Label("Final Energy: -");
        lblLoserEnergy.setFont(Font.font("Arial", 14));
        lblLoserEnergy.setStyle("-fx-text-fill: #ff6b6b;");

        Separator sep3 = new Separator();

        btnReturnToStart = new Button("↩  Return to Main Menu");
        btnReturnToStart.setMaxWidth(Double.MAX_VALUE);
        btnReturnToStart.setStyle(
                "-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 15; "
                + "-fx-font-weight: bold; -fx-padding: 12 20; -fx-cursor: hand; "
                + "-fx-background-radius: 8;");

        card.getChildren().addAll(
                trophy, gameWon, sep1,
                winnerHeader, lblWinnerName, lblWinnerRole, lblWinnerEnergy,
                sep2,
                loserHeader, lblLoserName, lblLoserEnergy,
                sep3,
                btnReturnToStart
        );

        root.getChildren().add(card);
        scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Game Over – DooR DasH");
    }

    // ── PUBLIC API ─────────────────────────────────────────────────────────

    public void show() { stage.show(); }

    /** Populate the win screen before showing it. */
    public void setWinnerInfo(String name, String role, int energy) {
        lblWinnerName.setText(name);
        lblWinnerRole.setText("Role: " + role);
        lblWinnerEnergy.setText("Final Energy: " + energy);
    }

    public void setLoserInfo(String name, int energy) {
        lblLoserName.setText(name);
        lblLoserEnergy.setText("Final Energy: " + energy);
    }

    public Button getBtnReturnToStart() { return btnReturnToStart; }

    public Scene getScene()  { return scene; }
    public Stage  getStage() { return stage; }
}
