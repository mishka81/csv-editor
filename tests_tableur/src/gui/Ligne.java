package gui;

import gui2.JTableur2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Ligne extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numero;
	private int index;
	
	private JLabel jLabelNom;
	
	private Map<Integer, Cellule> mapCelluleParIndexColonne = new HashMap<Integer, Cellule>();
	
	
	public Ligne(int numero, int index, int y, int hauteur) {
		super();
		this.numero = numero;
		this.index = index;
		
		this.setLocation(1, y);
		this.setSize(JTableur2.LARGEUR_NUMERO_LIGNE, hauteur);
		this.setPreferredSize(new Dimension(JTableur2.LARGEUR_NUMERO_LIGNE, hauteur));
		
		
		jLabelNom = new JLabel(String.valueOf(numero));
		jLabelNom = new JLabel(String.valueOf(numero), SwingConstants.CENTER);
		this.add(jLabelNom, BorderLayout.CENTER);
	}
	
	public Cellule getCelluleOnTheRight(Cellule cellule) {
		int indexColonne = cellule.getColonne().getIndex() + 1;
		if (mapCelluleParIndexColonne.containsKey(indexColonne)) {
			return mapCelluleParIndexColonne.get(indexColonne);
		}
		return cellule;
	}
	
	public Cellule getCelluleOnTheLeft(Cellule cellule) {
		int indexColonne = cellule.getColonne().getIndex() - 1;
		if (mapCelluleParIndexColonne.containsKey(indexColonne)) {
			return mapCelluleParIndexColonne.get(indexColonne);
		}
		return cellule;
	}

	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void addCellule(Cellule cellule) {
		mapCelluleParIndexColonne.put(cellule.getColonne().getIndex(), cellule);
	}
	
}
