package gui.action;

import gui.JColonne;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import modele.modele2.TableurModele3;

public class SupprimerColonneAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JColonne colonne;
	private TableurModele3 modele;

	public SupprimerColonneAction(TableurModele3 modele, JColonne colonne) {
		this.modele = modele;
		this.colonne = colonne;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		modele.supprimerColonne(colonne.getNumero());

	}

}
