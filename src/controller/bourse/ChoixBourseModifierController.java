package controller.bourse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class ChoixBourseModifierController extends HomeController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Button Retour, modifier;

	@FXML
	private Label errorMsg;

	@FXML
	private ComboBox<Integer> listeIDBourse;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
	
	public void switchToModifier(ActionEvent event) throws IOException {
		if (listeIDBourse.getValue() == null) {
			displayMessage(errorMsg);
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					".." + File.separator + ".." + File.separator + "view" + File.separator + "BourseModifier.fxml"));
			root = loader.load();
			
			BourseModifierController modifierController = loader.getController();
			modifierController.getDataBourse(listeIDBourse.getValue());
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
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
