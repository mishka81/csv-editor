package modele;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Ligne {
	private int numero;

	private String nom;

	int numeroColonneMax = 0;

	private Map<Integer, Cellule> mapCelluleParNumeroColonne = new HashMap<Integer, Cellule>();

	public Ligne(int numero) {
		this(numero, String.valueOf(numero));
	}

	public Ligne(int numero, String nom) {
		super();
		this.numero = numero;
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void addCellule(Cellule cellule) {
		int numeroColonne = cellule.getColonne().getNumero();
		if (numeroColonne > numeroColonneMax) {
			numeroColonneMax = numeroColonne;
		}
		mapCelluleParNumeroColonne.put(numeroColonne, cellule);
	}

	public Cellule getCelluleInColonne(int numeroColonne) {
		return mapCelluleParNumeroColonne.get(numeroColonne);
	}

	public int getNumeroColonneMax() {
		return numeroColonneMax;
	}

	public void insertNewCellule(TableurModele modele, Colonne colonne) {
		// d�cale toutes les cellules suivantes vers la droite
		int numeroColonne = colonne.getNumero();
		boolean creerCellule = false;
		for (int i = numeroColonneMax; i >= numeroColonne; i--) {
			mapCelluleParNumeroColonne.put(i + 1, mapCelluleParNumeroColonne.get(i));
			creerCellule = true;
		}

		// La cellule ne doit �tre cr��e que dans le cas o� la colonne n'a pas
		// �t� ins�r�e
		// apr�s la derni�re colonne de la ligne
		if (creerCellule) {
			new Cellule(modele, colonne, this, "");
			numeroColonneMax++;
		}

	}

	public Collection<Cellule> getCollectionCellule() {
		return mapCelluleParNumeroColonne.values();
	}

}
