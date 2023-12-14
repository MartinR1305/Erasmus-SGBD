package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
	
	private static Connection conn = null;
	private static Statement stat = null;
	
	// Constantes pour les informations de connexion
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/erasmus";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Babalou1942";
	
	public static void connectToBDD() {
		try {
            conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
			stat = conn.createStatement();
		} catch (SQLException ex) {
			// Gérer les erreurs
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	// Ajoutez cette méthode pour obtenir la connexion
    public static Connection getConnection() {
        return conn;
    }
	

	public static void createBDD() {

		try {

			// Création des tables

			String etudiant = "CREATE TABLE Etudiant (" + "numEtudiant INTEGER not NULL AUTO_INCREMENT," + "nomEtudiant VARCHAR(255),"
					+ "prenomEtudiant VARCHAR(255)," + "noteMoyenne DOUBLE," + "PRIMARY KEY (numEtudiant))";

			stat.executeUpdate(etudiant);

			String enseignant = "CREATE TABLE Enseignant (" + "idEnseignant INTEGER not NULL AUTO_INCREMENT,"
					+ "nomEnseignant VARCHAR(255)," + "prenomEnseignant VARCHAR(255)," + "PRIMARY KEY (idEnseignant))";

			stat.executeUpdate(enseignant);

			String bourse = "CREATE TABLE Bourse (" + "idBourse INTEGER not NULL AUTO_INCREMENT," + "destination VARCHAR(255),"
					+ "nombrePostes INTEGER," + "responsableLocal INTEGER NOT NULL,"
					+ "PRIMARY KEY (idBourse),"
					+ "FOREIGN KEY (responsableLocal) REFERENCES Enseignant(idEnseignant))";

			stat.executeUpdate(bourse);

			String enseignement = "CREATE TABLE Enseignement (" + "idEnseignement INTEGER not NULL AUTO_INCREMENT,"
					+ "nomEnseignement VARCHAR(255)," + "nombreCredit INTEGER," + "volumeHoraire INTEGER,"
					+ "PRIMARY KEY (idEnseignement))";

			stat.executeUpdate(enseignement);

			String candidature = "CREATE TABLE Candidature (" +
					"idCandidature INT PRIMARY KEY AUTO_INCREMENT," +
					"score INT," +
					"noteResponsableErasmus INT," +
					"noteResponsableLocal INT," +
					"idEtudiant INT," +
					"responsableErasmus INT," +
					"idBourse1 INT," +
					"idBourse2 INT," +
					"idEnseignement1 INT," +
					"idEnseignement2 INT," +
					"idEnseignement3 INT," +
					"FOREIGN KEY (idEtudiant) REFERENCES Etudiant(numEtudiant)," +
					"FOREIGN KEY (responsableErasmus) REFERENCES Enseignant(idEnseignant)," +
					"FOREIGN KEY (idEnseignement1) REFERENCES Enseignement(idEnseignement)," +
					"FOREIGN KEY (idEnseignement2) REFERENCES Enseignement(idEnseignement)," +
					"FOREIGN KEY (idEnseignement3) REFERENCES Enseignement(idEnseignement)," +
					"FOREIGN KEY (idBourse1) REFERENCES Bourse(idBourse)," +
					"FOREIGN KEY (idBourse2) REFERENCES Bourse(idBourse));";

			stat.executeUpdate(candidature);



		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void createData() {
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

			// Données pour la table Bourse
			stat.executeUpdate(
					"INSERT INTO Bourse (idBourse, destination, nombrePostes, responsableLocal) VALUES (1, 'Canada', 5, 1)");
			stat.executeUpdate(
					"INSERT INTO Bourse (idBourse, destination, nombrePostes, responsableLocal) VALUES (2, 'Maroc', 3, 2)");
			stat.executeUpdate(
					"INSERT INTO Bourse (idBourse, destination, nombrePostes, responsableLocal) VALUES (3, 'Konoha', 6, 3)");
			stat.executeUpdate(
					"INSERT INTO Bourse (idBourse, destination, nombrePostes, responsableLocal) VALUES (4, 'Marineford', 3, 4)");

			// Données pour la table Enseignement
			stat.executeUpdate(
					"INSERT INTO Enseignement (idEnseignement, nomEnseignement, nombreCredit, volumeHoraire) VALUES (1, 'Ninjutsu', 5, 60)");
			stat.executeUpdate(
					"INSERT INTO Enseignement (idEnseignement, nomEnseignement, nombreCredit, volumeHoraire) VALUES (2, 'Haki', 4, 50)");
			stat.executeUpdate(
					"INSERT INTO Enseignement (idEnseignement, nomEnseignement, nombreCredit, volumeHoraire) VALUES (3, 'Senjutsu', 4, 50)");
			stat.executeUpdate(
					"INSERT INTO Enseignement (idEnseignement, nomEnseignement, nombreCredit, volumeHoraire) VALUES (4, 'Maitrise des Sabres', 4, 50)");


			// Données pour la table Candidature
			stat.executeUpdate(
					"INSERT INTO Candidature (score, noteResponsableErasmus, noteResponsableLocal, idEtudiant, responsableErasmus, idBourse1, idBourse2, idEnseignement1, idEnseignement2, idEnseignement3) VALUES (89.0, 90.0, 88.0, 1, 1, 1, NULL, 1, 2, 3)");
			stat.executeUpdate(
					"INSERT INTO Candidature (score, noteResponsableErasmus, noteResponsableLocal, idEtudiant, responsableErasmus, idBourse1, idBourse2, idEnseignement1, idEnseignement2, idEnseignement3 ) VALUES (85.0, 92.0, 87.0, 2, 2, 2, 3, 2, 3, 4)");
			stat.executeUpdate(
					"INSERT INTO Candidature (score, noteResponsableErasmus, noteResponsableLocal, idEtudiant, responsableErasmus, idBourse1, idBourse2, idEnseignement1, idEnseignement2, idEnseignement3 ) VALUES (78.0, 85.0, 80.0, 3, 3, 1, 4, 1, 3, 4)");
			stat.executeUpdate(
					"INSERT INTO Candidature (score, noteResponsableErasmus, noteResponsableLocal, idEtudiant, responsableErasmus, idBourse1, idBourse2, idEnseignement1, idEnseignement2, idEnseignement3 ) VALUES (92.0, 88.0, 95.0, 4, 4, 2, 3, 2, 3, 4)");



		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void closeBDD() {
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
