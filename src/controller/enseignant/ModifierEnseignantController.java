package controller.enseignant;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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

public class ModifierEnseignantController extends HomeController{


	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Label echec;

	@FXML
	private Label succes;

	@FXML
	private Label champsVides;

	@FXML
	private Button modifier;

	@FXML
	private TextField nom;

	@FXML
	private TextField prenom;

	@FXML
	private Button retour;

	private int idEnseignant;

	Connection conn = null;
	Statement stat = null;

	public void initData(int teacherNumber) {
		idEnseignant = teacherNumber;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942");
			stat = conn.createStatement();

			String sql = "SELECT nomEnseignant, prenomEnseignant FROM Enseignant WHERE idEnseignant = " + idEnseignant;
			ResultSet resultSet = stat.executeQuery(sql);

			if (resultSet.next()) {
				// Remplissez les champs de texte avec les informations récupérées
				nom.setText(resultSet.getString("nomEnseignant"));
				prenom.setText(resultSet.getString("prenomEnseignant"));
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
		String newPrenom = prenom.getText();

		// Vérifier que tous les champs sont remplis
		if (newNom.isEmpty() || newPrenom.isEmpty()) {
			// Gérer l'erreur, par exemple afficher un message à l'utilisateur
			displayMessage(champsVides);
			return;
		}


		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942")) {
			String sql = "UPDATE Enseignant SET nomEnseignant = ?, prenomEnseignant = ? WHERE idEnseignant = ?";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setString(1, newNom);
				statement.setString(2, newPrenom);
				statement.setInt(3, idEnseignant);

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



	public void switchToChoixModifierEnseignant(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "ChoixModifierEnseignant.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
