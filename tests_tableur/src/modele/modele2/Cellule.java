package modele.modele2;

import java.util.ArrayList;
import java.util.List;

import modele.CelluleListener;

public class Cellule {
	private String contenu;

	List<CelluleListener> listCelluleListener = new ArrayList<CelluleListener>();

	public Cellule(String contenu) {
		super();
		this.contenu = contenu;
	}

	public void addCelluleListener(CelluleListener listener) {
		this.listCelluleListener.add(listener);
	}

	public void removeCelluleListener(CelluleListener listener) {
		this.listCelluleListener.remove(listener);
	}

	public void removeAllCelluleListener() {
		this.listCelluleListener.clear();
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

	@Override
	public String toString() {
		return contenu;
	}

}
