package controller.enseignant;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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

public class AjouterEnseignantController extends HomeController{

	Connection conn = null;
	Statement stat = null;
	
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
	private Button ajouter;

	@FXML
	private TextField nom;

	@FXML
	private TextField note;

	@FXML
	private TextField prenom;

	@FXML
	private Button retour;

	public void creerEnseignant() {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942")) {
			// on récupère les valeurs des champs de texte
			String nomEnseignant = nom.getText();
			String prenomEnseignant = prenom.getText();

			// Vérifier que tous les champs sont remplis
			if (nomEnseignant.isEmpty() || prenomEnseignant.isEmpty()) {
				// Gérer l'erreur, par exemple afficher un message à l'utilisateur
				displayMessage(champsVides);
				return;
			}



			try (Statement stat = conn.createStatement()) {
				String sql = "INSERT INTO Enseignant (nomEnseignant, prenomEnseignant) VALUES (?, ?)";
				try (PreparedStatement statement = conn.prepareStatement(sql)) {
					statement.setString(1, nomEnseignant);
					statement.setString(2, prenomEnseignant);

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
	
	public void switchToEnseignant(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "Enseignant.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
