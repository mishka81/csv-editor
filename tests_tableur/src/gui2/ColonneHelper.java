package gui2;

import gui.JColonne;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

public class ColonneHelper {

	private List<Integer> largeurColonne = new ArrayList<Integer>();

	private static ColonneHelper instance;
	private JTableur2 jTableur2;

	private ColonneHelper(JTableur2 jTableur2) {
		this.jTableur2 = jTableur2;
		int nbColonnes = jTableur2.modele.getNbColonnes();
		for (int i = 0; i < nbColonnes; i++) {
			largeurColonne.add(JTableur2.LARGEUR_DEFAUT_COLONNE);
		}
	}

	public static ColonneHelper getInstance(JTableur2 jTableur2) {
		if (instance == null) {
			instance = new ColonneHelper(jTableur2);

		}
		return instance;
	}

	protected void supprimerComposantTitreDeColonne(final int numeroColonneMin, final int numeroColonneMax, final int indexMin) {
		final int nombreColonnes = numeroColonneMax - numeroColonneMin + 1;
		for (int i = numeroColonneMin; i <= numeroColonneMax; i++) {
			JColonne colonneASupprimer = jTableur2.listeColonne.get(indexMin);
			jTableur2.listeColonne.remove(colonneASupprimer);
			jTableur2.panelGrille.remove(colonneASupprimer);
		}

		// MISE A JOUR GRAPHIQUE
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				// replace les colonnes suivantes
				for (int i = indexMin; i < jTableur2.listeColonne.size(); i++) {
					JColonne jColonne = jTableur2.listeColonne.get(i);
					replacerColonne(jColonne);
					jColonne.setIndex(jColonne.getIndex() - nombreColonnes);
				}
				// Rajoute des colonnes à droite si besoin
				jTableur2.fillColonnesDroite();
				jTableur2.setValeurMaximumHorizontalScrollbar();
			}
		});
	}

	private void replacerColonne(JColonne jColonne) {
		if (jColonne.getNumero() == jTableur2.numeroCelluleGauche) {
			// C'est la colonne la plus à gauche, il faut la mettre à la
			// position correspondante
			positionnerColonne(jColonne, JTableur2.LARGEUR_NUMERO_LIGNE);
		} else {
			int indexColonne = jTableur2.listeColonne.indexOf(jColonne);
			int coordonneeDroiteColonnePrecedente = getCoordonneeDroite(indexColonne - 1);
			positionnerColonne(jColonne, coordonneeDroiteColonnePrecedente);
		}
	}

	private void positionnerColonne(JColonne jColonne, int positionGauche) {
		jColonne.setLocation(positionGauche, 0);
		jTableur2.springLayout.putConstraint(SpringLayout.WEST, jColonne, positionGauche, SpringLayout.WEST, jTableur2);
	}

	/**
	 * récupère la position gauche de la colonne en fonction de la colonne
	 * précédente
	 * 
	 * @param numeroColonneMin
	 *            index de colonne dont on veut la position
	 */
	public int getCoordonneeGauche(int indexColonne) {
		if (indexColonne == 0) {
			// C'est la colonne de gauche, on la positionne donc en fonction
			return JTableur2.LARGEUR_NUMERO_LIGNE;
		}
		if (indexColonne <= jTableur2.listeColonne.size()) {
			JColonne jColonnePrecedente = jTableur2.listeColonne.get(indexColonne - 1);
			return jColonnePrecedente.getX() + jColonnePrecedente.getWidth();
		}
		throw new RuntimeException("Impossible de récupérer la coordonnée gauche de la colonne à l'index " + indexColonne + " car actuellement, il n'existe que " + jTableur2.listeColonne.size() + " colonnes.");
	}

	/**
	 * récupère la position droite de la colonne en fonction de la colonne
	 * (position de la droite de la poignée de redimmentionnement) précédente
	 * 
	 * @param indexColonne
	 *            indexColonne de colonne dont on veut la position
	 */
	public int getCoordonneeDroite(int indexColonne) {
		if (indexColonne < 0) {
			return JTableur2.LARGEUR_NUMERO_LIGNE;
		}
		if (indexColonne < jTableur2.listeColonne.size()) {
			JColonne jColonne = jTableur2.listeColonne.get(indexColonne);
			return jColonne.getX() + jColonne.getWidth();
		}
		throw new RuntimeException("Impossible de récupérer la coordonnée droite de la colonne à l'index " + indexColonne + " car actuellement, il n'existe que " + jTableur2.listeColonne.size() + " colonnes.");
	}

	public void reindexerListeColonne(int indexMin) {
		for (int i = indexMin; i < jTableur2.listeColonne.size(); i++) {
			jTableur2.listeColonne.get(i).setIndex(i);
		}
	}

	public void replacerListeColonne(int indexMin) {
		for (int i = indexMin; i < jTableur2.listeColonne.size(); i++) {
			replacerColonne(jTableur2.listeColonne.get(i));
		}
	}

	public int insererComposantTitreDeColonne(int numeroColonneMin, int numeroColonneMax, int indexMin) {

		int nombreColonnesInserees = numeroColonneMax - numeroColonneMin + 1;
		for (int i = numeroColonneMin; i <= numeroColonneMax; i++) {
			// l'index represente l'index dans la liste
			int index = indexMin - numeroColonneMin + i;
			int positionGauche = getCoordonneeGauche(index);
			JColonne jColonne = new JColonne(jTableur2.modele, i, index, positionGauche, JTableur2.LARGEUR_DEFAUT_COLONNE, jTableur2);
			jTableur2.panelGrille.add(jColonne);
			jTableur2.listeColonne.add(index, jColonne);

			replacerColonne(jColonne);

			CelluleHelper.getInstance(jTableur2).genererCellule(0, jTableur2.listeLigne.size() - 1, index, index);

			if (getCoordonneeDroite(index) >= jTableur2.getWidth()) {
				break;
			}
		}

		// décalle les colonnes suivantes si il y en a

		int indexDerniereColonneInseree = indexMin + nombreColonnesInserees - 1;
		int indexADecaller = indexDerniereColonneInseree + 1;

		reindexerListeColonne(indexADecaller);
		replacerListeColonne(indexADecaller);
		return indexDerniereColonneInseree;
	}
}
