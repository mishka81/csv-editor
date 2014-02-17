package gui2;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import gui.Cellule;
import gui.Colonne;
import gui.Ligne;
import gui.Zone;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

public class JTableur2 extends JPanel{
	
	public static final int HAUTEUR_ENTETE_COLONNE=30;
	public static final int LARGEUR_POIGNEE_ENTETE_COLONNE=1;
	public static final int LARGEUR_NUMERO_LIGNE=40;
	public static final int HAUTEUR_POIGNEE_ENTETE_LIGNE=1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<Colonne> listeColonne = new ArrayList<Colonne>();
	List<Ligne> listeLigne = new ArrayList<Ligne>();
	Cellule celluleSelectionnee;
	
	public void setCelluleSelectionne(final Cellule cellule) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				if (celluleSelectionnee != null) {
					celluleSelectionnee.setSelectionnee(false);
					celluleSelectionnee.validerSaise();
					celluleSelectionnee.repaint();
				}
				JTableur2.this.celluleSelectionnee = cellule;
				if (celluleSelectionnee != null) {
					celluleSelectionnee.setSelectionnee(true);
					celluleSelectionnee.repaint();
				}
			}
		});
	}
	
	public void selectCelluleOnRight(Cellule cellule) {
		Ligne ligne = cellule.getLigne();
		setCelluleSelectionne(ligne.getCelluleOnTheRight(cellule));
	}
	
	public void selectCelluleOnLeft(Cellule cellule) {
		Ligne ligne = cellule.getLigne();
		setCelluleSelectionne(ligne.getCelluleOnTheLeft(cellule));
	}
	public void selectCelluleOnTop(Cellule cellule) {
		Colonne colonne = cellule.getColonne();
		setCelluleSelectionne(colonne.getCelluleOnTheTop(cellule));
	}
	
	public void selectCelluleOnBottom(Cellule cellule) {
		Colonne colonne = cellule.getColonne();
		setCelluleSelectionne(colonne.getCelluleOnTheBottom(cellule));
	}
	
	
	public JTableur2() {
		super();
		
		//Initialisation du tableur
		this.setBackground(new Color(177,181,186));
		SpringLayout springLayout = new SpringLayout();
		this.setLayout(springLayout);
		
		//Création des colonnes
		Colonne colonne1 = new Colonne(1,1,1,99);
		Colonne colonne2 = new Colonne(2,2,100,99);
		Colonne colonne3 = new Colonne(3,3,200,70);
		listeColonne.add(colonne1);
		listeColonne.add(colonne2);
		listeColonne.add(colonne3);
		
		//Ajout des listener globaux
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "selectionnerCelluleSurLaDroite");
		this.getActionMap().put("selectionnerCelluleSurLaDroite", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnRight(celluleSelectionnee);
			}
		});
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "selectionnerCelluleSurLaGauche");
		this.getActionMap().put("selectionnerCelluleSurLaGauche", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnLeft(celluleSelectionnee);
			}
		});

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "selectionnerCelluleSurLeHaut");
		this.getActionMap().put("selectionnerCelluleSurLeHaut", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnTop(celluleSelectionnee);
			}
		});
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "selectionnerCelluleSurLeBas");
		this.getActionMap().put("selectionnerCelluleSurLeBas", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnBottom(celluleSelectionnee);
			}
		});
		
		int droiteColonnePrecedente = LARGEUR_NUMERO_LIGNE;
		for (Colonne colonne : listeColonne) {
			int gaucheColonne = droiteColonnePrecedente + LARGEUR_POIGNEE_ENTETE_COLONNE;
			colonne.setLocation(gaucheColonne, 0);
			this.add(colonne);
			springLayout.putConstraint(SpringLayout.WEST, colonne,
					gaucheColonne,
	                SpringLayout.WEST, this);
			System.out.println(colonne.getBounds());
			droiteColonnePrecedente = colonne.getX() + colonne.getWidth();
		}
		
		//Création des lignes
		Ligne ligne1 = new Ligne(1,1,1,29);
		Ligne ligne2 = new Ligne(2,2,30,49);
		Ligne ligne3 = new Ligne(3,3,80,29);
		listeLigne.add(ligne1);
		listeLigne.add(ligne2);
		listeLigne.add(ligne3);
		
		int basLignePrecedente = HAUTEUR_ENTETE_COLONNE;
		for (Ligne ligne : listeLigne) {
			int hautLigne = basLignePrecedente + HAUTEUR_POIGNEE_ENTETE_LIGNE;
			ligne.setLocation(0,hautLigne);
			this.add(ligne);
			springLayout.putConstraint(SpringLayout.NORTH, ligne,
	                hautLigne,
	                SpringLayout.NORTH, this);
			System.out.println(ligne.getBounds());
			basLignePrecedente = ligne.getY() + ligne.getHeight();
		}
		
		//Création des cellules
		boolean firstCellule = true;
		for (Colonne colonne : listeColonne) {
			for (Ligne ligne : listeLigne) {
				Cellule cellule = new Cellule(this, colonne, ligne, new Zone("test"));
				cellule.setBounds(colonne.getX(), ligne.getY(),colonne.getWidth(), ligne.getHeight());
				this.add(cellule);
				ligne.addCellule(cellule);
				colonne.addCellule(cellule);
				springLayout.putConstraint(SpringLayout.NORTH, cellule,
		                ligne.getY(),
		                SpringLayout.NORTH, this);
				springLayout.putConstraint(SpringLayout.WEST, cellule,
		                colonne.getX(),
		                SpringLayout.WEST, this);
				if (firstCellule) {
					setCelluleSelectionne(cellule);
					firstCellule = false;
				}
			}
		}
	}
}
