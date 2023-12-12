package controller.candidature;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import controller.ForAllControllers;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjouterCandidatureController extends ForAllControllers implements Initializable {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Label msgError, msgSuccess;

	@FXML
	private Button ajouter, switchToHome;

	@FXML
	private ComboBox<Integer> bourse1, bourse2, enseignant, etudiant, enseignement1, enseignement2, enseignement3;

	@FXML
	private TextField note1, note2;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Integer> listIDBourse = FXCollections.observableArrayList();
		ObservableList<Integer> listIDEtudiant = FXCollections.observableArrayList();
		ObservableList<Integer> listIDEnseignant = FXCollections.observableArrayList();
		ObservableList<Integer> listIDEnseignement = FXCollections.observableArrayList();

		// Recherche des IDs dans la BDD
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
				"Smo!Aoki1305")) {
			String sqlEtudiant = "SELECT numEtudiant FROM Etudiant";
			String sqlBourse = "SELECT idBourse FROM Bourse";
			String sqlEnseignant = "SELECT idEnseignant FROM Enseignant";
			String sqlEnseignement = "SELECT idEnseignement FROM Enseignement";

			// ID Bourse
			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBourse)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						int idBourse = resultSet.getInt("idBourse");
						listIDBourse.add(idBourse);
					}
				}
			}

			// Num Etudiant
			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlEtudiant)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						int numEtu = resultSet.getInt("numEtudiant");
						listIDEtudiant.add(numEtu);
					}
				}
			}

			// ID Enseignant
			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlEnseignant)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						int idEnseignant = resultSet.getInt("idEnseignant");
						listIDEnseignant.add(idEnseignant);
					}
				}
			}

			// ID Enseignement
			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlEnseignement)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						int idEnseignement = resultSet.getInt("idEnseignement");
						listIDEnseignement.add(idEnseignement);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Initialisation des comboBox
		setComboBoxWithListID(bourse1, listIDBourse);
		setComboBoxWithListID(bourse2, listIDBourse);
		setComboBoxWithListID(etudiant, listIDEtudiant);
		setComboBoxWithListID(enseignant, listIDEnseignant);
		setComboBoxWithListID(enseignement1, listIDEnseignement);
		setComboBoxWithListID(enseignement2, listIDEnseignement);
		setComboBoxWithListID(enseignement3, listIDEnseignement);
	}

	public void ajouterCandidature() {
		// Cas d'erreur
		if ((bourse1.getValue() == null && bourse2.getValue() == null) || etudiant.getValue() == null
				|| enseignant.getValue() == null || enseignement1.getValue() == null || enseignement2.getValue() == null
				|| enseignement3.getValue() == null || !isInteger(note1.getText()) || !isInteger(note2.getText())
				|| bourse1.getValue() == bourse2.getValue() || enseignement1.getValue() == enseignement2.getValue()
				|| enseignement1.getValue() == enseignement3.getValue()
				|| enseignement2.getValue() == enseignement2.getValue()) {
			displayMessage(msgError);

		} else {
			// Il faut vérifier que l'étudiant n'a pas déjà fait une candidature
			
			
			
			
			// Après ajout
			bourse1.setValue(null);
			bourse2.setValue(null);
			etudiant.setValue(null);
			enseignant.setValue(null);
			enseignement1.setValue(null);
			enseignement2.setValue(null);
			enseignement3.setValue(null);
			displayMessage(msgSuccess);
		}
	}

	public void switchToCandidature(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				".." + File.separator + ".." + File.separator + "view" + File.separator + "Candidature.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
