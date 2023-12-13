package controller.bourse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BourseVisualiserController extends BourseController {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private Label bourse, destination, idEnseignant, nbPostesDisponible;

	@FXML
	private Button retour;

	public void viewBourse(int idB) {

		bourse.setText("ID Bourse : " + idB);

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/erasmus", "root",
				"Smo!Aoki1305")) {

			try (Statement stat = connection.createStatement()) {
				String sql = "SELECT * FROM Bourse WHERE idBourse = ?";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setInt(1, idB);

					try (ResultSet resultSet = statement.executeQuery()) {
						if (resultSet.next()) {
							// Récupérez les informations de la bourse
							String strDestination = resultSet.getString("destination");
							int intNbPostes = resultSet.getInt("nombrePostes");
							int intIDResponsableLocal = resultSet.getInt("responsableLocal");

							destination.setText("Destination : " + strDestination);
							nbPostesDisponible.setText("Nombre de postes disponible : " + intNbPostes);
							idEnseignant.setText("ID Responsable Local : " + intIDResponsableLocal);

						} else {
							System.out.println("Aucune bourse trouvée avec l'ID spécifié.");
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
