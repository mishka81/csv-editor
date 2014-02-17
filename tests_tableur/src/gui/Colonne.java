package gui;

import gui2.JTableur2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Colonne extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numero;
	private int index;
	
	private JLabel jLabelNom;
	

	private Map<Integer, Cellule> mapCelluleParIndexLigne = new HashMap<Integer, Cellule>();
	
	
	
	public Colonne(int numero, int index, int x, int largeur) {
		super();
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(223,227,232));
		
		this.numero = numero;
		this.index = index;
		this.setLocation(x, 1);
		this.setSize(largeur, JTableur2.HAUTEUR_ENTETE_COLONNE);
		this.setPreferredSize(new Dimension(largeur, JTableur2.HAUTEUR_ENTETE_COLONNE));
		
		
		jLabelNom = new JLabel(String.valueOf(numero));
		jLabelNom = new JLabel(String.valueOf(numero), SwingConstants.CENTER);
		this.add(jLabelNom, BorderLayout.CENTER);
	}
	
	public Cellule getCelluleOnTheBottom(Cellule cellule) {
		int indexLigne = cellule.getLigne().getIndex() + 1;
		if (mapCelluleParIndexLigne.containsKey(indexLigne)) {
			return mapCelluleParIndexLigne.get(indexLigne);
		}
		return cellule;
	}
	
	public Cellule getCelluleOnTheTop(Cellule cellule) {
		int indexLigne = cellule.getLigne().getIndex() - 1;
		if (mapCelluleParIndexLigne.containsKey(indexLigne)) {
			return mapCelluleParIndexLigne.get(indexLigne);
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
		mapCelluleParIndexLigne.put(cellule.getLigne().getIndex(), cellule);
	}
}
