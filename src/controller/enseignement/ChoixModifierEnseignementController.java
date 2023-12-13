package controller.enseignement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ChoixModifierEnseignementController extends HomeController  implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private ComboBox<Integer> listeEnseignement;

	@FXML
	private Button modifier;

	@FXML
	private Button retour;

	Connection conn = null;
	Statement stat = null;
	
	public void switchToModifierEnseignement(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(
	            getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "ModifierEnseignement.fxml"));
	    root = loader.load();
	    ModifierEnseignementController modifierEnseignementController = loader.getController();

	    int selectedTeacherNumber = listeEnseignement.getValue();

	    modifierEnseignementController.initData(selectedTeacherNumber);

	    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	
	
	public void setComboBoxWithEnseignements(ComboBox<Integer> comboBoxEnseignement) {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942");
			stat = conn.createStatement();

			String sql = "SELECT idEnseignement FROM Enseignement";
			ResultSet resultSet = stat.executeQuery(sql);

			ObservableList<Integer> listeEnseignements = FXCollections.observableArrayList();

			while (resultSet.next()) {
				int idEnseignement = resultSet.getInt("idEnseignement");
				listeEnseignements.add(idEnseignement);
			}

			// Set the drop-down menu
			comboBoxEnseignement.setCellFactory(param -> new ListCell<Integer>() {
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

			comboBoxEnseignement.setItems(listeEnseignements);

			comboBoxEnseignement.setConverter(new StringConverter<Integer>() {
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
	
	
	public void switchToEnseignement(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "Enseignement.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		setComboBoxWithEnseignements(listeEnseignement);
	}

}
