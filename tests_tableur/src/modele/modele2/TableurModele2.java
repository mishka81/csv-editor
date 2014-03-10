package modele.modele2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableurModele2 {
	private List<TableurModeleStructureListener> listeTableurModeleStructureListener = new ArrayList<TableurModeleStructureListener>();
	int nbColonnes = 0;
	int nbLignes = 0;

	Map<Coordonnee, Cellule> mapCelluleParCoordonnee = new HashMap<Coordonnee, Cellule>();
	Map<Integer, List<Cellule>> mapCellulesParNumeroLigne = new HashMap<Integer, List<Cellule>>();
	Map<Integer, List<Cellule>> mapCellulesParNumeroColonne = new HashMap<Integer, List<Cellule>>();

	public void setValeur(int ligne, int colonne, String valeur) {
		Coordonnee coordonnee = new Coordonnee(ligne, colonne);
		if (mapCelluleParCoordonnee.get(coordonnee) != null) {
			// Il y a une cellule à cet emplacement, on met la valeur à jour
			mapCelluleParCoordonnee.get(coordonnee).setContenu(valeur);
		} else {
			Cellule cellule = new Cellule(valeur);
			mapCelluleParCoordonnee.put(coordonnee, cellule);

			if (mapCellulesParNumeroLigne.get(ligne) == null) {
				mapCellulesParNumeroLigne.put(ligne, new ArrayList<Cellule>());
			}
			mapCellulesParNumeroLigne.get(mapCellulesParNumeroLigne).add(colonne, cellule);

			if (mapCellulesParNumeroColonne.get(colonne) == null) {
				mapCellulesParNumeroColonne.put(colonne, new ArrayList<Cellule>());
			}
			mapCellulesParNumeroColonne.get(mapCellulesParNumeroColonne).add(ligne, cellule);

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
		if (numeroLigne >= mapCellulesParNumeroLigne.size()) {
			return 0;
		}
		return mapCellulesParNumeroLigne.size();
	}

	public String getValeur(int ligne, int colonne) {
		return getCellule(ligne, colonne).getContenu();
	}

	public Cellule getCellule(int ligne, int colonne) {
		Coordonnee coordonnee = new Coordonnee(ligne, colonne);
		return mapCelluleParCoordonnee.get(coordonnee);
	}

	// public void insererColonne(int numero) {
	// for (int numeroColonneADecaler = getNbColonnes(); numeroColonneADecaler
	// >= numero;numeroColonneADecaler--) {
	// List<Cellule> cellulesColonneADecaler =
	// mapCellulesParNumeroColonne.get(numeroColonneADecaler);
	// for (int
	// numeroLigne=0;numeroLigne<cellulesColonneADecaler.size();numeroLigne++) {
	// setValeur(numeroLigne, colonne, valeur);
	// }
	// for (Cellule cellule : cellulesColonneADecaler) {
	// cellule
	// }
	//
	// }
	//
	// Colonne colonne = new Colonne(numero);
	//
	// // décale les colonnes suivates
	// int numeroColonneADecaler = numero;
	// while (numeroColonneADecaler < listeColonne.size()) {
	// Colonne colonneADecaler = listeColonne.get(numeroColonneADecaler);
	// colonneADecaler.setNumero(numeroColonneADecaler + 1);
	// colonneADecaler.setNom(String.valueOf(colonneADecaler.getNumero()));
	// numeroColonneADecaler++;
	// }
	// // décale la cellule sur toutes les lignes et insère la nouvelle cellule
	// for (Ligne ligne : listeLigne) {
	// ligne.insertNewCellule(modele, colonne);
	// }
	// for (TableurModeleStructureListener listener :
	// listeTableurModeleStructureListener) {
	// listener.onColonneInsered(colonne);
	// }
	// listeColonne.add(numero, colonne);
	// }
	//
	// public void addModeleStructureListener(TableurModeleStructureListener
	// tableurModeleStructureListener) {
	// this.listeTableurModeleStructureListener.add(tableurModeleStructureListener);
	// }
	//
	// public void supprimerColonne(TableurModele modele, int numero) {
	// // Supprime l'entete de colonne
	// modele.listeColonne.remove(numero);
	//
	// //supprime les cellules correspondantes
	// for (Ligne ligne : listeLigne) {
	// ligne.getCelluleInColonne(numeroColonne)(modele, colonne);
	// }
	// }
}
