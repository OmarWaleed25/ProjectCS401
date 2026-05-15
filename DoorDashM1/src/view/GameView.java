package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import game.engine.Game;
import game.engine.Role;
import game.engine.Constants;
import game.engine.cells.*;
import game.engine.cells.Cell;
import game.engine.monsters.*;

/**
 * GameView – the main game screen.
 *
 * Layout (left-to-right):
 *   [Left Panel: Player Info]  [Center: Board]  [Right Panel: Opponent Info + Log]
 *
 * All styling is done inline (no external CSS).
 * Pass a fully constructed Game to the constructor – the board and monster
 * panels are populated automatically.
 */
public class GameView {

    // ── Board ──────────────────────────────────────────────────────────────
    public static final int BOARD_SIZE   = 10;
    public static final int CELL_SIZE    = 60;

    private final StackPane[][] cellPanes = new StackPane[BOARD_SIZE][BOARD_SIZE];

    // ── Top status bar ─────────────────────────────────────────────────────
    private Label lblCurrentTurn;
    private Label lblCurrentPlayer;
    private Label lblDiceResult;

    // ── Player (left) panel ────────────────────────────────────────────────
    private Label lblPlayerName;
    private Label lblPlayerOriginalRole;
    private Label lblPlayerCurrentRole;
    private Label lblPlayerType;
    private Label lblPlayerEnergy;
    private Label lblPlayerPosition;
    private Label lblPlayerStatusEffects;

    // ── Opponent (right) panel ─────────────────────────────────────────────
    private Label lblOpponentName;
    private Label lblOpponentOriginalRole;
    private Label lblOpponentCurrentRole;
    private Label lblOpponentType;
    private Label lblOpponentEnergy;
    private Label lblOpponentPosition;
    private Label lblOpponentStatusEffects;

    // ── Action controls ────────────────────────────────────────────────────
    private Button btnUsePowerup;
    private Button btnRollDice;
    private Label  lblActionFeedback;

    // ── Card drawn panel ───────────────────────────────────────────────────
    private Label lblCardName;
    private Label lblCardEffect;
    private VBox  cardPanel;

    // ── Game log ───────────────────────────────────────────────────────────
    private TextArea gameLog;

    // ── Scene / Stage ──────────────────────────────────────────────────────
    private Scene scene;
    private Stage stage;

    // ── Game reference ─────────────────────────────────────────────────────
    private Game game;

    /**
     * Creates and fully initialises the GameView from the given Game instance.
     * The board cells are coloured by type, door energies shown, stationed
     * monster names shown on their cells, and both player/opponent panels
     * populated with their starting data.
     *
     * @param stage the primary JavaFX Stage
     * @param game  a fully constructed Game (board already initialised)
     */
    public GameView(Stage stage, Game game) {
        this.stage = stage;
        this.game  = game;
        buildUI();
        initializeFromGame();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  BUILD
    // ═══════════════════════════════════════════════════════════════════════
    private void buildUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        root.setTop(buildTopBar());
        root.setLeft(buildPlayerPanel());
        root.setCenter(buildBoardArea());
        root.setRight(buildOpponentPanel());
        root.setBottom(buildBottomBar());

        scene = new Scene(root, 1280, 820);
        stage.setTitle("DooR DasH: Scare vs Laugh Touchdown");
        stage.setScene(scene);
        stage.setResizable(false);
    }

    // ── TOP BAR ────────────────────────────────────────────────────────────
    private HBox buildTopBar() {
        HBox bar = new HBox(30);
        bar.setPadding(new Insets(10, 20, 10, 20));
        bar.setAlignment(Pos.CENTER);
        bar.setStyle("-fx-background-color: #16213e; -fx-border-color: #0f3460; -fx-border-width: 0 0 2 0;");

        Text title = new Text("DooR DasH: Scare vs Laugh Touchdown");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.web("#e94560"));

