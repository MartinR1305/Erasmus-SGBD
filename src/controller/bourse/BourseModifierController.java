package controller.bourse;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.HomeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BourseModifierController extends HomeController {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Label errorMsg, successMsg, labelIDBourse;

	@FXML
	private TextField destination, nbPostesDisponible;

	@FXML
	private ComboBox<Integer> idEnseignant;

	@FXML
	private Button modifier, retour;

	private int idBourse;

	public void getDataBourse(int idB) {
		idBourse = idB;
		labelIDBourse.setText("ID Bourse : " + idBourse);
		ObservableList<Integer> listIDEnseignant = FXCollections.observableArrayList();

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
				"Smo!Aoki1305")) {
			String sqlEnseignant = "SELECT idEnseignant FROM Enseignant";

			// ID Enseignant
			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlEnseignant)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						int idEnseignant = resultSet.getInt("idEnseignant");
						listIDEnseignant.add(idEnseignant);
					}
				}
			}
			setComboBoxWithListID(idEnseignant, listIDEnseignant);

			String sql = "SELECT * FROM Bourse WHERE idBourse = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setInt(1, idBourse);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						// Récupérez les informations de la bourse
						String strDestination = resultSet.getString("destination");
						int intNombrePostes = resultSet.getInt("nombrePostes");
						int intResponsableLocal = resultSet.getInt("responsableLocal");

						destination.setText(strDestination);
						nbPostesDisponible.setText(String.valueOf(intNombrePostes));
						idEnseignant.setValue(intResponsableLocal);

					} else {
						System.out.println("Aucune bourse trouvée avec l'ID : " + idBourse);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void modifierBourse(ActionEvent event) throws SQLException {
		if (destination.getText() == null || nbPostesDisponible.getText() == null || idEnseignant.getValue() == null) {
			displayMessage(errorMsg);
		} else {
			if (!isInteger(nbPostesDisponible.getText())) {
				displayMessage(errorMsg);
			} else {

				try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
						"Smo!Aoki1305")) {
					String sql = "UPDATE Bourse SET destination = ?, nombrePostes = ?, responsableLocal = ? WHERE idBourse = ?";

					try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
						preparedStatement.setString(1, destination.getText());
						preparedStatement.setInt(2, Integer.valueOf(nbPostesDisponible.getText()));
						preparedStatement.setInt(3, idEnseignant.getValue());
						preparedStatement.setInt(4, idBourse);

						int lignesModifiees = preparedStatement.executeUpdate();

						if (lignesModifiees > 0) {
							System.out.println("Les informations de la bourse ont été mises à jour avec succès.");
							displayMessage(successMsg);	
							
						} else {
							System.out.println("Aucune bourse trouvée avec l'ID : " + idBourse);
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void switchToChoixBourse(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				".." + File.separator + ".." + File.separator + "view" + File.separator + "ChoixBourseModifier.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
