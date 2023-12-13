package controller.enseignement;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import controller.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DatabaseConnector;

public class AjouterEnseignementController extends HomeController{

	@FXML
	private Button ajouter;

	@FXML
	private Label champsVides;

	@FXML
	private TextField credit;

	@FXML
	private Label creditEntier;

	@FXML
	private Label creditNegatif;

	@FXML
	private Label echec;

	@FXML
	private TextField nom;

	@FXML
	private Button retour;

	@FXML
	private Label succes;

	@FXML
	private TextField volume;

	@FXML
	private Label volumeEntier;

	@FXML
	private Label volumeNegatif;

	Connection conn = null;
	Statement stat = null;

	private Stage stage;
	private Scene scene;
	private Parent root;


	public void creerEnseignement() {
		try  {
			DatabaseConnector.connectToBDD();
			conn = DatabaseConnector.getConnection();
			
			// on récupère les valeurs des champs de texte
			String nomEnseignement = nom.getText();
			String volumeHoraire = volume.getText();
			String nombreCredit = credit.getText();

			// Vérifier que tous les champs sont remplis
			if (nomEnseignement.isEmpty() || volumeHoraire.isEmpty() || nombreCredit.isEmpty()) {
				// Gérer l'erreur, par exemple afficher un message à l'utilisateur
				displayMessage(champsVides);
				return;
			}

			// Vérifier que le volume horaire est un entier positif
			try {
				int volumeInt = Integer.parseInt(volumeHoraire);
				if (volumeInt <= 0) {
					displayMessage(volumeNegatif);
					return;
				}
			} catch (NumberFormatException e) {
				displayMessage(volumeEntier);
				return;
			}

			// Vérifier que le nombre de crédits est un entier positif
			try {
				int creditInt = Integer.parseInt(nombreCredit);
				if (creditInt <= 0) {
					displayMessage(creditNegatif);
					return;
				}
			} catch (NumberFormatException e) {
				displayMessage(creditEntier);
				return;
			}

			try (Statement stat = conn.createStatement()) {
				String sql = "INSERT INTO Enseignement (nomEnseignement, nombreCredit, volumeHoraire) VALUES (?, ?, ?)";
				try (PreparedStatement statement = conn.prepareStatement(sql)) {
					statement.setString(1, nomEnseignement);
					statement.setInt(2, Integer.parseInt(nombreCredit));
					statement.setInt(3, Integer.parseInt(volumeHoraire));

					int rowsAffected = statement.executeUpdate();

					if (rowsAffected > 0) {
						displayMessage(succes);
					} else {
						displayMessage(echec);
					}
				}
			}

		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}


	public void switchToEnseignement(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "Enseignement.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}



}
