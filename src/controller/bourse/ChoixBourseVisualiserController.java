package controller.bourse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import model.DatabaseConnector;

public class ChoixBourseVisualiserController extends HomeController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Label errorMsg;

	@FXML
	private ComboBox<Integer> listeIDBourse;

	@FXML
	private Button retour, visualiser;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Integer> listIDB = FXCollections.observableArrayList();
		
		try {
			DatabaseConnector.connectToBDD();
			Connection connection = DatabaseConnector.getConnection();
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

	public void switchToBourse(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				".." + File.separator + ".." + File.separator + "view" + File.separator + "Bourse.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToVisualiser(ActionEvent event) throws IOException {
		if (listeIDBourse.getValue() == null) {
			displayMessage(errorMsg);
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					".." + File.separator + ".." + File.separator + "view" + File.separator + "BourseVisualiser.fxml"));
			root = loader.load();
			
			BourseVisualiserController visualiserController = loader.getController();
			try {
				visualiserController.viewBourse(listeIDBourse.getValue());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
}
