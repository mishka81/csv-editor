package gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import modele.TableurModele;

public class InsererColonneAction extends AbstractAction {

	private int numero;
	private TableurModele modele;

	public InsererColonneAction(TableurModele modele, int numero) {
		this.modele=modele;
		this.numero=numero;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		modele.insererColonne(modele,numero);
		
	}

}
