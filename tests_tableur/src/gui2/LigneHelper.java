package gui2;

import gui.JLigne;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

public class LigneHelper {

	private List<Integer> largeurLigne = new ArrayList<Integer>();

	private static LigneHelper instance;
	private JTableur2 jTableur2;

	private LigneHelper(JTableur2 jTableur2) {
		this.jTableur2 = jTableur2;
		int nbLignes = jTableur2.modele.getNbLignes();
		for (int i = 0; i < nbLignes; i++) {
			largeurLigne.add(JTableur2.HAUTEUR_DEFAUT_LIGNE);
		}
	}

	public static LigneHelper getInstance(JTableur2 jTableur2) {
		if (instance == null) {
			instance = new LigneHelper(jTableur2);

		}
		return instance;
	}

	protected void supprimerComposantTitreDeLigne(final int numeroLigneMin, final int numeroLigneMax, final int indexMin) {
		final int nombreLignes = numeroLigneMax - numeroLigneMin + 1;
		for (int i = numeroLigneMin; i <= numeroLigneMax; i++) {
			JLigne ligneASupprimer = jTableur2.listeLigne.get(indexMin);
			jTableur2.listeLigne.remove(ligneASupprimer);
			jTableur2.panelGrille.remove(ligneASupprimer);
		}

		// MISE A JOUR GRAPHIQUE
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				// replace les lignes suivantes
				for (int i = indexMin; i < jTableur2.listeLigne.size(); i++) {
					JLigne jLigne = jTableur2.listeLigne.get(i);
					replacerLigne(jLigne);
					jLigne.setIndex(jLigne.getIndex() - nombreLignes);
				}
				// Rajoute des lignes à droite si besoin
				jTableur2.fillLignesBas();
				jTableur2.setValeurMaximumVerticalScrollbar();
			}
		});
	}

	private void replacerLigne(JLigne jLigne) {
		if (jLigne.getNumero() == jTableur2.numeroCelluleHaut) {
			// C'est la ligne la plus en haut, il faut la mettre à la
			// position correspondante
			positionnerLigne(jLigne, JTableur2.HAUTEUR_ENTETE_COLONNE);
		} else {
			int indexLigne = jTableur2.listeLigne.indexOf(jLigne);
			int coordonneeBasLignePrecedente = getCoordonneeBas(indexLigne - 1);
			positionnerLigne(jLigne, coordonneeBasLignePrecedente);
		}
	}

	private void positionnerLigne(JLigne jLigne, int positionHaut) {
		jLigne.setLocation(0, positionHaut);
		jTableur2.springLayout.putConstraint(SpringLayout.NORTH, jLigne, positionHaut, SpringLayout.NORTH, jTableur2);
	}

	/**
	 * récupère la position haut de la ligne en fonction de la ligne précédente
	 * 
	 * @param indexLigne
	 *            index de ligne dont on veut la position
	 */
	public int getCoordonneeHaut(int indexLigne) {
		if (indexLigne == 0) {
			// C'est la ligne en haut, on la positionne donc en fonction
			return JTableur2.HAUTEUR_ENTETE_COLONNE;
		}
		if (indexLigne <= jTableur2.listeLigne.size()) {
			JLigne jLignePrecedente = jTableur2.listeLigne.get(indexLigne - 1);
			return jLignePrecedente.getY() + jLignePrecedente.getHeight();
		}
		throw new RuntimeException("Impossible de récupérer la coordonnée haute de la ligne à l'index " + indexLigne + " car actuellement, il n'existe que " + jTableur2.listeLigne.size() + " lignes.");
	}

	/**
	 * récupère la position bas de la ligne en fonction de la ligne (position du
	 * bas de la poignée de redimmentionnement) précédente
	 * 
	 * @param indexLigne
	 *            indexLigne de ligne dont on veut la position
	 */
	public int getCoordonneeBas(int indexLigne) {
		if (indexLigne < 0) {
			return JTableur2.HAUTEUR_ENTETE_COLONNE;
		}
		if (indexLigne < jTableur2.listeLigne.size()) {
			JLigne jLigne = jTableur2.listeLigne.get(indexLigne);
			return jLigne.getY() + jLigne.getHeight();
		}
		throw new RuntimeException("Impossible de récupérer la coordonnée bas de la ligne à l'index " + indexLigne + " car actuellement, il n'existe que " + jTableur2.listeLigne.size() + " lignes.");
	}

	public void reindexerListeLigne(int indexMin) {
		for (int i = indexMin; i < jTableur2.listeLigne.size(); i++) {
			jTableur2.listeLigne.get(i).setIndex(i);
		}
	}

	public void replacerListeLigne(int indexMin) {
		for (int i = indexMin; i < jTableur2.listeLigne.size(); i++) {
			replacerLigne(jTableur2.listeLigne.get(i));
		}
	}

	public int insererComposantNumeroDeLigne(int numeroLigneMin, int numeroLigneMax, int indexMin) {

		int nombreLignesInserees = numeroLigneMax - numeroLigneMin + 1;
		for (int i = numeroLigneMin; i <= numeroLigneMax; i++) {
			// l'index represente l'index dans la liste
			int index = indexMin - numeroLigneMin + i;
			int positionHaut = getCoordonneeHaut(index);
			JLigne jLigne = new JLigne(jTableur2.modele, i, index, positionHaut, JTableur2.HAUTEUR_DEFAUT_LIGNE, jTableur2);
			jTableur2.panelGrille.add(jLigne);
			jTableur2.listeLigne.add(index, jLigne);

			replacerLigne(jLigne);

			if (getCoordonneeBas(index) >= jTableur2.getWidth()) {
				break;
			}
		}

		// décalle les lignes suivantes si il y en a

		int indexDerniereLigneInseree = indexMin + nombreLignesInserees - 1;
		int indexADecaller = indexDerniereLigneInseree + 1;

		reindexerListeLigne(indexADecaller);
		replacerListeLigne(indexADecaller);
		return indexDerniereLigneInseree;
	}
}
