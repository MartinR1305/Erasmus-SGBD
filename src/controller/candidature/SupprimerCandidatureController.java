package controller.candidature;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

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
import javafx.stage.Stage;
import model.DatabaseConnector;

public class SupprimerCandidatureController extends CandidatureController implements Initializable {
	@FXML
	private Button non, oui, retour, supprimer;

	@FXML
	private Label question, errorMsg, successMsg;

	@FXML
	private ComboBox<Integer> listeIDCandidature;

	private Stage stage;
	private Scene scene;
	private Parent root;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		obtenirListIDEtActualiserComboBox();
	}

	public void supprimerCandidature() throws SQLException {
		try {
			DatabaseConnector.connectToBDD();
			Connection connection = DatabaseConnector.getConnection();
			
			int idCandidatureToSuppr = listeIDCandidature.getValue();
			
			try (Statement stat = connection.createStatement()) {
				String sql = "DELETE FROM Candidature WHERE idCandidature = ?";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setInt(1, idCandidatureToSuppr);
					int rowsAffected = statement.executeUpdate();

					if (rowsAffected > 0) {
						System.out.println("Suppression réussie.");
						displayMessage(successMsg);
						listeIDCandidature.setValue(null);
						hideConfirmation();
						obtenirListIDEtActualiserComboBox();
					} else {
						System.out.println("Aucune candidature trouvée avec l'ID spécifié.");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}finally {
			DatabaseConnector.closeBDD();
		}
	}

	public void displayConfirmation() {
		if (listeIDCandidature.getValue() != null) {
			question.setVisible(true);

			oui.setVisible(true);
			oui.setDisable(false);

			non.setVisible(true);
			non.setDisable(false);
		}

		else {
			displayMessage(errorMsg);
		}
	}

	public void hideConfirmation() {
		question.setVisible(false);

		oui.setVisible(false);
		oui.setDisable(true);

		non.setVisible(false);
		non.setDisable(true);
	}
	
	public void obtenirListIDEtActualiserComboBox() {
		ObservableList<Integer> listIDCand = FXCollections.observableArrayList();

		// Recherche des IDs dans la BDD
		try {
			DatabaseConnector.connectToBDD();
			Connection connection = DatabaseConnector.getConnection();
			
			String sqlCandidature = "SELECT idCandidature FROM Candidature";

			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCandidature)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						int idCandidature = resultSet.getInt("idCandidature");
						listIDCand.add(idCandidature);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setComboBoxWithListID(listeIDCandidature, listIDCand);
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