package app;
	import view.StartScreen;
	import javafx.application.Application;
	import javafx.stage.Stage;
	import javafx.scene.Scene;
	import javafx.scene.layout.*;
	import javafx.scene.paint.Color;
	import javafx.scene.control.*;




public class Main extends Application{
	public void start(Stage stage){
		StartScreen.show(stage, role -> showGameScreen(stage, role));
	}
	
	private void showGameScreen(Stage stage, String role) {
        // TODO: build and set your game scene here
        System.out.println("Starting game as: " + role);
    }
	
	
	
	public static void main(String[] args){
		launch(args);
	}
}
