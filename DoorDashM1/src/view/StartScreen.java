package view;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class StartScreen {

    // ── COLORS ─────────────────────────────────────────────
    private static final Color BG_TOP     = Color.web("#0a0015");
    private static final Color BG_BOT     = Color.web("#0d1f0a");

    private static final Color SCARER_CLR = Color.web("#4fc3f7");
    private static final Color LAUGHER_CLR= Color.web("#aed581");
    private static final Color GOLD       = Color.web("#e0b84f");

    private static final Color PANEL_BG   = Color.web("#12002a");
    private static final Color BTN_BASE   = Color.web("#1a003a");

    private static final String FONT_TITLE = "Impact";
    private static final String FONT_BODY  = "Georgia";

    // ── AUDIO ─────────────────────────────────────────────
    private static MediaPlayer bgMusic;
    private static AudioClip sfxHover;
    private static AudioClip sfxClick;

    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void show(Stage stage, RoleSelectedCallback onStart) {

        initAudio();

        StackPane root = new StackPane();
        root.setPrefSize(900, 650);

        // Background
        root.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,
                        new Stop(0, BG_TOP),
                        new Stop(1, BG_BOT)
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        Pane particles = buildParticleLayer(900, 650);
        VBox content = buildContent(stage, onStart);

        root.getChildren().addAll(particles, content);

        Scene scene = new Scene(root, 900, 650);
        stage.setScene(scene);
        stage.setTitle("DooR DasH: Scare vs Laugh Touchdown");
        stage.setResizable(false);
        stage.show();

        playEntranceAnimation(content);

        if (bgMusic != null) bgMusic.play();
    }

    // ─────────────────────────────────────────────
    // CONTENT
    // ─────────────────────────────────────────────
    private static VBox buildContent(Stage stage, RoleSelectedCallback onStart) {

        VBox vbox = new VBox(24);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40, 60, 40, 60));
        vbox.setOpacity(0);

        VBox title = buildTitleBlock();

        Label tagline = new Label("\" We scare because we care \" vs \" We laugh, that's our path \"");
        tagline.setFont(Font.font(FONT_BODY, FontPosture.ITALIC, 13));
        tagline.setTextFill(Color.web("#cccccc", 0.75));

        Line sep = new Line(0,0,520,0);
        sep.setStroke(GOLD);
        sep.setStrokeWidth(1.5);
        sep.setOpacity(0.5);

        Button startBtn = buildButton("⚡ START GAME", SCARER_CLR);
        Button infoBtn  = buildButton("📜 INSTRUCTIONS", LAUGHER_CLR);
        Button exitBtn  = buildButton("✖ EXIT", Color.web("#ef9a9a"));

        startBtn.setOnAction(e -> {
            playClick();
            showRoleDialog(stage, onStart);
        });

        infoBtn.setOnAction(e -> {
            playClick();
            showInstructionsDialog(stage);
        });

        exitBtn.setOnAction(e -> {
            playClick();
            if (bgMusic != null) bgMusic.stop();
            Platform.exit();
        });

        VBox buttons = new VBox(14, startBtn, infoBtn, exitBtn);
        buttons.setAlignment(Pos.CENTER);

        Label version = new Label("v1.0 Monstropolis Edition");
        version.setTextFill(Color.GRAY);

        vbox.getChildren().addAll(title, tagline, sep, buttons, version);
        return vbox;
    }

    // ─────────────────────────────────────────────
    // TITLE
    // ─────────────────────────────────────────────
    private static VBox buildTitleBlock() {

        Label title = new Label("DooR DasH");
        title.setFont(Font.font(FONT_TITLE, FontWeight.BOLD, 72));
        title.setTextFill(GOLD);

        DropShadow glow = new DropShadow();
        glow.setRadius(18);
        glow.setColor(GOLD.deriveColor(0,1,1,0.6));
        title.setEffect(glow);

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2.2), title);
        pulse.setFromX(1); pulse.setToX(1.04);
        pulse.setFromY(1); pulse.setToY(1.04);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();

        Label sub = new Label("SCARE vs LAUGH TOUCHDOWN");
        sub.setTextFill(Color.web("#e0e0e0"));

        VBox box = new VBox(4, title, sub);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    // ─────────────────────────────────────────────
    // BUTTON
    // ─────────────────────────────────────────────
    private static Button buildButton(String text, Color accent) {

        Button btn = new Button(text);
        btn.setFont(Font.font(FONT_TITLE, FontWeight.BOLD, 17));
        btn.setTextFill(Color.WHITE);
        btn.setPrefSize(320, 50);
        btn.setCursor(javafx.scene.Cursor.HAND);

        // Background
        Background base = new Background(new BackgroundFill(
                BTN_BASE,
                new CornerRadii(8),
                Insets.EMPTY
        ));

        Background hover = new Background(new BackgroundFill(
                accent.deriveColor(0,1,1,0.25),
                new CornerRadii(8),
                Insets.EMPTY
        ));

        Border border = new Border(new BorderStroke(
                accent,
                BorderStrokeStyle.SOLID,
                new CornerRadii(8),
                new BorderWidths(2)
        ));

        DropShadow shadow = new DropShadow(8, accent);

        btn.setBackground(base);
        btn.setBorder(border);
        btn.setEffect(shadow);

        btn.setOnMouseEntered(e -> {
            playHover();
            btn.setBackground(hover);
            btn.setEffect(new DropShadow(18, accent));
            btn.setScaleX(1.03);
            btn.setScaleY(1.03);
        });

        btn.setOnMouseExited(e -> {
            btn.setBackground(base);
            btn.setEffect(shadow);
            btn.setScaleX(1);
            btn.setScaleY(1);
        });

        return btn;
    }

    // ─────────────────────────────────────────────
    // PARTICLES
    // ─────────────────────────────────────────────
    private static Pane buildParticleLayer(double w, double h) {

        Pane pane = new Pane();
        pane.setMouseTransparent(true);

        for (int i = 0; i < 22; i++) {

            Color c = (i % 2 == 0) ? SCARER_CLR : LAUGHER_CLR;

            Circle orb = new Circle(5 + Math.random()*6);
            orb.setFill(c.deriveColor(0,1,1,0.4));

            orb.setLayoutX(Math.random()*w);
            orb.setLayoutY(Math.random()*h);

            orb.setEffect(new DropShadow(10, c));

            TranslateTransition tt = new TranslateTransition(Duration.seconds(6 + Math.random()*5), orb);
            tt.setByY(-200);
            tt.setCycleCount(Animation.INDEFINITE);
            tt.play();

            pane.getChildren().add(orb);
        }

        return pane;
    }

    // ─────────────────────────────────────────────
    // ANIMATION
    // ─────────────────────────────────────────────
    private static void playEntranceAnimation(VBox v) {

        FadeTransition f = new FadeTransition(Duration.millis(800), v);
        f.setFromValue(0);
        f.setToValue(1);

        TranslateTransition t = new TranslateTransition(Duration.millis(800), v);
        t.setFromY(30);
        t.setToY(0);

        new ParallelTransition(f, t).play();
    }

    // ─────────────────────────────────────────────
    // AUDIO
    // ─────────────────────────────────────────────
    private static void initAudio() {
        bgMusic  = loadAudio("/resources/sounds/theme.mp3",    true);
        sfxHover = loadClip("/resources/sounds/btn_hover.mp3");
        sfxClick = loadClip("/resources/sounds/btn_click.mp3");
 
        if (bgMusic != null) {
            bgMusic.setCycleCount(MediaPlayer.INDEFINITE);
            bgMusic.setVolume(0.45);
        }
        if (sfxHover != null) sfxHover.setVolume(0.3);
        if (sfxClick != null) sfxClick.setVolume(0.5);
    }
 
    private static MediaPlayer loadAudio(String resourcePath, boolean isMusic) {
        try {
            URL url = StartScreen.class.getResource(resourcePath);
            if (url == null) return null;
            Media media = new Media(url.toExternalForm());
            return new MediaPlayer(media);
        } catch (Exception e) {
            // Sound files are optional – game works without them
            System.out.println("[StartScreen] Audio not loaded: " + resourcePath);
            return null;
        }
    }
    private static AudioClip loadClip(String resourcePath) {
        try {
            URL url = StartScreen.class.getResource(resourcePath);
            if (url == null) return null;
            return new AudioClip(url.toExternalForm());
        } catch (Exception e) {
            System.out.println("[StartScreen] Sound effect not loaded: " + resourcePath);
            return null;
        }
    }
    private static void playHover() {
        if (sfxHover == null) return;
        sfxHover.stop();
        sfxHover.play();
    }
 
    private static void playClick() {
        if (sfxClick == null) return;
        sfxClick.stop();
        sfxClick.play();
    }
    private static void showRoleDialog(Stage owner, RoleSelectedCallback onStart) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
        dialog.setTitle("Choose Your Side");
        dialog.setResizable(false);

        // ── ROOT
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(36, 48, 36, 48));

        root.setBackground(new Background(new BackgroundFill(
                PANEL_BG,
                new CornerRadii(12),
                Insets.EMPTY
        )));

        // ── TITLE
        Label prompt = new Label("Who will you become?");
        prompt.setFont(Font.font(FONT_TITLE, FontWeight.BOLD, 22));
        prompt.setTextFill(GOLD);

        // ── CARDS
        VBox scarerCard = buildRoleCard(
                "⚡ SCARER",
                SCARER_CLR,
                "Harness the power of screams.\nDominate through fear and tradition.",
                "Sulley · Randall · Roz · Waternoose"
        );

        VBox laugherCard = buildRoleCard(
                "😄 LAUGHER",
                LAUGHER_CLR,
                "Revolutionize with laughter energy!\n10× more power than screams.",
                "Mike · Celia · Fungus · Yeti"
        );

        // ── CANCEL BUTTON (NO CSS)
        Button cancelBtn = buildMainButton("Cancel", Color.web("#888888"), Color.web("#555555"));
        cancelBtn.setPrefWidth(200);
        cancelBtn.setPrefHeight(40);

        cancelBtn.setOnAction(e -> dialog.close());

        // ── ACTIONS
        scarerCard.setOnMouseClicked(e -> {
            playClick();
            dialog.close();
            onStart.onRoleSelected("SCARER");
        });

        laugherCard.setOnMouseClicked(e -> {
            playClick();
            dialog.close();
            onStart.onRoleSelected("LAUGHER");
        });

        HBox cards = new HBox(20, scarerCard, laugherCard);
        cards.setAlignment(Pos.CENTER);

        root.getChildren().addAll(prompt, cards, cancelBtn);

        Scene scene = new Scene(root, 520, 310);
        dialog.setScene(scene);

        // ── FADE IN
        FadeTransition ft = new FadeTransition(Duration.millis(250), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        dialog.showAndWait();
    }
    private static VBox buildRoleCard(String title, Color accent, String desc, String monsters) {

        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setPrefWidth(210);
        card.setCursor(javafx.scene.Cursor.HAND);

        // ── BACKGROUND
        card.setBackground(new Background(new BackgroundFill(
                Color.web("#1a003a"),
                new CornerRadii(10),
                Insets.EMPTY
        )));

        // ── BORDER
        card.setBorder(new Border(new BorderStroke(
                accent,
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(2)
        )));

        // ── EFFECT
        DropShadow shadow = new DropShadow(10, accent);
        card.setEffect(shadow);

        // ── TITLE
        Label t = new Label(title);
        t.setFont(Font.font(FONT_TITLE, FontWeight.BOLD, 18));
        t.setTextFill(accent);

        // ── DESCRIPTION
        Label d = new Label(desc);
        d.setFont(Font.font(FONT_BODY, 12));
        d.setTextFill(Color.web("#cccccc"));
        d.setWrapText(true);

        // ── MONSTERS
        Label m = new Label(monsters);
        m.setFont(Font.font(FONT_BODY, 11));
        m.setTextFill(Color.web("#888888"));
        m.setWrapText(true);

        card.getChildren().addAll(t, d, m);

        // ── HOVER EFFECT (NO CSS)
        card.setOnMouseEntered(e -> {
            card.setBackground(new Background(new BackgroundFill(
                    accent.deriveColor(0,1,1,0.15),
                    new CornerRadii(10),
                    Insets.EMPTY
            )));
            card.setEffect(new DropShadow(20, accent));
            card.setScaleX(1.03);
            card.setScaleY(1.03);
        });

        card.setOnMouseExited(e -> {
            card.setBackground(new Background(new BackgroundFill(
                    Color.web("#1a003a"),
                    new CornerRadii(10),
                    Insets.EMPTY
            )));
            card.setEffect(shadow);
            card.setScaleX(1);
            card.setScaleY(1);
        });

        return card;
    }
    
    private static void showInstructionsDialog(Stage owner) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
        dialog.setTitle("How to Play – DooR DasH");
        dialog.setResizable(false);

        // ── SCROLL PANE (CSS → JavaFX)
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        // Replace CSS background with JavaFX Background
        scroll.setBackground(new Background(new BackgroundFill(
                Color.web("#0a0015"),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // ── CONTENT
        VBox content = new VBox(16);
        content.setPadding(new Insets(30, 40, 30, 40));
        content.setBackground(new Background(new BackgroundFill(
                Color.web("#0a0015"),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        content.getChildren().addAll(
                instrTitle("🎮 How to Play"),

                instrSection("🏁 Objective",
                        "Be the first monster to reach Boo's Door (cell 99) with at least 1,000 energy " +
                        "in your canister. Both position AND energy matter – reaching the door empty-handed sends you back!"),

                instrSection("🎲 Turn Sequence",
                        "1. Powerup Phase (optional) – spend 500 energy to activate your monster's special ability.\n" +
                        "2. Roll a 6-sided die and move that many cells forward.\n" +
                        "3. If your destination is occupied by the opponent, you must re-roll.\n" +
                        "4. Resolve the effect of the cell you land on.\n" +
                        "5. Turn passes to the other player."),

                instrSection("🚪 Door Cells (odd cells 1–99)",
                        "• Role Match → You and all teammates gain the door's energy.\n" +
                        "• Role Mismatch → You and all teammates LOSE the door's energy (a shield blocks this).\n" +
                        "• Doors are one-time-use; they're exhausted after first activation."),

                instrSection("👾 Monster Cells",
                        "• Same role → Use your powerup for FREE.\n" +
                        "• Opposite role → If you have more energy than the cell monster, energies are swapped."),

                instrSection("🔄 Transport Cells",
                        "• Conveyor Belts (green) – teleport you FORWARD.\n" +
                        "• Contamination Socks (orange) – teleport you BACKWARD and drain 100 energy " +
                        "(shield blocks the drain, not the move)."),

                instrSection("🃏 Card Cells",
                        "Draw a random card from the pile and apply its effect immediately.\n" +
                        "Cards include: Position Swap, Energy Steal, Start Over, Shield, and Confusion."),

                instrSection("🛡️ Shield",
                        "Blocks the next negative energy effect for your entire team. " +
                        "Only one shield exists – drawing it removes the opponent's shield first."),

                instrSection("🎭 Monster Types",
                        "• Dasher – moves at 2× speed; powerup gives 3× speed for 3 turns.\n" +
                        "• Dynamo – doubles all energy gains AND losses; powerup freezes opponent 1 turn.\n" +
                        "• Multitasker – moves at ½ speed but +200 to every energy change; powerup restores normal speed 2 turns.\n" +
                        "• Schemer – +10 to every energy change; powerup steals 10 energy from ALL other monsters (ignores shields)."),

                instrSection("🏆 Win Condition",
                        "Land exactly on cell 99 AND have ≥ 1,000 energy. If you don't have enough energy, " +
                        "the game continues until you (or your opponent) meets both conditions.")
        );

        scroll.setContent(content);

        // ── CLOSE BUTTON
        Button closeBtn = buildMainButton("Close", GOLD, Color.web("#4a3000"));
        closeBtn.setOnAction(e -> dialog.close());

        // prevent hover sound spam
        closeBtn.setOnMouseEntered(e -> {});

        VBox footer = new VBox(closeBtn);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10, 0, 20, 0));
        footer.setBackground(new Background(new BackgroundFill(
                Color.web("#0a0015"),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        // ── WRAPPER
        BorderPane wrapper = new BorderPane();
        wrapper.setCenter(scroll);
        wrapper.setBottom(footer);
        wrapper.setBackground(new Background(new BackgroundFill(
                Color.web("#0a0015"),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        // ── SCENE
        Scene scene = new Scene(wrapper, 600, 520);
        dialog.setScene(scene);

        // fade animation
        FadeTransition ft = new FadeTransition(Duration.millis(250), wrapper);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        dialog.showAndWait();
    }
    private static VBox instrSection(String heading, String body) {

        Label h = new Label(heading);
        h.setFont(Font.font(FONT_TITLE, FontWeight.BOLD, 15));
        h.setTextFill(SCARER_CLR);

        Label b = new Label(body);
        b.setFont(Font.font(FONT_BODY, 13));
        b.setTextFill(Color.web("#cccccc"));
        b.setWrapText(true);

        Line line = new Line(0, 0, 500, 0);
        line.setStroke(Color.web("#333344"));

        VBox box = new VBox(6, h, b, line);
        return box;
    }
    private static Label instrTitle(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(FONT_TITLE, FontWeight.BOLD, 26));
        l.setTextFill(GOLD);
        return l;
    }
    private static Button buildMainButton(String text, Color accentColor, Color hoverColor) {

        Button btn = new Button(text);

        btn.setFont(Font.font(FONT_TITLE, FontWeight.BOLD, 17));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(320);
        btn.setPrefHeight(50);
        btn.setCursor(javafx.scene.Cursor.HAND);

        // ── Base background
        Background baseBg = new Background(new BackgroundFill(
                BTN_BASE,
                new CornerRadii(8),
                Insets.EMPTY
        ));

        // ── Hover background
        Background hoverBg = new Background(new BackgroundFill(
                hoverColor.deriveColor(0, 1, 1, 0.25),
                new CornerRadii(8),
                Insets.EMPTY
        ));

        // ── Border
        Border border = new Border(new BorderStroke(
                accentColor,
                BorderStrokeStyle.SOLID,
                new CornerRadii(8),
                new BorderWidths(2)
        ));

        // ── Glow effect
        DropShadow shadow = new DropShadow(10, accentColor);

        btn.setBackground(baseBg);
        btn.setBorder(border);
        btn.setEffect(shadow);

        // ── Hover behavior
        btn.setOnMouseEntered(e -> {
            btn.setBackground(hoverBg);
            btn.setEffect(new DropShadow(18, accentColor));
            btn.setScaleX(1.03);
            btn.setScaleY(1.03);
        });

        btn.setOnMouseExited(e -> {
            btn.setBackground(baseBg);
            btn.setEffect(shadow);
            btn.setScaleX(1);
            btn.setScaleY(1);
        });

        // ── Press effect
        btn.setOnMousePressed(e -> {
            btn.setScaleX(0.97);
            btn.setScaleY(0.97);
        });

        btn.setOnMouseReleased(e -> {
            btn.setScaleX(1);
            btn.setScaleY(1);
        });

        return btn;
    }

    // ─────────────────────────────────────────────
    @FunctionalInterface
    public interface RoleSelectedCallback {
        void onRoleSelected(String role);
    }
}