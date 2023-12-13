package controller.candidature;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	private Label msgError, msgSuccess, msgErrorEtudiant;

	@FXML
	private Button ajouter, switchToHome;

	@FXML
	private ComboBox<Integer> bourse1, bourse2, enseignant, etudiant, enseignement1, enseignement2, enseignement3;

	@FXML
	private TextField note1, note2;

	private ObservableList<Integer> listIDBourse = FXCollections.observableArrayList();
	private ObservableList<Integer> listIDEtudiant = FXCollections.observableArrayList();
	private ObservableList<Integer> listIDEnseignant = FXCollections.observableArrayList();
	private ObservableList<Integer> listIDEnseignement = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

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
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
				"Smo!Aoki1305")) {

			// Cas d'erreur
			if ((bourse1.getValue() == null && bourse2.getValue() == null) || etudiant.getValue() == null
					|| enseignant.getValue() == null || enseignement1.getValue() == null
					|| enseignement2.getValue() == null || enseignement3.getValue() == null || note1.getText() == null
					|| note2.getText() == null || bourse1.getValue() == bourse2.getValue()
					|| enseignement1.getValue() == enseignement2.getValue()
					|| enseignement1.getValue() == enseignement3.getValue()
					|| enseignement2.getValue() == enseignement3.getValue()) {
				displayMessage(msgError);

			} else {
				if (!isInteger(note1.getText()) || !isInteger(note1.getText())) {
					displayMessage(msgError);
				} else {
					int numEtudiant = etudiant.getValue();
					ObservableList<Integer> listIDEtudiantCandidature = FXCollections.observableArrayList();
					String sqlEtudiantCandidature = "SELECT idEtudiant FROM Candidature";

					// ID Bourse
					try (PreparedStatement preparedStatement = connection.prepareStatement(sqlEtudiantCandidature)) {
						try (ResultSet resultSet = preparedStatement.executeQuery()) {
							while (resultSet.next()) {
								int numEtudiantCandidature = resultSet.getInt("idEtudiant");
								listIDEtudiantCandidature.add(numEtudiantCandidature);
							}
						}
					}

					// On vérifie que l'étudiant n'a pas déjà fait une candidature
					if (!listIDEtudiantCandidature.contains(numEtudiant)) {

						int idBourse1 = bourse1.getValue();
						int idBourse2 = bourse2.getValue();
						int idEnseignant = enseignant.getValue();
						int idEnseignement1 = enseignement1.getValue();
						int idEnseignement2 = enseignement2.getValue();
						int idEnseignement3 = enseignement3.getValue();
						int valueNote1 = Integer.valueOf(note1.getText());
						int valueNote2 = Integer.valueOf(note2.getText());
						int noteEtudiant = 0;

						String sqlNoteEtudiant = "SELECT noteMoyenne FROM Etudiant WHERE numEtudiant = numEtudiant ";
						try (PreparedStatement preparedStatement = connection.prepareStatement(sqlNoteEtudiant)) {
							try (ResultSet resultSet = preparedStatement.executeQuery()) {
								while (resultSet.next()) {
									noteEtudiant = resultSet.getInt("noteMoyenne");
								}
							}
						}

						try (Statement stat = connection.createStatement()) {
							String sql = "INSERT INTO Candidature (score, noteResponsableErasmus, noteResponsableLocal, idEtudiant, responsableErasmus, idBourse1, idBourse2, idEnseignement1, idEnseignement2, idEnseignement3) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
							try (PreparedStatement statement = connection.prepareStatement(sql)) {
								int score = ((noteEtudiant * 5) + valueNote1 + valueNote2) / 3;

								statement.setInt(1, score);
								statement.setInt(2, valueNote1);
								statement.setInt(3, valueNote2);
								statement.setInt(4, numEtudiant);
								statement.setInt(5, idEnseignant);
								statement.setInt(6, idBourse1);
								statement.setInt(7, idBourse2);
								statement.setInt(8, idEnseignement1);
								statement.setInt(9, idEnseignement2);
								statement.setInt(10, idEnseignement3);

								int rowsAffected = statement.executeUpdate();
								if (rowsAffected > 0) {
									System.out.println("Insertion réussie.");
								} else {
									System.out.println("Échec de l'insertion.");
								}
							}
						} catch (SQLException e) {
							e.printStackTrace(); // Gérez les exceptions de manière appropriée dans votre application
						}

						bourse1.setValue(null);
						bourse2.setValue(null);
						etudiant.setValue(null);
						enseignant.setValue(null);
						enseignement1.setValue(null);
						enseignement2.setValue(null);
						enseignement3.setValue(null);
						note1.setText(null);
						note2.setText(null);
						displayMessage(msgSuccess);
					} else {
						displayMessage(msgErrorEtudiant);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
