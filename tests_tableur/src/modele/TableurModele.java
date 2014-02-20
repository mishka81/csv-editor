package modele;

import java.util.ArrayList;
import java.util.List;

public class TableurModele {
	private List<Ligne> listeLigne = new ArrayList<Ligne>();
	private List<Colonne> listeColonne = new ArrayList<Colonne>();
	
	int nbColonnes = 0;
	int nbLignes = 0;
	private List<TableurModeleStructureListener> listeTableurModeleStructureListener = new ArrayList<TableurModeleStructureListener>();
	
	public void setValeur (int ligne, int colonne, String valeur) {
		while (ligne >= listeLigne.size()) {
			listeLigne.add(new Ligne(listeLigne.size()));
		}
		while (colonne >= listeColonne.size()) {
			listeColonne.add(new Colonne(listeColonne.size()));
		}
		
		Cellule cellule = listeLigne.get(ligne).getCelluleInColonne(colonne);
		if (cellule == null) {
			new Cellule(this, listeColonne.get(colonne), listeLigne.get(ligne), valeur);
		} else {
			cellule.setContenu(valeur);
		}
		
		nbColonnes = colonne + 1;
		nbLignes = ligne + 1;
		
	}
	
	public int getNbColonnes() {
		return nbColonnes;
	}

	public int getNbLignes() {
		return nbLignes;
	}
	
	public int getNbColonnesSurLigne(int numeroLigne) {
		if (numeroLigne>=listeLigne.size()) {
			return 0;
		}
		return listeLigne.get(numeroLigne).getNumeroColonneMax();
	}

	public String getValeur (int ligne, int colonne) {
		return getCellule(ligne, colonne).getContenu();
	}
	
	public Cellule getCellule (int ligne, int colonne) {
		if (ligne >= listeLigne.size()) {
			return null;
		}
		if (colonne >= listeColonne.size()) {
			return null;
		}
		Cellule cellule = listeLigne.get(ligne).getCelluleInColonne(colonne);
		if (cellule == null) {
			//Possible dans le cas où une ligne a moins de champs que les autres
			return null;
		}
		return cellule;
	}
	
	public String[][] getValeurPlage(int ligneDebut, int ligneFin, int colonneDebut, int colonneFin) {
		return null;
	}
	
	public List<Cellule> getAllCellule() {
		List<Cellule> allCellule = new ArrayList<Cellule>();
		for (Ligne ligne : listeLigne) {
			allCellule.addAll(ligne.getCollectionCellule());
		}
		return allCellule;
	}

	public void insererColonne(TableurModele modele, int numero) {
		Colonne colonne = new Colonne(numero);
		
		//décale les colonnes suivates
		int numeroColonneADecaler=numero;
		while (numeroColonneADecaler < listeColonne.size()) {
			Colonne colonneADecaler = listeColonne.get(numeroColonneADecaler);
			colonneADecaler.setNumero(numeroColonneADecaler+1);
			colonneADecaler.setNom(String.valueOf(colonneADecaler.getNumero()));
			numeroColonneADecaler++;
		}
		//décale la cellule sur toutes les lignes et insère la nouvelle cellule
		for (Ligne ligne : listeLigne) {
			ligne.insertNewCellule(modele, colonne);
		}
		for (TableurModeleStructureListener listener : listeTableurModeleStructureListener) {
			listener.onColonneInsered(colonne);
		}
		listeColonne.add(numero, colonne);
	}
	
	
	public void addModeleStructureListener(TableurModeleStructureListener tableurModeleStructureListener) {
		this.listeTableurModeleStructureListener .add(tableurModeleStructureListener);
	}
	
}
