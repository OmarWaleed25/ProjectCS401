package app;
	import game.engine.Game;
	import game.engine.Role;
    
    import view.StartScreen;
    import javafx.application.Application;
    import javafx.stage.Stage;
	import javafx.scene.Scene;
	import javafx.scene.layout.*;
	import javafx.scene.paint.Color;
	import javafx.scene.control.*;
	import game.engine.Role;
	import view.GameView;

	import java.io.IOException;


public class Main extends Application{
	Game game;
	public void start(Stage stage) {
		StartScreen.show(stage, role -> {
            try {
                showGameScreen(stage, role);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
	}

	private void showGameScreen(Stage stage, String role) throws IOException {
		if(role.equals("SCARER"))
			game = new Game(Role.SCARER);
		else
			game = new Game(Role.LAUGHER);
		
		GameView g = new GameView(stage,game);
		g.show();
		
	}
	

	
	
	
	public static void main(String[] args){
		launch(args);
	}
}
