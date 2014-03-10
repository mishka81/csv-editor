package modele.modele2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableurModele3 {
	private List<TableurModeleStructureListener> listeTableurModeleStructureListener = new ArrayList<TableurModeleStructureListener>();
	int nbColonnes = 0;
	int nbLignes = 0;

	Map<Coordonnee, Cellule> mapCelluleParCoordonnee = new HashMap<Coordonnee, Cellule>();

	public void setValeur(int ligne, int colonne, String valeur) {
		Coordonnee coordonnee = new Coordonnee(ligne, colonne);
		if (mapCelluleParCoordonnee.get(coordonnee) != null) {
			// Il y a une cellule à cet emplacement, on met la valeur à jour
			mapCelluleParCoordonnee.get(coordonnee).setContenu(valeur);
		} else {
			Cellule cellule = new Cellule(valeur);
			mapCelluleParCoordonnee.put(coordonnee, cellule);

			if (colonne + 1 > nbColonnes) {
				nbColonnes = colonne + 1;
			}

			if (ligne + 1 > nbLignes) {
				nbLignes = ligne + 1;
			}
		}
	}

	public int getNbColonnes() {
		return nbColonnes;
	}

	public int getNbLignes() {
		return nbLignes;
	}

	public int getNbColonnesSurLigne(int numeroLigne) {
		int numeroColonne = 0;
		while (mapCelluleParCoordonnee.get(new Coordonnee(numeroLigne, numeroColonne)) != null) {
			numeroColonne++;
		}

		return numeroColonne - 1;
	}

	public String getValeur(int ligne, int colonne) {
		return getCellule(ligne, colonne).getContenu();
	}

	public Cellule getCellule(int ligne, int colonne) {
		Coordonnee coordonnee = new Coordonnee(ligne, colonne);
		return mapCelluleParCoordonnee.get(coordonnee);
	}

	public void insererColonne(int numeroColonne) {
		for (int numeroColonneADecaler = getNbColonnes(); numeroColonneADecaler >= numeroColonne; numeroColonneADecaler--) {
			for (int numeroLigne = 0; numeroLigne < getNbLignes(); numeroLigne++) {
				Cellule celluleADecaler = getCellule(numeroLigne, numeroColonneADecaler);
				if (celluleADecaler != null) {
					mapCelluleParCoordonnee.remove(celluleADecaler);
					mapCelluleParCoordonnee.put(new Coordonnee(numeroLigne, numeroColonneADecaler + 1), celluleADecaler);
				}
			}
		}
		nbColonnes++;
		for (TableurModeleStructureListener tableurModeleStructureListener : listeTableurModeleStructureListener) {
			tableurModeleStructureListener.onColonneInsered(numeroColonne);
		}

	}

	private List<Cellule> getListeCellulesColonne(int numeroColonne) {
		List<Cellule> listeCellule = new ArrayList<Cellule>();
		int numeroLigne = 0;
		while (mapCelluleParCoordonnee.get(new Coordonnee(numeroLigne, numeroColonne)) != null) {
			listeCellule.add(mapCelluleParCoordonnee.get(new Coordonnee(numeroLigne, numeroColonne)));
			numeroLigne++;
		}
		return listeCellule;
	}

	public void addModeleStructureListener(TableurModeleStructureListener tableurModeleStructureListener) {
		this.listeTableurModeleStructureListener.add(tableurModeleStructureListener);
	}

	public void supprimerColonne(int numero) {
		for (int numeroLigne = 0; numeroLigne < getNbLignes(); numeroLigne++) {
			// Supprime la cellule
			Coordonnee coordonneeCellule = new Coordonnee(numeroLigne, numero);
			Cellule cellule = mapCelluleParCoordonnee.get(coordonneeCellule);
			cellule.removeAllCelluleListener();
			mapCelluleParCoordonnee.remove(coordonneeCellule);

			// décalle toutes les cellules suivantes
			for (int numeroColonneADecaler = numero; numeroColonneADecaler < getNbColonnes(); numeroColonneADecaler++) {
				Cellule celluleADecaler = getCellule(numeroLigne, numeroColonneADecaler);
				if (celluleADecaler != null) {
					mapCelluleParCoordonnee.remove(celluleADecaler);
					mapCelluleParCoordonnee.put(new Coordonnee(numeroLigne, numeroColonneADecaler - 1), celluleADecaler);
				}
			}
		}
		nbColonnes--;

		for (TableurModeleStructureListener tableurModeleStructureListener : listeTableurModeleStructureListener) {
			tableurModeleStructureListener.onColonneRemoved(numero);
		}

	}
}
