package gui;

import gui2.JTableur2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import modele.Cellule;

public class JLigne extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numero;
	private int index;
	
	private JLabel jLabelNom;
	
	private Map<Integer, JCellule> mapCelluleParIndexColonne = new HashMap<Integer, JCellule>();
	
	
	public JLigne(int numero, int index, int y, int hauteur) {
		super();
		this.numero = numero;
		this.index = index;
		this.setBackground(new Color(223,227,232));
		
		this.setLocation(1, y);
		this.setSize(JTableur2.LARGEUR_NUMERO_LIGNE, hauteur);
		this.setPreferredSize(new Dimension(JTableur2.LARGEUR_NUMERO_LIGNE, hauteur));
		
		
		jLabelNom = new JLabel(String.valueOf(numero));
		jLabelNom = new JLabel(String.valueOf(numero), SwingConstants.CENTER);
		this.add(jLabelNom, BorderLayout.CENTER);
	}
	
	public JCellule getCelluleOnTheRight(JCellule cellule) {
		int indexColonne = cellule.getColonne().getIndex() + 1;
		if (mapCelluleParIndexColonne.containsKey(indexColonne)) {
			return mapCelluleParIndexColonne.get(indexColonne);
		}
		return cellule;
	}
	
	public JCellule getCelluleOnTheLeft(JCellule cellule) {
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
	
	public void addCellule(JCellule cellule) {
		mapCelluleParIndexColonne.put(cellule.getColonne().getIndex(), cellule);
	}
	
	public Collection<JCellule> getCollectionCellule() {
		return mapCelluleParIndexColonne.values();
	}
	
}
