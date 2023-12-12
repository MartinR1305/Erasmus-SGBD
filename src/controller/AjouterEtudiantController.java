package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

	public void creerEtudiant() {
	    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942")) {
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

	            try (Statement stat = conn.createStatement()) {
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
	    }
	}




}
