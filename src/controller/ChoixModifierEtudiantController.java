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
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ChoixModifierEtudiantController extends HomeController implements Initializable{

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

			ObservableList<Integer> listeEtudiants = FXCollections.observableArrayList();

			while (resultSet.next()) {
				int numEtudiant = resultSet.getInt("numEtudiant");
				listeEtudiants.add(numEtudiant);
			}

			// Set the drop-down menu
			comboBoxEtudiant.setCellFactory(param -> new ListCell<Integer>() {
	            @Override
	            protected void updateItem(Integer num, boolean empty) {
	                super.updateItem(num, empty);

	                if (empty || num == null) {
	                    setText(null);
	                } else {
	                    setText(num.toString());
	                }
	            }
	        });

	        comboBoxEtudiant.setItems(listeEtudiants);

	        // We set display of level once selected
	        comboBoxEtudiant.setConverter(new StringConverter<Integer>() {
	            @Override
	            public String toString(Integer num) {
	                if (num == null) {
	                    return null;
	                } else {
	                    return num.toString();
	                }
	            }

	            @Override
	            public Integer fromString(String string) {
	                return null;
	            }
	        });

			

		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}


	public void initialize(URL arg0, ResourceBundle arg1) {
	    setComboBoxWithEtudiants(listeEtudiant);
	}









}
