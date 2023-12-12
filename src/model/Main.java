package model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	Connection conn = null;
	Statement stat = null;

	public Main() throws IOException {
		connectToBDD();
//		createBDD();
//		createData();
		closeBDD();
		System.out.println("ok");
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// Here we start the application
			Parent root = FXMLLoader
					.load(getClass().getResource(".." + File.separator + "view" + File.separator + "Home.fxml"));
			Scene scene1 = new Scene(root);
			primaryStage.setScene(scene1);
			primaryStage.centerOnScreen();
			primaryStage.show();

			// Set on center the stage
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
			primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void connectToBDD() {

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root", "Babalou1942");

			stat = conn.createStatement();

		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void createBDD() {

		try {

			// Création des tables

			String etudiant = "CREATE TABLE Etudiant (" + "numEtudiant INTEGER not NULL AUTO_INCREMENT," + "nomEtudiant VARCHAR(255),"
					+ "prenomEtudiant VARCHAR(255)," + "noteMoyenne DOUBLE," + "PRIMARY KEY (numEtudiant))";

			stat.executeUpdate(etudiant);

			String enseignant = "CREATE TABLE Enseignant (" + "idEnseignant INTEGER not NULL AUTO_INCREMENT,"
					+ "nomEnseignant VARCHAR(255)," + "prenomEnseignant VARCHAR(255)," + "PRIMARY KEY (idEnseignant))";

			stat.executeUpdate(enseignant);

			String candidature = "CREATE TABLE Candidature (" + "idCandidature INTEGER not NULL AUTO_INCREMENT,"
					+ "score DOUBLE," + "noteResponsableErasmus DOUBLE," + "noteResponsableLocal DOUBLE,"
					+ "etudiant INTEGER NOT NULL," + "responsableErasmus INTEGER NOT NULL,"
					+ "PRIMARY KEY (idCandidature)," + "FOREIGN KEY (etudiant) REFERENCES Etudiant(numEtudiant),"
					+ "FOREIGN KEY (responsableErasmus) REFERENCES Enseignant(idEnseignant))";

			stat.executeUpdate(candidature);

			String bourse = "CREATE TABLE Bourse (" + "idBourse INTEGER not NULL AUTO_INCREMENT," + "destination VARCHAR(255),"
					+ "nombrePostes INTEGER," + "responsableLocal INTEGER NOT NULL," + "candidature INTEGER NOT NULL,"
					+ "PRIMARY KEY (idBourse)," + "FOREIGN KEY (candidature) REFERENCES Candidature(idCandidature),"
					+ "FOREIGN KEY (responsableLocal) REFERENCES Enseignant(idEnseignant))";

			stat.executeUpdate(bourse);

			String enseignement = "CREATE TABLE Enseignement (" + "idEnseignement INTEGER not NULL AUTO_INCREMENT,"
					+ "nomEnseignement VARCHAR(255)," + "nombreCredit INTEGER," + "volumeHoraire INTEGER,"
					+ "candidature INTEGER NOT NULL,"
					+ "FOREIGN KEY (candidature) REFERENCES Candidature(idCandidature),"
					+ "PRIMARY KEY (idEnseignement))";

			stat.executeUpdate(enseignement);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void createData() {
		try {

			// Données pour la table Etudiant
			stat.executeUpdate(
					"INSERT INTO Etudiant (numEtudiant, nomEtudiant, prenomEtudiant, noteMoyenne) VALUES (1, 'Recher', 'Martin', 18.5)");
			stat.executeUpdate(
					"INSERT INTO Etudiant (numEtudiant, nomEtudiant, prenomEtudiant, noteMoyenne) VALUES (2, 'Filali', 'Saad', 20.0)");
			stat.executeUpdate(
					"INSERT INTO Etudiant (numEtudiant, nomEtudiant, prenomEtudiant, noteMoyenne) VALUES (3, 'Chakori', 'Alaa', 19.0)");
			stat.executeUpdate(
					"INSERT INTO Etudiant (numEtudiant, nomEtudiant, prenomEtudiant, noteMoyenne) VALUES (4, 'Son', 'Gohan', 16.0)");

			// Données pour la table Enseignant
			stat.executeUpdate(
					"INSERT INTO Enseignant (idEnseignant, nomEnseignant, prenomEnseignant) VALUES (1, 'Monkey D', 'Luffy')");
			stat.executeUpdate(
					"INSERT INTO Enseignant (idEnseignant, nomEnseignant, prenomEnseignant) VALUES (2, 'Uzumaki', 'Naruto')");
			stat.executeUpdate(
					"INSERT INTO Enseignant (idEnseignant, nomEnseignant, prenomEnseignant) VALUES (3, 'Uchiha', 'Sasuke')");
			stat.executeUpdate(
					"INSERT INTO Enseignant (idEnseignant, nomEnseignant, prenomEnseignant) VALUES (4, 'Roronoa', 'Zoro')");

			// Données pour la table Candidature
			stat.executeUpdate(
					"INSERT INTO Candidature (score, noteResponsableErasmus, noteResponsableLocal, etudiant, responsableErasmus) VALUES (89.0, 90.0, 88.0, 1, 1)");
			stat.executeUpdate(
					"INSERT INTO Candidature (score, noteResponsableErasmus, noteResponsableLocal, etudiant, responsableErasmus) VALUES (85.0, 92.0, 87.0, 2, 2)");
			stat.executeUpdate(
					"INSERT INTO Candidature (score, noteResponsableErasmus, noteResponsableLocal, etudiant, responsableErasmus) VALUES (78.0, 85.0, 80.0, 3, 3)");
			stat.executeUpdate(
					"INSERT INTO Candidature (score, noteResponsableErasmus, noteResponsableLocal, etudiant, responsableErasmus) VALUES (92.0, 88.0, 95.0, 4, 4)");

			// Données pour la table Bourse
			stat.executeUpdate(
					"INSERT INTO Bourse (idBourse, destination, nombrePostes, responsableLocal, candidature) VALUES (1, 'Canada', 5, 1, 1)");
			stat.executeUpdate(
					"INSERT INTO Bourse (idBourse, destination, nombrePostes, responsableLocal, candidature) VALUES (2, 'Maroc', 3, 2, 1)");
			stat.executeUpdate(
					"INSERT INTO Bourse (idBourse, destination, nombrePostes, responsableLocal, candidature) VALUES (3, 'Konoha', 6, 3, 3)");
			stat.executeUpdate(
					"INSERT INTO Bourse (idBourse, destination, nombrePostes, responsableLocal, candidature) VALUES (4, 'Marineford', 3, 4, 4)");

			// Données pour la table Enseignement
			stat.executeUpdate(
					"INSERT INTO Enseignement (idEnseignement, nomEnseignement, nombreCredit, volumeHoraire, candidature) VALUES (1, 'Ninjutsu', 5, 60, 1)");
			stat.executeUpdate(
					"INSERT INTO Enseignement (idEnseignement, nomEnseignement, nombreCredit, volumeHoraire, candidature) VALUES (2, 'Haki', 4, 50, 2)");
			stat.executeUpdate(
					"INSERT INTO Enseignement (idEnseignement, nomEnseignement, nombreCredit, volumeHoraire, candidature) VALUES (3, 'Senjutsu', 4, 50, 3)");
			stat.executeUpdate(
					"INSERT INTO Enseignement (idEnseignement, nomEnseignement, nombreCredit, volumeHoraire, candidature) VALUES (4, 'Maitrise des Sabres', 4, 50, 4)");

		} catch (SQLException e) {
			e.printStackTrace();
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
}
