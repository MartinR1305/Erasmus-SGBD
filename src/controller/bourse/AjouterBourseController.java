package controller.bourse;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjouterBourseController extends HomeController implements Initializable {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Button ajouterBourse, retour;

	@FXML
	private TextField destination, nbPostesDispo;

	@FXML
	private Label errorMsg, successMsg;

	@FXML
	private ComboBox<Integer> listeEnseignant;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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

			setComboBoxWithListID(listeEnseignant, listIDEnseignant);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ajouterBourse(ActionEvent event) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
				"Smo!Aoki1305")) {

			if (destination.getText() == null || nbPostesDispo.getText() == null
					|| listeEnseignant.getValue() == null) {
				displayMessage(errorMsg);
			} else {
				if (!isInteger(nbPostesDispo.getText())) {
					displayMessage(errorMsg);
				} else {

					String strDestination = destination.getText();
					int intPostesDispo = Integer.valueOf(nbPostesDispo.getText());
					int idEnseignant = listeEnseignant.getValue();

					try (Statement stat = connection.createStatement()) {
						String sql = "INSERT INTO Bourse (destination, nombrePostes, responsableLocal) VALUES (?, ?, ?)";
						try (PreparedStatement statement = connection.prepareStatement(sql)) {
							statement.setString(1, strDestination);
							statement.setInt(2, intPostesDispo);
							statement.setInt(3, idEnseignant);

							int rowsAffected = statement.executeUpdate();
							if (rowsAffected > 0) {
								System.out.println("Insertion réussie.");
								destination.setText(null);
								nbPostesDispo.setText(null);
								listeEnseignant.setValue(null);
								displayMessage(successMsg);
							} else {
								System.out.println("Échec de l'insertion.");
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void switchToBourse(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "Bourse.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
