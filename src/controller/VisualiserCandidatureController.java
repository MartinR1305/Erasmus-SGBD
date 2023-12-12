package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class VisualiserCandidatureController extends ForAllControllers{
	@FXML
	private Label idBourse1, idBourse2, idCandidature, idEnseignement1, idEnseignement2, idEnseignement3,
			idRespoErasmus, idRespoLocal, note1, note2, numEtudiant, score;

	@FXML
	private Button retour;
	
	private int intIDCandidature;
	
	public void viewCandidature(int idCandidature) {
		intIDCandidature = idCandidature;
		System.out.println(intIDCandidature);
	}
}