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

import controller.ForAllControllers;
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

public class ChoixCandidatureVisualiserController extends ForAllControllers implements Initializable {

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

		connectToBDD();
		try {
			// Requête SQL pour obtenir les ID de candidature
			String sql = "SELECT idCandidature FROM Candidature";
			preparedStatement = conn.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			// Parcourir les résultats et ajouter les ID à la liste
			while (resultSet.next()) {
				int candidatureId = resultSet.getInt("idCandidature");
				candidatureIds.add(candidatureId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Fermer les ressources
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		closeBDD();
	}

	public void connectToBDD() {

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Smo!Aoki1305");

			stat = conn.createStatement();

		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public void closeBDD() {
		// Fermer la connexion
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}
	}

	public void switchToVisualiser(ActionEvent event) throws IOException {
		if (listIDCandidature.getValue() == null) {
			displayMessage(msgError);
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					".." + File.separator + ".." + File.separator + "view" + File.separator + "PlayerView.fxml"));
			root = loader.load();
			VisualiserCandidatureController visualiserController = loader.getController();
			visualiserController.viewCandidature(listIDCandidature.getValue());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	public void switchToHome(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + "view" + File.separator + "Home.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
