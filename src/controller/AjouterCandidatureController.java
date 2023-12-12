package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjouterController extends ForAllControllers implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Label msgError;

	@FXML
	private Button ajouter, switchToHome;

	@FXML
	private ComboBox<Integer> bourse1, bourse2, enseignant, etudiant;

	@FXML
	private ComboBox<String> enseignement1, enseignement2, enseignement3;

	@FXML
	private TextField note1, note2;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	public void ajouterCandidature() {
		// Cas d'erreur
		if ((bourse1.getValue() == null && bourse2.getValue() == null) || etudiant.getValue() == null|| enseignant.getValue() == null 
				|| enseignement1.getValue() == null || enseignement2.getValue() == null || enseignement3.getValue() == null
				|| !isInteger(note1.getText()) || !isInteger(note2.getText())) {
			displayMessage(msgError);

		} else {
			// Il faut vérifier que l'étudiant n'a pas déjà fait une candidature
		}
	}

	public void switchToHome(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + "view" + File.separator + "Home.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
