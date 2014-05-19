package gui.action;

import gui.JLigne;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import modele.modele2.TableurModele3;

public class InsererLigneAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLigne ligne;
	private TableurModele3 modele;

	public InsererLigneAction(TableurModele3 modele, JLigne ligne) {
		this.modele = modele;
		this.ligne = ligne;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		modele.insererLigne(ligne.getNumero());

	}

}
