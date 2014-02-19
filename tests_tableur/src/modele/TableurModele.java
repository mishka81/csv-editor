package modele;

import java.util.ArrayList;
import java.util.List;

public class TableurModele {
	private List<Ligne> listeLigne = new ArrayList<Ligne>();
	private List<Colonne> listeColonne = new ArrayList<Colonne>();
	
	int nbColonnes = 0;
	int nbLignes = 0;
	
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
		return cellule.getContenu();
	}
	
	public String[][] getValeurPlage(int ligneDebut, int ligneFin, int colonneDebut, int colonneFin) {
		return null;
	}

	public void insererColonne(TableurModele modele, int numero) {
		//TODO décaler toutes les cellules, mettre à jour les maps des lignes et colonnes
		Colonne colonne = new Colonne(numero);
		
		//décale les colonnes suivates
		int numeroColonneADecaler=numero;
		while (numeroColonneADecaler < listeColonne.size()) {
			Colonne colonneADecaler = listeColonne.get(numeroColonneADecaler);
			colonneADecaler.setNumero(numeroColonneADecaler+1);
			
		}
		
		//crée les cellules vides et les insère sur les lignes
		for (int i = 0; i < listeLigne.size();i++) {
			Ligne ligne = listeLigne.get(i);
			new Cellule(modele, colonne, ligne, "");
			
			
		}
		
		
		listeColonne.add(numero, colonne);
	}
	
}
