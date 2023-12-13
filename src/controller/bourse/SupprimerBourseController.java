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
import javafx.stage.Stage;

public class SupprimerBourseController extends HomeController implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
    private Label successMsg, errorMsg, question;

    @FXML
    private ComboBox<Integer> listeIDBourse;

    @FXML
    private Button non, oui, retour, supprimer;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		obtenirListIDEtActualiserComboBox();
	}

    public void supprimerBourse() throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
				"Smo!Aoki1305")) {
			int idBourseToSuppr = listeIDBourse.getValue();
			
			try (Statement stat = connection.createStatement()) {
				String sql = "DELETE FROM Bourse WHERE idBourse = ?";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setInt(1, idBourseToSuppr);
					int rowsAffected = statement.executeUpdate();

					if (rowsAffected > 0) {
						System.out.println("Suppression réussie.");
						displayMessage(successMsg);
						listeIDBourse.setValue(null);
						hideConfirmation();
						obtenirListIDEtActualiserComboBox();
					} else {
						System.out.println("Aucune bourse trouvée avec l'ID spécifié.");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }

	public void displayConfirmation() {
		if (listeIDBourse.getValue() != null) {
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


	public void switchToBourse(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass()
				.getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "Bourse.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void obtenirListIDEtActualiserComboBox() {
		ObservableList<Integer> listIDB = FXCollections.observableArrayList();
		
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
				"Smo!Aoki1305")) {
			String sqlBourse = "SELECT idBourse FROM Bourse";

			// ID Bourse
			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBourse)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						int idBourse = resultSet.getInt("idBourse");
						listIDB.add(idBourse);
					}
				}
			}
			setComboBoxWithListID(listeIDBourse, listIDB);

		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}