        lblCurrentTurn = styledLabel("Turn: 1", "#f5f5f5", 14, true);
        lblCurrentPlayer = styledLabel("Current: -", "#ffd700", 14, true);
        lblDiceResult = styledLabel("Dice: -", "#00d4ff", 14, true);

        Region spacer1 = new Region(); HBox.setHgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region(); HBox.setHgrow(spacer2, Priority.ALWAYS);

        bar.getChildren().addAll(title, spacer1, lblCurrentTurn, lblCurrentPlayer, lblDiceResult, spacer2);
        return bar;
    }

    // ── PLAYER PANEL (LEFT) ────────────────────────────────────────────────
    private VBox buildPlayerPanel() {
        VBox panel = new VBox(8);
        panel.setPadding(new Insets(14, 12, 14, 12));
        panel.setPrefWidth(190);
        panel.setStyle("-fx-background-color: #16213e; -fx-border-color: #0f3460; -fx-border-width: 0 2 0 0;");

        Text header = new Text("⚔  PLAYER");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        header.setFill(Color.web("#4ecdc4"));

        lblPlayerName         = styledLabel("Name: -",          "#ffffff", 13, true);
        lblPlayerOriginalRole = styledLabel("Origin: -",        "#aaaaaa", 12, false);
        lblPlayerCurrentRole  = styledLabel("Role: -",          "#ffd700", 13, true);
        lblPlayerType         = styledLabel("Type: -",          "#aaaaaa", 12, false);
        lblPlayerEnergy       = styledLabel("Energy: 0",        "#00ff88", 14, true);
        lblPlayerPosition     = styledLabel("Position: 0",      "#00d4ff", 13, true);
        lblPlayerStatusEffects= styledLabel("Effects: none",    "#ff6b6b", 12, false);
        lblPlayerStatusEffects.setWrapText(true);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #0f3460;");

        // Action buttons
        btnUsePowerup = actionButton("⚡ Use Powerup", "#e94560");
        btnRollDice   = actionButton("🎲 Roll Dice",   "#4ecdc4");

        lblActionFeedback = styledLabel("", "#ff6b6b", 11, false);
        lblActionFeedback.setWrapText(true);
        lblActionFeedback.setPrefHeight(40);

        // Card drawn section
        cardPanel = new VBox(4);
        cardPanel.setStyle("-fx-background-color: #0f3460; -fx-border-color: #e94560; "
                + "-fx-border-width: 1; -fx-padding: 6;");
        cardPanel.setVisible(false);

        Text cardHeader = new Text("🃏 Card Drawn");
        cardHeader.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        cardHeader.setFill(Color.web("#ffd700"));

        lblCardName   = styledLabel("Name: -",   "#ffffff", 12, true);
        lblCardEffect = styledLabel("Effect: -", "#aaaaaa", 11, false);
        lblCardEffect.setWrapText(true);
        cardPanel.getChildren().addAll(cardHeader, lblCardName, lblCardEffect);

        panel.getChildren().addAll(
                header, new Separator(),
                lblPlayerName, lblPlayerOriginalRole, lblPlayerCurrentRole,
                lblPlayerType, lblPlayerEnergy, lblPlayerPosition, lblPlayerStatusEffects,
                sep,
                btnUsePowerup, btnRollDice,
                lblActionFeedback,
                new Separator(),
                cardPanel
        );
        return panel;
    }

    // ── BOARD AREA (CENTER) ────────────────────────────────────────────────
    private VBox buildBoardArea() {
        VBox wrapper = new VBox(6);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        // Board rows go bottom→top visually; row 9 = indices 0-9 (bottom), row 0 = indices 90-99 (top)
        for (int visualRow = 0; visualRow < BOARD_SIZE; visualRow++) {
            int boardRow = BOARD_SIZE - 1 - visualRow; // board row 9 at visual row 0 bottom
            for (int col = 0; col < BOARD_SIZE; col++) {
                int index = boardIndexFromRowCol(boardRow, col);
                StackPane cell = buildCell(index);
                cellPanes[boardRow][col] = cell;
                grid.add(cell, col, visualRow);
            }
        }

        // Column labels 0-9
        HBox colLabels = new HBox(2);
        colLabels.setAlignment(Pos.CENTER);
        for (int c = 0; c < BOARD_SIZE; c++) {
            Label lbl = new Label(String.valueOf(c));
            lbl.setPrefWidth(CELL_SIZE + 2);
            lbl.setAlignment(Pos.CENTER);
            lbl.setStyle("-fx-text-fill: #888888; -fx-font-size: 10;");
            colLabels.getChildren().add(lbl);
        }

        wrapper.getChildren().addAll(colLabels, grid);
        return wrapper;
    }

    /** Converts board row/col → linear index (zigzag). Even rows L→R, odd rows R→L. */
    private int boardIndexFromRowCol(int row, int col) {
        if (row % 2 == 0) {
            return row * BOARD_SIZE + col;
        } else {
            return row * BOARD_SIZE + (BOARD_SIZE - 1 - col);
        }
    }

    private StackPane buildCell(int index) {
        StackPane cell = new StackPane();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setMinSize(CELL_SIZE, CELL_SIZE);
        cell.setMaxSize(CELL_SIZE, CELL_SIZE);
        cell.setStyle("-fx-border-color: #888888; -fx-border-width: 1;");
        cell.setId("cell_" + index);

        // Layer 1: background color rectangle (always present, image sits on top)
        Rectangle bg = new Rectangle(CELL_SIZE, CELL_SIZE);
        bg.setId("cell_bg_" + index);
        bg.setFill(Color.web("#e8dfc0")); // cream – matches the board image
        cell.getChildren().add(bg);

        // Layer 2: ImageView – filled by setCellImage() after board init
        ImageView imageView = new ImageView();
        imageView.setId("cell_img_" + index);
        imageView.setFitWidth(CELL_SIZE);
        imageView.setFitHeight(CELL_SIZE);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        cell.getChildren().add(imageView);

        // Layer 3: index label (top-left, semi-transparent dark bg for readability)
        Label idxLabel = new Label(String.valueOf(index));
        idxLabel.setFont(Font.font("Arial", FontWeight.BOLD, 8));
        idxLabel.setStyle("-fx-text-fill: white; "
                + "-fx-background-color: rgba(0,0,0,0.55); "
                + "-fx-padding: 0 2 0 2; -fx-background-radius: 2;");
        StackPane.setAlignment(idxLabel, Pos.TOP_LEFT);
        StackPane.setMargin(idxLabel, new Insets(2, 0, 0, 2));
        cell.getChildren().add(idxLabel);

        // Layer 4: content label (door energy / conveyor value / "?" etc.)
        Label contentLabel = new Label("");
        contentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        contentLabel.setWrapText(true);
        contentLabel.setTextAlignment(TextAlignment.CENTER);
        contentLabel.setStyle("-fx-text-fill: white; "
                + "-fx-background-color: rgba(0,0,0,0.50); "
                + "-fx-padding: 1 3; -fx-background-radius: 3;");
        contentLabel.setId("cell_content_" + index);
        StackPane.setAlignment(contentLabel, Pos.BOTTOM_CENTER);
        StackPane.setMargin(contentLabel, new Insets(0, 0, 2, 0));
        cell.getChildren().add(contentLabel);

        return cell;
    }

    /**
     * Sets the image displayed inside a cell.
     *
     * The image path should be a classpath resource, e.g.:
     *   "/resources/images/scarer_door.png"
     *   "/resources/images/laugher_door.png"
     *   "/resources/images/conveyor.png"
     *   "/resources/images/sock.png"
     *   "/resources/images/card_cell.png"
     *   "/resources/images/normal.png"
     *   "/resources/images/monsters/sullivan.png"   (stationed monster)
     *
     * Place all images under src/resources/images/ so they are on the
     * classpath at runtime.  If the resource is not found the cell simply
     * keeps its plain background colour – no crash.
     *
     * @param index      linear board index (0-99)
     * @param imagePath  classpath path starting with "/"
     */
    public void setCellImage(int index, String imagePath) {
        StackPane pane = getCellPane(index);
        pane.getChildren().stream()
                .filter(n -> ("cell_img_" + index).equals(n.getId()))
                .findFirst()
                .ifPresent(n -> {
                    try {
                        Image img = new Image(
                                getClass().getResourceAsStream(imagePath),
                                CELL_SIZE, CELL_SIZE, false, true);
                        ((ImageView) n).setImage(img);
                    } catch (Exception e) {
                        // Image not found – leave cell with background colour only
                        System.err.println("GameView: image not found: " + imagePath);
                    }
                });
    }

    /**
     * Convenience – sets both the CellType colour tint AND the image in one call.
     * Call this from initializeBoardCells() instead of setCellType() alone.
     */
    public void setCellTypeAndImage(int index, CellType type, String imagePath) {
        setCellType(index, type);
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            setCellImage(index, imagePath);
        }
    }

    /** Returns background color based on cell type heuristics (doors alternate, etc.).
     *  The controller can override individual cell styles at runtime. */
    private String defaultCellStyle(int index) {
        String base = "-fx-border-color: #888888; -fx-border-width: 1;";
        return base + "-fx-background-color: #e8dfc0;";
    }

    // ── OPPONENT PANEL (RIGHT) ─────────────────────────────────────────────
    private VBox buildOpponentPanel() {
        VBox panel = new VBox(8);
        panel.setPadding(new Insets(14, 12, 14, 12));
        panel.setPrefWidth(200);
        panel.setStyle("-fx-background-color: #16213e; -fx-border-color: #0f3460; -fx-border-width: 0 0 0 2;");

        Text header = new Text("💀  OPPONENT");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        header.setFill(Color.web("#e94560"));

        lblOpponentName         = styledLabel("Name: -",       "#ffffff", 13, true);
        lblOpponentOriginalRole = styledLabel("Origin: -",     "#aaaaaa", 12, false);
        lblOpponentCurrentRole  = styledLabel("Role: -",       "#ffd700", 13, true);
        lblOpponentType         = styledLabel("Type: -",       "#aaaaaa", 12, false);
        lblOpponentEnergy       = styledLabel("Energy: 0",     "#ff6b6b", 14, true);
        lblOpponentPosition     = styledLabel("Position: 0",   "#00d4ff", 13, true);
        lblOpponentStatusEffects= styledLabel("Effects: none", "#ff6b6b", 12, false);
        lblOpponentStatusEffects.setWrapText(true);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #0f3460;");

        // Game log
        Text logHeader = new Text("📋 Game Log");
        logHeader.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        logHeader.setFill(Color.web("#ffffff"));

        gameLog = new TextArea();
        gameLog.setEditable(false);
        gameLog.setWrapText(true);
        gameLog.setPrefHeight(340);
        gameLog.setStyle("-fx-control-inner-background: #0f1923; -fx-text-fill: #c8c8c8; "
                + "-fx-font-size: 11; -fx-font-family: 'Courier New';");
        VBox.setVgrow(gameLog, Priority.ALWAYS);

        panel.getChildren().addAll(
                header, new Separator(),
                lblOpponentName, lblOpponentOriginalRole, lblOpponentCurrentRole,
                lblOpponentType, lblOpponentEnergy, lblOpponentPosition, lblOpponentStatusEffects,
                sep,
                logHeader, gameLog
        );
        return panel;
    }

    // ── BOTTOM BAR ─────────────────────────────────────────────────────────
    private HBox buildBottomBar() {
        HBox bar = new HBox();
        bar.setPadding(new Insets(6, 20, 6, 20));
        bar.setAlignment(Pos.CENTER);
        bar.setStyle("-fx-background-color: #0f3460;");

        Label hint = new Label("First use Powerup (optional), then Roll Dice to take your turn.");
        hint.setStyle("-fx-text-fill: #888888; -fx-font-size: 11;");
        bar.getChildren().add(hint);
        return bar;
    }

    // ── HELPERS ────────────────────────────────────────────────────────────
    private Label styledLabel(String text, String color, int size, boolean bold) {
        Label lbl = new Label(text);
        String weight = bold ? "bold" : "normal";
        lbl.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: %d; -fx-font-weight: %s;",
                color, size, weight));
        return lbl;
    }

    private Button actionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 13; "
                + "-fx-font-weight: bold; -fx-padding: 8 14; -fx-cursor: hand; "
                + "-fx-background-radius: 6;", color));
        return btn;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  PUBLIC API – used by controllers
    // ═══════════════════════════════════════════════════════════════════════

    public void show() {
        stage.show();
    }

    // Turn / dice
    public void setCurrentTurn(int turn)       { lblCurrentTurn.setText("Turn: " + turn); }
    public void setCurrentPlayer(String name)  { lblCurrentPlayer.setText("Current: " + name); }
    public void setDiceResult(int result)      { lblDiceResult.setText("Dice: " + result); }

    // Player info
    public void setPlayerName(String v)          { lblPlayerName.setText("Name: " + v); }
    public void setPlayerOriginalRole(String v)  { lblPlayerOriginalRole.setText("Origin: " + v); }
    public void setPlayerCurrentRole(String v)   { lblPlayerCurrentRole.setText("Role: " + v); }
    public void setPlayerType(String v)          { lblPlayerType.setText("Type: " + v); }
    public void setPlayerEnergy(int v)           { lblPlayerEnergy.setText("Energy: " + v); }
    public void setPlayerPosition(int v)         { lblPlayerPosition.setText("Position: " + v); }
    public void setPlayerStatusEffects(String v) { lblPlayerStatusEffects.setText("Effects: " + v); }

    /** Highlight when player is confused */
    public void setPlayerConfused(boolean confused) {
        lblPlayerCurrentRole.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-size: 13; -fx-font-weight: bold;",
                confused ? "#ff4444" : "#ffd700"));
    }

    // Opponent info
    public void setOpponentName(String v)          { lblOpponentName.setText("Name: " + v); }
    public void setOpponentOriginalRole(String v)  { lblOpponentOriginalRole.setText("Origin: " + v); }
    public void setOpponentCurrentRole(String v)   { lblOpponentCurrentRole.setText("Role: " + v); }
    public void setOpponentType(String v)          { lblOpponentType.setText("Type: " + v); }
    public void setOpponentEnergy(int v)           { lblOpponentEnergy.setText("Energy: " + v); }
    public void setOpponentPosition(int v)         { lblOpponentPosition.setText("Position: " + v); }
    public void setOpponentStatusEffects(String v) { lblOpponentStatusEffects.setText("Effects: " + v); }

    public void setOpponentConfused(boolean confused) {
        lblOpponentCurrentRole.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-size: 13; -fx-font-weight: bold;",
                confused ? "#ff4444" : "#ffd700"));
    }

    // Card drawn
    public void showCardDrawn(String name, String effect) {
        lblCardName.setText("Name: " + name);
        lblCardEffect.setText("Effect: " + effect);
        cardPanel.setVisible(true);
    }
    public void hideCardPanel() { cardPanel.setVisible(false); }

    // Action feedback
    public void setActionFeedback(String msg, boolean error) {
        lblActionFeedback.setText(msg);
        lblActionFeedback.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-size: 11; -fx-font-weight: bold;",
                error ? "#ff4444" : "#00ff88"));
    }

    // Game log
    public void appendLog(String entry) {
        gameLog.appendText(entry + "\n");
    }

    // Buttons
    public Button getBtnUsePowerup() { return btnUsePowerup; }
    public Button getBtnRollDice()   { return btnRollDice; }

    /** Enable/disable action buttons */
    public void setActionsEnabled(boolean powerupEnabled, boolean rollEnabled) {
        btnUsePowerup.setDisable(!powerupEnabled);
        btnRollDice.setDisable(!rollEnabled);
    }

    // ── Board cell access ──────────────────────────────────────────────────

    /**
     * Returns the StackPane at the given board index so controllers can
     * update cell appearance (background, content label, monster tokens).
     */
    public StackPane getCellPane(int index) {
        int row = index / BOARD_SIZE;
        int rawCol = index % BOARD_SIZE;
        int col = (row % 2 == 0) ? rawCol : (BOARD_SIZE - 1 - rawCol);
        return cellPanes[row][col];
    }

    /** Set cell type style (called by controller during board init). */
    public void setCellType(int index, CellType type) {
        StackPane pane = getCellPane(index);
        // Update the background Rectangle fill so the image tints correctly
        pane.getChildren().stream()
                .filter(n -> ("cell_bg_" + index).equals(n.getId()))
                .findFirst()
                .ifPresent(n -> {
                    String hex;
                    switch (type) {
                        case SCARER_DOOR:   hex = "#c8a0d0"; break; // purple tint
                        case LAUGHER_DOOR:  hex = "#a0c8a0"; break; // green tint
                        case MONSTER_CELL:  hex = "#a0b8e0"; break; // blue tint
                        case CARD_CELL:     hex = "#e0a0a0"; break; // red tint
                        case CONVEYOR_BELT: hex = "#a0d0a0"; break; // bright green tint
                        case CONTAM_SOCK:   hex = "#e8dfc0"; break; // cream (sock image handles it)
                        default:            hex = "#e8dfc0"; break; // cream – normal
                    }
                    ((Rectangle) n).setFill(Color.web(hex));
                });
        // Keep a thin border
        pane.setStyle("-fx-border-color: #888888; -fx-border-width: 1;");
    }

    /** Set content text inside a cell (door energy, monster name abbreviation, etc.). */
    public void setCellContent(int index, String text) {
        StackPane pane = getCellPane(index);
        pane.getChildren().stream()
                .filter(n -> n.getId() != null && n.getId().equals("cell_content_" + index))
                .findFirst()
                .ifPresent(n -> ((Label) n).setText(text));
    }

    /** Mark a door cell as activated/exhausted (dims the image layer). */
    public void setDoorActivated(int index, boolean activated) {
        StackPane pane = getCellPane(index);
        pane.getChildren().stream()
                .filter(n -> ("cell_img_" + index).equals(n.getId()))
                .findFirst()
                .ifPresent(n -> n.setOpacity(activated ? 0.3 : 1.0));
        // Also dim the background rectangle
        pane.getChildren().stream()
                .filter(n -> ("cell_bg_" + index).equals(n.getId()))
                .findFirst()
                .ifPresent(n -> n.setOpacity(activated ? 0.5 : 1.0));
    }

    /** Place a monster token on a cell. color distinguishes player vs opponent. */
    public void placeMonsterToken(int index, String abbreviation, String hexColor) {
        StackPane pane = getCellPane(index);
        // Remove any existing token with same id
        pane.getChildren().removeIf(n -> "monster_token".equals(n.getId()));
        Circle token = new Circle(10, Color.web(hexColor));
        token.setId("monster_token");
        token.setStroke(Color.WHITE);
        token.setStrokeWidth(1.5);
        Label abbr = new Label(abbreviation);
        abbr.setFont(Font.font("Arial", FontWeight.BOLD, 8));
        abbr.setStyle("-fx-text-fill: white;");
        StackPane tokenGroup = new StackPane(token, abbr);
        tokenGroup.setId("monster_token");
        StackPane.setAlignment(tokenGroup, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(tokenGroup, new Insets(0, 2, 2, 0));
        pane.getChildren().add(tokenGroup);
    }

    /** Remove all monster tokens from a cell. */
    public void clearMonsterTokens(int index) {
        StackPane pane = getCellPane(index);
        pane.getChildren().removeIf(n -> "monster_token".equals(n.getId()));
    }

    /** Highlight a cell briefly (e.g. after landing). */
    public void highlightCell(int index) {
        StackPane pane = getCellPane(index);
        String orig = pane.getStyle();
        pane.setStyle(orig + "-fx-border-color: #ffd700; -fx-border-width: 2;");
        // Controller should call clearHighlight after a delay
    }
    public void clearHighlight(int index) {
        StackPane pane = getCellPane(index);
        String style = pane.getStyle()
                .replace("-fx-border-color: #ffd700; -fx-border-width: 2;", "");
        pane.setStyle(style);
    }

    public Scene getScene() { return scene; }
    public Stage getStage() { return stage; }

    // ═══════════════════════════════════════════════════════════════════════
    //  INITIALISE FROM GAME
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Reads the fully constructed Game object and populates:
     *  1. Every board cell with the correct colour / type
     *  2. Door cells with their energy value
     *  3. Monster cells with the stationed monster's name
     *  4. Conveyor / Contamination cells with their effect value
     *  5. Player and opponent info panels with starting data
     *  6. Both monster tokens at cell 0
     *  7. Top-bar current-player label
     */
    private void initializeFromGame() {
        initializeBoardCells();
        initializeMonsterPanels();
        initializeMonsterTokens();

        // Top bar – show whose turn it is first
        lblCurrentTurn.setText("Turn: 1");
        lblCurrentPlayer.setText("Current: " + game.getCurrent().getName());
        lblDiceResult.setText("Dice: -");

        appendLog("=== Game Started ===");
        appendLog("Player : " + game.getPlayer().getName()
                + " (" + game.getPlayer().getOriginalRole() + " / "
                + getMonsterType(game.getPlayer()) + ")");
        appendLog("Opponent: " + game.getOpponent().getName()
                + " (" + game.getOpponent().getOriginalRole() + " / "
                + getMonsterType(game.getOpponent()) + ")");
        appendLog("First turn: " + game.getCurrent().getName());
    }

    /** Iterates all 100 indices, reads the Cell from the board and colours accordingly. */
    private void initializeBoardCells() {
        Cell[][] boardCells = game.getBoard().getBoardCells();

        for (int index = 0; index < 100; index++) {
            // Convert linear index → row/col using the same zigzag logic as Board
            int row    = index / BOARD_SIZE;
            int rawCol = index % BOARD_SIZE;
            int col    = (row % 2 == 0) ? rawCol : (BOARD_SIZE - 1 - rawCol);

            Cell cell = boardCells[row][col];

            if (cell == null) {
                setCellTypeAndImage(index, CellType.NORMAL, "/resources/images/normal.png");
                continue;
            }

            if (cell instanceof DoorCell) {
                DoorCell door = (DoorCell) cell;
                if (door.getRole() == Role.SCARER) {
                    setCellTypeAndImage(index, CellType.SCARER_DOOR, "/resources/images/scarer_door.png");
                } else {
                    setCellTypeAndImage(index, CellType.LAUGHER_DOOR, "/resources/images/laugher_door.png");
                }
                setCellContent(index, String.valueOf(door.getEnergy()));

            } else if (cell instanceof MonsterCell) {
                MonsterCell mc = (MonsterCell) cell;
                Monster stationed = mc.getMonster();
                // Each monster has its own image; fall back to generic if missing
                String imgPath = stationed != null
                        ? "/resources/images/monsters/" + safeFileName(stationed.getName()) + ".png"
                        : "/resources/images/monster_cell.png";
                setCellTypeAndImage(index, CellType.MONSTER_CELL, imgPath);
                if (stationed != null) {
                    setCellContent(index, abbrev(stationed.getName()));
                }

            } else if (cell instanceof CardCell) {
                setCellTypeAndImage(index, CellType.CARD_CELL, "/resources/images/card_cell.png");
                setCellContent(index, "?");

            } else if (cell instanceof ConveyorBelt) {
                ConveyorBelt belt = (ConveyorBelt) cell;
                setCellTypeAndImage(index, CellType.CONVEYOR_BELT, "/resources/images/conveyor.png");
                setCellContent(index, "+" + belt.getEffect());

            } else if (cell instanceof ContaminationSock) {
                ContaminationSock sock = (ContaminationSock) cell;
                setCellTypeAndImage(index, CellType.CONTAM_SOCK, "/resources/images/sock.png");
                setCellContent(index, String.valueOf(sock.getEffect()));

            } else {
                setCellTypeAndImage(index, CellType.NORMAL, "/resources/images/normal.png");
            }
        }
    }

    /** Populates the left (player) and right (opponent) info panels. */
    private void initializeMonsterPanels() {
        Monster player   = game.getPlayer();
        Monster opponent = game.getOpponent();

        // Player panel
        lblPlayerName.setText("Name: "         + player.getName());
        lblPlayerOriginalRole.setText("Origin: "+ player.getOriginalRole().toString());
        lblPlayerCurrentRole.setText("Role: "  + player.getRole().toString());
        lblPlayerType.setText("Type: "         + getMonsterType(player));
        lblPlayerEnergy.setText("Energy: "     + player.getEnergy());
        lblPlayerPosition.setText("Position: " + player.getPosition());
        lblPlayerStatusEffects.setText("Effects: none");

        // Opponent panel
        lblOpponentName.setText("Name: "         + opponent.getName());
        lblOpponentOriginalRole.setText("Origin: "+ opponent.getOriginalRole().toString());
        lblOpponentCurrentRole.setText("Role: "  + opponent.getRole().toString());
        lblOpponentType.setText("Type: "         + getMonsterType(opponent));
        lblOpponentEnergy.setText("Energy: "     + opponent.getEnergy());
        lblOpponentPosition.setText("Position: " + opponent.getPosition());
        lblOpponentStatusEffects.setText("Effects: none");
    }

    /** Places the player (cyan) and opponent (red) tokens at their starting cell (0). */
    private void initializeMonsterTokens() {
        placeMonsterToken(
                game.getPlayer().getPosition(),
                abbrev(game.getPlayer().getName()),
                "#00d4ff");   // cyan = player

        placeMonsterToken(
                game.getOpponent().getPosition(),
                abbrev(game.getOpponent().getName()),
                "#e94560");   // red = opponent
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    /**
     * Returns the monster's type as a display string by checking its runtime class.
     * Dasher / Dynamo / Multitasker / Schemer.
     */
    private String getMonsterType(Monster m) {
        if (m instanceof Dasher)      return "Dasher";
        if (m instanceof Dynamo)      return "Dynamo";
        if (m instanceof MultiTasker) return "Multitasker";
        if (m instanceof Schemer)     return "Schemer";
        return "Unknown";
    }

    /** Short 2-char abbreviation used on board tokens. */
    private String abbrev(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
    }

    /**
     * Converts a monster's display name to a safe filename.
     * e.g. "James P. Sullivan" → "james_p_sullivan"
     * Place matching PNG files under src/resources/images/monsters/
     */
    private String safeFileName(String name) {
        if (name == null) return "unknown";
        return name.trim().toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("_+$", "");
    }

    // ── Cell type enum (used by controller for initial board setup) ─────────
    public enum CellType {
        NORMAL, SCARER_DOOR, LAUGHER_DOOR, MONSTER_CELL, CARD_CELL, CONVEYOR_BELT, CONTAM_SOCK
    }
}
