package controller.enseignant;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import model.DatabaseConnector;

public class ChoixModifierEnseignantController extends HomeController  implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private ComboBox<Integer> listeEnseignant;

	@FXML
	private Button modifier;

	@FXML
	private Button retour;

	Connection conn = null;
	Statement stat = null;
	
	
	public void switchToModifierEnseignant(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(
	            getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "ModifierEnseignant.fxml"));
	    root = loader.load();
	    ModifierEnseignantController modifierEnseignantController = loader.getController();

	    int selectedTeacherNumber = listeEnseignant.getValue();

	    modifierEnseignantController.initData(selectedTeacherNumber);

	    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	
	public void setComboBoxWithEnseignants(ComboBox<Integer> comboBoxEnseignant) {
		try {
			
			DatabaseConnector.connectToBDD();
			conn = DatabaseConnector.getConnection();

			stat = conn.createStatement();

			String sql = "SELECT idEnseignant FROM Enseignant";
			ResultSet resultSet = stat.executeQuery(sql);

			ObservableList<Integer> listeEnseignants = FXCollections.observableArrayList();

			while (resultSet.next()) {
				int idEnseignant = resultSet.getInt("idEnseignant");
				listeEnseignants.add(idEnseignant);
			}

			// Set the drop-down menu
			comboBoxEnseignant.setCellFactory(param -> new ListCell<Integer>() {
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

			comboBoxEnseignant.setItems(listeEnseignants);

			comboBoxEnseignant.setConverter(new StringConverter<Integer>() {
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
	
	public void switchToEnseignant(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "Enseignant.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		setComboBoxWithEnseignants(listeEnseignant);
	}

}
