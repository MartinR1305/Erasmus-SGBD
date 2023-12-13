package controller.etudiant;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
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
import model.DatabaseConnector;

public class VisualiserEtudiantController extends HomeController{

	@FXML
    private TableColumn<List<String>, String> nom, prenom;

    @FXML
    private TableColumn<List<String>, Integer> numero;

    @FXML
    private TableColumn<List<String>, Double> note;

    @FXML
    private TableView<List<String>> tableau;

	@FXML
	private Button retour;
	
	Connection conn = null;
	Statement stat = null;
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	public void visualiserEtudiants() {
	    try  {
	    	DatabaseConnector.connectToBDD();
	    	conn = DatabaseConnector.getConnection();
	    	
	        String sql = "SELECT nomEtudiant, numEtudiant, prenomEtudiant, noteMoyenne FROM Etudiant";
	        try (PreparedStatement statement = conn.prepareStatement(sql)) {
	            ResultSet resultSet = statement.executeQuery();

	            ObservableList<List<String>> listeEtudiants = FXCollections.observableArrayList();

	            while (resultSet.next()) {
	                String nomEtudiant = resultSet.getString("nomEtudiant");
	                int numEtudiant = resultSet.getInt("numEtudiant");
	                String prenomEtudiant = resultSet.getString("prenomEtudiant");
	                double noteMoyenne = resultSet.getDouble("noteMoyenne");

	                // Création d'une liste avec les données récupérées
	                List<String> etudiantData = new ArrayList<>();
	                etudiantData.add(nomEtudiant);
	                etudiantData.add(String.valueOf(numEtudiant));
	                etudiantData.add(prenomEtudiant);
	                etudiantData.add(String.valueOf(noteMoyenne));

	                listeEtudiants.add(etudiantData);
	            }

	            // Afficher les données dans la TableView
	            tableau.setItems(listeEtudiants);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	
	public void switchToEtudiant(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(".." + File.separator + ".." + File.separator + "view" + File.separator + "Etudiant.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}


	public void initialize() {
	    // Initialize columns with data
	    nom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
	    numero.setCellValueFactory(cellData -> new SimpleObjectProperty<>(Integer.parseInt(cellData.getValue().get(1))));
	    prenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
	    note.setCellValueFactory(cellData -> new SimpleObjectProperty<>(Double.parseDouble(cellData.getValue().get(3))));

	    // Call method to set data to table view
	    visualiserEtudiants();
	}


	


}
