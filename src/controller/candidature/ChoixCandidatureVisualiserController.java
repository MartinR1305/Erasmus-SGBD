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
import java.util.ArrayList;
import java.util.List;
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

public class ChoixCandidatureVisualiserController extends CandidatureController implements Initializable {

	private Stage stage;
	private Scene scene;
	private Parent root;

	Connection conn = null;
	Statement stat = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	List<Integer> candidatureIds = new ArrayList<>();

	@FXML
	private ComboBox<Integer> listIDCandidature;

	@FXML
	private Button retour, voir;

	@FXML
	private Label msgError;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Integer> listIDCand = FXCollections.observableArrayList();
		
		// Recherche des IDs dans la BDD
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
				"Smo!Aoki1305")) {
			String sqlCandidature = "SELECT idCandidature FROM Candidature";

			// ID Bourse
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
		
		setComboBoxWithListID(listIDCandidature, listIDCand);
	}

	public void switchToVisualiser(ActionEvent event) throws IOException {
		if (listIDCandidature.getValue() == null) {
			displayMessage(msgError);
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					".." + File.separator + ".." + File.separator + "view" + File.separator + "VisualiserCandidature.fxml"));
			root = loader.load();
			
			VisualiserCandidatureController visualiserController = loader.getController();
			visualiserController.viewCandidature(listIDCandidature.getValue());
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
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
