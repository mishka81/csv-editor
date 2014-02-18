package modele;

import java.util.HashMap;
import java.util.Map;


public class Ligne {
	private int numero;
	
	private String nom;
	
	int numeroColonneMax=0;
	

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
		if (numeroColonne > numeroColonneMax){
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
	
	
	
}
