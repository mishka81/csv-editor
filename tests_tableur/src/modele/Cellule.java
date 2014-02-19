package modele;


import java.util.ArrayList;
import java.util.List;

public class Cellule {
	private Colonne colonne;
	private Ligne ligne;
	private String contenu;
	
	TableurModele modele;
	
	List<CelluleListener> listCelluleListener = new ArrayList<CelluleListener>();
	
	
	public Cellule(TableurModele modele, Colonne colonne, Ligne ligne, String contenu) {
		super();
		this.colonne = colonne;
		this.ligne = ligne;
		this.contenu = contenu;
		this.modele = modele;
		colonne.addCellule(this);
		ligne.addCellule(this);
	}
	
	public void addCelluleListener(CelluleListener listener) {
		this.listCelluleListener.add(listener);
	}
	
	public void removeCelluleListener(CelluleListener listener) {
		this.listCelluleListener.remove(listener);
	}
	
	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		for (CelluleListener celluleListener : listCelluleListener) {
			celluleListener.onContenuChanged(this.contenu, contenu);
		}
		this.contenu = contenu;
	}

	public Colonne getColonne() {
		return colonne;
	}
	public void setColonne(Colonne colonne) {
		this.colonne = colonne;
	}
	public Ligne getLigne() {
		return ligne;
	}
	public void setLigne(Ligne ligne) {
		this.ligne = ligne;
	}
}
