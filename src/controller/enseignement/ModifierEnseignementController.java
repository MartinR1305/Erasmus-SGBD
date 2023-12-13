package controller.enseignement;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class ModifierEnseignementController extends HomeController{

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
	private Button modifier;

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

	private int idEnseignement;

	Connection conn = null;
	Statement stat = null;

	private Stage stage;
	private Scene scene;
	private Parent root;


	public void initData(int enseignementNumber) {

		idEnseignement = enseignementNumber;

		try {
			DatabaseConnector.connectToBDD();
			conn = DatabaseConnector.getConnection();
			stat = conn.createStatement();

			String sql = "SELECT nomEnseignement, nombreCredit, volumeHoraire FROM Enseignement WHERE idEnseignement = " + idEnseignement;
			ResultSet resultSet = stat.executeQuery(sql);

			if (resultSet.next()) {
				// Remplissez les champs de texte avec les informations récupérées
				nom.setText(resultSet.getString("nomEnseignement"));
				volume.setText(String.valueOf(resultSet.getInt("volumeHoraire")));
				credit.setText(String.valueOf(resultSet.getInt("nombreCredit")));

			}
		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}


	public void modifier() {
		// Get the updated information from the text fields
		String newNom = nom.getText();
		String textCredit = credit.getText();
		String textVolume = volume.getText();

		// Vérifier que tous les champs sont remplis
		if (newNom.isEmpty() || textCredit.isEmpty() || textVolume.isEmpty()) {
			// Gérer l'erreur, par exemple afficher un message à l'utilisateur
			displayMessage(champsVides);
			return;
		}

		// Vérifier que le volume horaire est un entier positif
		try {
			int volumeInt = Integer.parseInt(textVolume);
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
			int creditInt = Integer.parseInt(textCredit);
			if (creditInt <= 0) {
				displayMessage(creditNegatif);
				return;
			}
		} catch (NumberFormatException e) {
			displayMessage(creditEntier);
			return;
		}

		try  {
			DatabaseConnector.connectToBDD();
			Connection conn = DatabaseConnector.getConnection();
			
			String sql = "UPDATE Enseignement SET nomEnseignement = ?, volumeHoraire = ?, nombreCredit = ? WHERE idEnseignement = ?";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setString(1, newNom);
				statement.setInt(2, Integer.parseInt(textVolume));
				statement.setInt(3, Integer.parseInt(textCredit));
				statement.setInt(4, idEnseignement);

				int rowsAffected = statement.executeUpdate();

				if (rowsAffected > 0) {
					// Afficher un message de succès, par exemple
					displayMessage(succes);
				} else {
					// Afficher un message d'échec, par exemple
					displayMessage(echec);
				}
			}
		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}




	public void switchToChoixModifierEnseignement(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "ChoixModifierEnseignement.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
