package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JTableur extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<JColonne> listeColonne = new ArrayList<JColonne>();
	List<JLigne> listeLigne = new ArrayList<JLigne>();
	

	public JTableur() {
		super();
		
		JColonne colonne1 = new JColonne(1,1,1, 99);
		JColonne colonne2 = new JColonne(2,2,100, 100);
		JColonne colonne3 = new JColonne(3,3,200, 50);
		listeColonne.add(colonne1);
		listeColonne.add(colonne2);
		listeColonne.add(colonne3);
		
		this.add(colonne1);
		this.add(colonne2);
		this.add(colonne3);
	}




	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		
		//Dessine les entêtes de colonne
		for (JColonne colonne : listeColonne) {
			colonne.paint(g);
		}
		//super.paint(g);
	}
}
