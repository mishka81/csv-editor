package gui2;

import gui.JCellule;
import gui.JColonne;
import gui.JLigne;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SpringLayout;

public class CelluleHelper {

	private List<Integer> largeurLigne = new ArrayList<Integer>();

	private static CelluleHelper instance;
	private JTableur2 jTableur2;

	private CelluleHelper(JTableur2 jTableur2) {
		this.jTableur2 = jTableur2;
	}

	public static CelluleHelper getInstance(JTableur2 jTableur2) {
		if (instance == null) {
			instance = new CelluleHelper(jTableur2);

		}
		return instance;
	}

	public void genererCellule(final int indexLigneMin, final int indexLigneMax, final int indexColonneMin, final int indexColonneMax) {
		// SwingUtilities.invokeLater(new Runnable() {

		// @Override
		// public void run() {
		int nbCellulesGenerees = 0;
		boucleLigne: for (int indexLigne = indexLigneMin; indexLigne <= indexLigneMax; indexLigne++) {
			boucleColonne: for (int indexColonne = indexColonneMin; indexColonne <= indexColonneMax; indexColonne++) {
				JColonne colonne = jTableur2.listeColonne.get(indexColonne);
				JLigne ligne = jTableur2.listeLigne.get(indexLigne);
				JCellule jCellule = new JCellule(jTableur2, colonne, ligne, jTableur2.modele.getCellule(ligne.getNumero(), colonne.getNumero()), jTableur2.modele);
				jCellule.setBounds(colonne.getX(), ligne.getY(), colonne.getWidth(), ligne.getHeight());
				jTableur2.panelGrille.add(jCellule);

				ligne.addCellule(jCellule);
				colonne.addCellule(jCellule);
				jTableur2.springLayout.putConstraint(SpringLayout.NORTH, jCellule, ligne.getY(), SpringLayout.NORTH, jTableur2);
				jTableur2.springLayout.putConstraint(SpringLayout.WEST, jCellule, colonne.getX(), SpringLayout.WEST, jTableur2);

				nbCellulesGenerees++;

			}
		}
		System.out.println(nbCellulesGenerees + " cellules générées");
		// }
		// });

	}
}
