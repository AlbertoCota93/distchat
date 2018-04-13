package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root =  FXMLLoader.load(getClass().getClassLoader().getResource("GUI.fxml"));;
			Scene scene = new Scene(root,300,350);
			primaryStage.setScene(scene);
			primaryStage.show();
			scene.getRoot().requestFocus();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
