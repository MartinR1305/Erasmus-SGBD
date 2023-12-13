package controller.enseignement;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import controller.HomeController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class VisualiserEnseignementController extends HomeController {

	Connection conn = null;
	Statement stat = null;

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private TableColumn<List<String>, Integer> credit, volume;

	@FXML
	private TableColumn<List<String>, String> nom;

	@FXML
	private Button retour;

	@FXML
	private TableView<List<String>> tableau;
	
	
	public void visualiserEnseignements() {
	    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942")) {
	        String sql = "SELECT nomEnseignement, nombreCredit, volumeHoraire FROM Enseignement";
	        try (PreparedStatement statement = conn.prepareStatement(sql)) {
	            ResultSet resultSet = statement.executeQuery();

	            ObservableList<List<String>> listeEnseignements = FXCollections.observableArrayList();

	            while (resultSet.next()) {
	                String nomEnseignement = resultSet.getString("nomEnseignement");
	                int nombreCredit = resultSet.getInt("nombreCredit");
	                int volumeHoraire = resultSet.getInt("volumeHoraire");

	                // Création d'une liste avec les données récupérées
	                List<String> enseignementData = new ArrayList<>();
	                enseignementData.add(nomEnseignement);
	                enseignementData.add(String.valueOf(nombreCredit));
	                enseignementData.add(String.valueOf(volumeHoraire));

	                listeEnseignements.add(enseignementData);
	            }

	            // Afficher les données dans la TableView
	            tableau.setItems(listeEnseignements);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
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
	
	public void initialize() {
	    // Initialize columns with data
	    nom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
	    credit.setCellValueFactory(cellData -> new SimpleObjectProperty<>(Integer.parseInt(cellData.getValue().get(1))));
	    volume.setCellValueFactory(cellData -> new SimpleObjectProperty<>(Integer.parseInt(cellData.getValue().get(2))));

	    // Call method to set data to table view
	    visualiserEnseignements();
	}


}
