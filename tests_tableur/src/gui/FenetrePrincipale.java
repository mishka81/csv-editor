package gui;

import gui2.JTableur2;

import java.awt.Dimension;

import javax.swing.JFrame;

import modele.modele2.TableurModele3;

public class FenetrePrincipale extends JFrame {

	public FenetrePrincipale() {
		super();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		TableurModele3 modele = new TableurModele3();
		creerModeleLeger(modele);
		// creerModeleLourd(modele);

		this.setPreferredSize(new Dimension(1024, 480));

		JTableur2 jTableur2 = new JTableur2(modele);
		this.getContentPane().add(jTableur2);

		this.pack();
		jTableur2.initialize();
		this.setLocationRelativeTo(null);
		// this.getRootPane().updateUI();
		// this.getRootPane().update(this.getRootPane().getGraphics());

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	private void creerModeleLourd(TableurModele3 modele) {
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 50; j++) {
				modele.setValeur(i, j, i + "." + j);
			}
		}
	}

	private void creerModeleLeger(TableurModele3 modele) {
		modele.setValeur(0, 0, "0.0");
		modele.setValeur(0, 1, "0.1");
		modele.setValeur(0, 2, "0.2");
		modele.setValeur(0, 3, "0.3");
		modele.setValeur(1, 0, "1.0");
		modele.setValeur(1, 1, "1.1");
		modele.setValeur(1, 2, "1.2");
		modele.setValeur(1, 3, "1.3");
		modele.setValeur(1, 4, "1.4");
		modele.setValeur(1, 5, "1.5");
		modele.setValeur(3, 0, "3.0");
		modele.setValeur(3, 1, "3.1");
		modele.setValeur(3, 2, "3.2");
		modele.setValeur(3, 3, "3.3");
		modele.setValeur(3, 4, "3.4");
		modele.setValeur(3, 5, "3.5");
		modele.setValeur(4, 0, "4.0");
		modele.setValeur(4, 1, "4.1");
		modele.setValeur(4, 2, "4.2");
		modele.setValeur(4, 3, "4.3");
		modele.setValeur(4, 4, "4.4");
	}

	private void creerModeleLeger2(TableurModele3 modele) {
		modele.setValeur(0, 0, "0.0");
		modele.setValeur(0, 1, "0.1");
		modele.setValeur(0, 2, "0.2");
		modele.setValeur(0, 3, "0.3");
		modele.setValeur(1, 0, "1.0");
		modele.setValeur(1, 1, "1.1");
		modele.setValeur(1, 2, "1.2");
		modele.setValeur(1, 3, "1.3");

	}

}
