package modele;


import java.util.HashMap;
import java.util.Map;

public class Colonne{
	private int numero;
	private String nom;

	private Map<Integer, Cellule> mapCelluleParNumeroLigne = new HashMap<Integer, Cellule>();
	
	
	public Colonne(int numero) {
		this(numero, String.valueOf(numero));
	}
	
	public Colonne(int numero, String nom) {
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
		mapCelluleParNumeroLigne.put(cellule.getLigne().getNumero(), cellule);
	}
}
