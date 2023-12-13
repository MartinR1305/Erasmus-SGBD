package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

public class SupprimerEtudiantController extends HomeController implements Initializable{

	@FXML
	private ComboBox<Integer> listeEtudiant;

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

	public void setComboBoxWithEtudiants(ComboBox<Integer> comboBoxEtudiant) {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942");
			stat = conn.createStatement();

			String sql = "SELECT numEtudiant FROM Etudiant";
			ResultSet resultSet = stat.executeQuery(sql);

			ObservableList<Integer> listeEtudiants = FXCollections.observableArrayList();

			while (resultSet.next()) {
				int numEtudiant = resultSet.getInt("numEtudiant");
				listeEtudiants.add(numEtudiant);
			}

			// Set the drop-down menu
			comboBoxEtudiant.setCellFactory(param -> new ListCell<Integer>() {
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

			comboBoxEtudiant.setItems(listeEtudiants);

			// We set display of level once selected
			comboBoxEtudiant.setConverter(new StringConverter<Integer>() {
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


	public void supprimerEtudiant() {
		try {
			// Vérifier si un étudiant a été sélectionné
			if (listeEtudiant.getValue() != null) {
				int numEtudiant = listeEtudiant.getValue();

				// Effectuer la suppression dans la base de données
				String sql = "DELETE FROM Etudiant WHERE numEtudiant = " + numEtudiant;
				int rowsAffected = stat.executeUpdate(sql);

				if (rowsAffected > 0) {
					// Afficher un message de succès
					displayMessage(succes);
				}

				// Cacher la confirmation après la suppression
				hideConfirmation();

				// Actualiser la liste des étudiants dans la ComboBox
				setComboBoxWithEtudiants(listeEtudiant);
			}
		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}


	public void displayConfirmation() {

		if (listeEtudiant.getValue() != null) {
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

	public void initialize(URL arg0, ResourceBundle arg1) {
		setComboBoxWithEtudiants(listeEtudiant);
	}

}
