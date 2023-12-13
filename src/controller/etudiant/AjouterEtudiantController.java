package controller.etudiant;

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

public class AjouterEtudiantController extends HomeController{

	Connection conn = null;
	Statement stat = null;

	@FXML
	private Label echec;

	@FXML
	private Label entier;

	@FXML
	private Label noteComprise;

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

	private Stage stage;
	private Scene scene;
	private Parent root;



	public void creerEtudiant() {
		try {
			// Connexion à la base de données
			DatabaseConnector.connectToBDD();

			// on récupère les valeurs des champs de texte
			String nomEtudiant = nom.getText();
			String prenomEtudiant = prenom.getText();
			String noteEtudiantStr = note.getText();

			// Vérifier que tous les champs sont remplis
			if (nomEtudiant.isEmpty() || prenomEtudiant.isEmpty() || noteEtudiantStr.isEmpty()) {
				// Gérer l'erreur, par exemple afficher un message à l'utilisateur
				displayMessage(champsVides);
				return;
			}

			// on vérifie si la note est un double
			try {
				double noteEtudiant = Double.parseDouble(noteEtudiantStr);

				// Vérifier si la note est comprise entre 0 et 20
				if (noteEtudiant < 0 || noteEtudiant > 20) {
					displayMessage(noteComprise);
					return;
				}

				// Utilisation de la connexion de la classe DatabaseConnector
				try (Connection conn = DatabaseConnector.getConnection();
						Statement stat = conn.createStatement()) {
					String sql = "INSERT INTO Etudiant (nomEtudiant, prenomEtudiant, noteMoyenne) VALUES (?, ?, ?)";
					try (PreparedStatement statement = conn.prepareStatement(sql)) {
						statement.setString(1, nomEtudiant);
						statement.setString(2, prenomEtudiant);
						statement.setDouble(3, noteEtudiant);

						int rowsAffected = statement.executeUpdate();

						if (rowsAffected > 0) {
							displayMessage(succes);
						} else {
							displayMessage(echec);
						}
					}
				}
			} catch (NumberFormatException e) {
				// gestion d'erreur, par exemple afficher un message à l'utilisateur
				displayMessage(entier);
			}
		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			// Fermer la connexion à la base de données
			DatabaseConnector.closeBDD();
		}
	}

	public void switchToEtudiant(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "Etudiant.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}





}
