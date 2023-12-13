package controller.enseignant;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import controller.HomeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class SupprimerEnseignantController extends HomeController implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private ComboBox<Integer> listeEnseignant;

	@FXML
	private Label choisir;

	@FXML
	private Button non;

	@FXML
	private Button oui;

	@FXML
	private Label question;

	@FXML
	private Button retour;

	@FXML
	private Label succes;

	@FXML
	private Button supprimer;

	Connection conn = null;
	Statement stat = null;
	
	public void setComboBoxWithEnseignants(ComboBox<Integer> comboBoxEnseignant) {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942");
			stat = conn.createStatement();

			String sql = "SELECT idEnseignant FROM Enseignant";
			ResultSet resultSet = stat.executeQuery(sql);

			ObservableList<Integer> listeEnseignants = FXCollections.observableArrayList();

			while (resultSet.next()) {
				int idEnseignant = resultSet.getInt("idEnseignant");
				listeEnseignants.add(idEnseignant);
			}

			// Set the drop-down menu
			comboBoxEnseignant.setCellFactory(param -> new ListCell<Integer>() {
	            @Override
	            protected void updateItem(Integer num, boolean empty) {
	                super.updateItem(num, empty);

	                if (empty || num == null) {
	                    setText(null);
	                } else {
	                    setText(num.toString());
	                }
	            }
	        });

			comboBoxEnseignant.setItems(listeEnseignants);

			comboBoxEnseignant.setConverter(new StringConverter<Integer>() {
	            @Override
	            public String toString(Integer num) {
	                if (num == null) {
	                    return null;
	                } else {
	                    return num.toString();
	                }
	            }

	            @Override
	            public Integer fromString(String string) {
	                return null;
	            }
	        });

			

		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public void supprimerEnseignant() {
		try {
			// Vérifier si un étudiant a été sélectionné
			if (listeEnseignant.getValue() != null) {
				int idEnseignant = listeEnseignant.getValue();

				// Effectuer la suppression dans la base de données
				String sql = "DELETE FROM Enseignant WHERE idEnseignant = " + idEnseignant;
				int rowsAffected = stat.executeUpdate(sql);

				if (rowsAffected > 0) {
					// Afficher un message de succès
					displayMessage(succes);
				}

				// Cacher la confirmation après la suppression
				hideConfirmation();

				// Actualiser la liste des étudiants dans la ComboBox
				setComboBoxWithEnseignants(listeEnseignant);
			}
		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	
	public void displayConfirmation() {

		if (listeEnseignant.getValue() != null) {
			question.setVisible(true);

			oui.setVisible(true);
			oui.setDisable(false);

			non.setVisible(true);
			non.setDisable(false);
		}

		else {
			displayMessage(choisir);
		}
	}


	public void hideConfirmation() {
		question.setVisible(false);

		oui.setVisible(false);
		oui.setDisable(true);

		non.setVisible(false);
		non.setDisable(true);
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
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		setComboBoxWithEnseignants(listeEnseignant);
	}


}
