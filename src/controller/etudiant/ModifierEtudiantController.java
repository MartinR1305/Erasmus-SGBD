package controller.etudiant;

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

public class ModifierEtudiantController extends HomeController{

	private Stage stage;
	private Scene scene;
	private Parent root;

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
	private Button modifier;

	@FXML
	private TextField nom;

	@FXML
	private TextField note;

	@FXML
	private TextField prenom;

	@FXML
	private Button retour;

	private int numEtudiant;

	Connection conn = null;
	Statement stat = null;

	public void initData(int studentNumber) {
		numEtudiant = studentNumber;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942");
			stat = conn.createStatement();

			String sql = "SELECT nomEtudiant, prenomEtudiant, noteMoyenne FROM Etudiant WHERE numEtudiant = " + numEtudiant;
			ResultSet resultSet = stat.executeQuery(sql);

			if (resultSet.next()) {
				// Remplissez les champs de texte avec les informations récupérées
				nom.setText(resultSet.getString("nomEtudiant"));
				prenom.setText(resultSet.getString("prenomEtudiant"));
				note.setText(String.valueOf(resultSet.getDouble("noteMoyenne")));
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
	    String noteText = note.getText();

	    // Vérifier que tous les champs sont remplis
	    if (newNom.isEmpty() || newPrenom.isEmpty() || noteText.isEmpty()) {
	        // Gérer l'erreur, par exemple afficher un message à l'utilisateur
	        displayMessage(champsVides);
	        return;
	    }

	    // on verifie si la note est un double
	    try {
	        double noteEtudiant = Double.parseDouble(noteText);

	        // Vérifier si la note est comprise entre 0 et 20
	        if (noteEtudiant < 0 || noteEtudiant > 20) {
	            displayMessage(noteComprise);
	            return;
	        }

	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942")) {
	            String sql = "UPDATE Etudiant SET nomEtudiant = ?, prenomEtudiant = ?, noteMoyenne = ? WHERE numEtudiant = ?";
	            try (PreparedStatement statement = conn.prepareStatement(sql)) {
	                statement.setString(1, newNom);
	                statement.setString(2, newPrenom);
	                statement.setDouble(3, noteEtudiant);
	                statement.setInt(4, numEtudiant);

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
	    } catch (NumberFormatException e) {
	        // gestion d'erreur, par exemple afficher un message à l'utilisateur
	        displayMessage(entier);
	    }
	}


	public void switchToChoixModifierEtudiant(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "ChoixModifierEtudiant.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
