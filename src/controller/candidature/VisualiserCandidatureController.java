package controller.candidature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class VisualiserCandidatureController extends CandidatureController {
	@FXML
	private Label idBourse1, idBourse2, idCandidature, idEnseignement1, idEnseignement2, idEnseignement3,
			idRespoErasmus, note1, note2, numEtudiant, score;

	@FXML
	private Button retour;

	private int intIDCandidature;

	public void viewCandidature(int idC) {
		intIDCandidature = idC;
		idCandidature.setText("ID Candidature : " + intIDCandidature);

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
				"Smo!Aoki1305")) {

			try (Statement stat = connection.createStatement()) {
				String sql = "SELECT * FROM Candidature WHERE idCandidature = ?";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setInt(1, intIDCandidature);

					try (ResultSet resultSet = statement.executeQuery()) {
						if (resultSet.next()) {
							// Récupérez les informations de la candidature
							int intScore = resultSet.getInt("score");
							int intNoteResponsableErasmus = resultSet.getInt("noteResponsableErasmus");
							int intNoteResponsableLocal = resultSet.getInt("noteResponsableLocal");
							int intIDEtudiant = resultSet.getInt("idEtudiant");
							int intIDResponsableErasmus = resultSet.getInt("responsableErasmus");
							int intIDBourse1 = resultSet.getInt("idBourse1");
							int intIDBourse2 = resultSet.getInt("idBourse2");
							int intIDEnseignement1 = resultSet.getInt("idEnseignement1");
							int intIDEnseignement2 = resultSet.getInt("idEnseignement2");
							int intIDEnseignement3 = resultSet.getInt("idEnseignement3");

							idBourse1.setText("ID Bourse 1 : " + intIDBourse1);
							if (intIDBourse2 == 0) {
								idBourse2.setText("ID Bourse 2 : Aucune");
							} else {
								idBourse2.setText("ID Bourse 2 : " + intIDBourse2);
							}
							idEnseignement1.setText("ID Enseignement 1 : " + intIDEnseignement1);
							idEnseignement2.setText("ID Enseignement 2 : " + intIDEnseignement2);
							idEnseignement3.setText("ID Enseignement 3 : " + intIDEnseignement3);
							idRespoErasmus.setText("ID Responsable Erasmus : " + intIDResponsableErasmus);
							score.setText("Score : " + intScore);
							note1.setText("Note Responsable Erasmus : " + intNoteResponsableErasmus);
							note2.setText("Note Responsable Local : " + intNoteResponsableLocal);
							numEtudiant.setText("Numéro Étudiant : " + intIDEtudiant);
						} else {
							System.out.println("Aucune candidature trouvée avec l'ID spécifié.");
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}