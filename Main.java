import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[]args) {
		launch(args); 
		
	}
	
	public void start(Stage primaryStage) throws IOException {
    	
		Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml")); 
    	primaryStage.setTitle("Client");
    	primaryStage.setScene(new Scene(root,650,550));
    	primaryStage.show(); 
    	
    }
}
