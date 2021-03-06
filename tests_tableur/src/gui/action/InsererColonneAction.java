package gui.action;

import gui.JColonne;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import modele.modele2.TableurModele3;

public class InsererColonneAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JColonne colonne;
	private TableurModele3 modele;

	public InsererColonneAction(TableurModele3 modele, JColonne colonne) {
		this.modele = modele;
		this.colonne = colonne;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		modele.insererColonne(colonne.getNumero());

	}

}
