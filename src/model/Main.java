package model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	Connection conn = null;
	Statement stat = null;

	public Main() throws IOException {
		DatabaseConnector.connectToBDD();
//		DatabaseConnector.createBDD();
//		DatabaseConnector.createData();
		DatabaseConnector.closeBDD();
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// Here we start the application
			Parent root = FXMLLoader
					.load(getClass().getResource(".." + File.separator + "view" + File.separator + "Home.fxml"));
			Scene scene1 = new Scene(root);
			primaryStage.setScene(scene1);
			primaryStage.centerOnScreen();
			primaryStage.show();

			// Set on center the stage
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
			primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	
}
