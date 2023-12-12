package controller;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ForAllControllers {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void displayMessage(Label label) {
		// Create a new thread to manage the wait
		Thread waitThread = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(3000);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							// Make label invisible after pause
							label.setVisible(false);
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		label.setVisible(true);
		waitThread.start();
	}
	
	public boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public void switchToVisualiser(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + "view" + File.separator + "ChoixCandidature.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
