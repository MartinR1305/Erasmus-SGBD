package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ChoixModifierEtudiantController extends HomeController{

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private ComboBox<Integer> listeEtudiant;

	@FXML
	private Button modifier;

	@FXML
	private Button retour;

	Connection conn = null;
	Statement stat = null;


	public void switchToModifierEtudiant(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + "view" + File.separator + "ModifierEtudiant.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}


	public void setComboBoxWithEtudiants(ComboBox<Integer> comboBoxEtudiant) {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942");
			stat = conn.createStatement();

			String sql = "SELECT numEtudiant FROM Etudiant";
			ResultSet resultSet = stat.executeQuery(sql);

			while (resultSet.next()) {
				int numEtudiant = resultSet.getInt("numEtudiant");
				comboBoxEtudiant.getItems().add(numEtudiant);
			}

		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}








}
