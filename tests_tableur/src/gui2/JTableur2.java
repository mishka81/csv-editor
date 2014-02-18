package gui2;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import gui.JCellule;
import gui.JColonne;
import gui.JLigne;
import gui.Zone;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import modele.Colonne;
import modele.TableurModele;

public class JTableur2 extends JPanel{
	
	public static final int HAUTEUR_ENTETE_COLONNE=30;
	public static final int LARGEUR_POIGNEE_ENTETE_COLONNE=1;
	public static final int LARGEUR_NUMERO_LIGNE=40;
	public static final int HAUTEUR_POIGNEE_ENTETE_LIGNE=1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<JColonne> listeColonne = new ArrayList<JColonne>();
	List<JLigne> listeLigne = new ArrayList<JLigne>();
	JCellule celluleSelectionnee;
	
	public void setCelluleSelectionne(final JCellule cellule) {
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
	
	public void selectCelluleOnRight(JCellule cellule) {
		JLigne ligne = cellule.getLigne();
		setCelluleSelectionne(ligne.getCelluleOnTheRight(cellule));
	}
	
	public void selectCelluleOnLeft(JCellule cellule) {
		JLigne ligne = cellule.getLigne();
		setCelluleSelectionne(ligne.getCelluleOnTheLeft(cellule));
	}
	public void selectCelluleOnTop(JCellule cellule) {
		JColonne colonne = cellule.getColonne();
		setCelluleSelectionne(colonne.getCelluleOnTheTop(cellule));
	}
	
	public void selectCelluleOnBottom(JCellule cellule) {
		JColonne colonne = cellule.getColonne();
		setCelluleSelectionne(colonne.getCelluleOnTheBottom(cellule));
	}
	
	
	public JTableur2(TableurModele modele) {
		super();
		
		//Initialisation du tableur
		this.setBackground(new Color(177,181,186));
		SpringLayout springLayout = new SpringLayout();
		this.setLayout(springLayout);
		
		int numeroColonne = 0;
		int numeroLigne = 0;
		
		
		
		
		boolean firstCellule=false;
		while (numeroLigne < modele.getNbLignes()) {
			//Récupération de la ligne
			if (numeroLigne == listeLigne.size()) {
				int y = HAUTEUR_ENTETE_COLONNE + HAUTEUR_POIGNEE_ENTETE_LIGNE;
				if (numeroLigne != 0) {
					JLigne jLignePrecedente = listeLigne.get(numeroLigne - 1);
					y = jLignePrecedente.getY() + HAUTEUR_POIGNEE_ENTETE_LIGNE + jLignePrecedente.getHeight(); 
				}
				
				listeLigne.add(new JLigne(numeroLigne, numeroLigne, y, 30));
			}
			JLigne ligne = listeLigne.get(numeroLigne);
			int nombreColonneSurLigne = modele.getNbColonnesSurLigne(numeroLigne);
			
			while (numeroColonne < nombreColonneSurLigne) {
				String valeur = modele.getValeur(numeroLigne, numeroColonne);
				if (valeur != null) {
					//Il y a une donnée saisie à ces coordonnées
					//Récupération de la colonne
					while (numeroColonne == listeColonne.size()) {
						int x = LARGEUR_NUMERO_LIGNE + LARGEUR_POIGNEE_ENTETE_COLONNE;
						if (numeroColonne != 0) {
							JColonne jColonnePrecedente = listeColonne.get(numeroColonne - 1);
							x = jColonnePrecedente.getX() + LARGEUR_POIGNEE_ENTETE_COLONNE + jColonnePrecedente.getWidth(); 
						}
						
						listeColonne.add(new JColonne(numeroColonne, numeroColonne, x, 100));
					}
					JColonne colonne = listeColonne.get(numeroColonne);
					
					//Création de la cellule
					JCellule cellule = new JCellule(this, colonne, ligne, new Zone(valeur));
					cellule.setBounds(colonne.getX(), ligne.getY(),colonne.getWidth(), ligne.getHeight());
					this.add(cellule);
					ligne.addCellule(cellule);
					colonne.addCellule(cellule);
System.out.println(ligne.getY());
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
				} else {
					//TODO gérer le cas des cellules inexistantes
				}
				numeroColonne++;
			}
			numeroLigne++;
		}
		
		//Création des entêtes de colonnes
		int droiteColonnePrecedente = LARGEUR_NUMERO_LIGNE;
		for (JColonne colonne : listeColonne) {
			int gaucheColonne = droiteColonnePrecedente + LARGEUR_POIGNEE_ENTETE_COLONNE;
			colonne.setLocation(gaucheColonne, 0);
			this.add(colonne);
			springLayout.putConstraint(SpringLayout.WEST, colonne,
					gaucheColonne,
	                SpringLayout.WEST, this);
			System.out.println(colonne.getBounds());
			droiteColonnePrecedente = colonne.getX() + colonne.getWidth();
		}
		
		//création des lignes
		int basLignePrecedente = HAUTEUR_ENTETE_COLONNE;
		for (JLigne ligne : listeLigne) {
			int hautLigne = basLignePrecedente + HAUTEUR_POIGNEE_ENTETE_LIGNE;
			ligne.setLocation(0,hautLigne);
			this.add(ligne);
			springLayout.putConstraint(SpringLayout.NORTH, ligne,
	                hautLigne,
	                SpringLayout.NORTH, this);
			System.out.println(ligne.getBounds());
			basLignePrecedente = ligne.getY() + ligne.getHeight();
		}
		
		
		//Création des colonnes
//		JColonne colonne1 = new JColonne(1,1,1,99);
//		JColonne colonne2 = new JColonne(2,2,100,99);
//		JColonne colonne3 = new JColonne(3,3,200,70);
//		listeColonne.add(colonne1);
//		listeColonne.add(colonne2);
//		listeColonne.add(colonne3);
		
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
		
		//TODO restaurer
//		int droiteColonnePrecedente = LARGEUR_NUMERO_LIGNE;
//		for (JColonne colonne : listeColonne) {
//			int gaucheColonne = droiteColonnePrecedente + LARGEUR_POIGNEE_ENTETE_COLONNE;
//			colonne.setLocation(gaucheColonne, 0);
//			this.add(colonne);
//			springLayout.putConstraint(SpringLayout.WEST, colonne,
//					gaucheColonne,
//	                SpringLayout.WEST, this);
//			System.out.println(colonne.getBounds());
//			droiteColonnePrecedente = colonne.getX() + colonne.getWidth();
//		}
		
		//Création des lignes
//		JLigne ligne1 = new JLigne(1,1,1,29);
//		JLigne ligne2 = new JLigne(2,2,30,49);
//		JLigne ligne3 = new JLigne(3,3,80,29);
//		listeLigne.add(ligne1);
//		listeLigne.add(ligne2);
//		listeLigne.add(ligne3);
		
//		int basLignePrecedente = HAUTEUR_ENTETE_COLONNE;
//		for (JLigne ligne : listeLigne) {
//			int hautLigne = basLignePrecedente + HAUTEUR_POIGNEE_ENTETE_LIGNE;
//			ligne.setLocation(0,hautLigne);
//			this.add(ligne);
//			springLayout.putConstraint(SpringLayout.NORTH, ligne,
//	                hautLigne,
//	                SpringLayout.NORTH, this);
//			System.out.println(ligne.getBounds());
//			basLignePrecedente = ligne.getY() + ligne.getHeight();
//		}
		
		//Création des cellules
//		boolean firstCellule = true;
//		for (JColonne colonne : listeColonne) {
//			for (JLigne ligne : listeLigne) {
//				JCellule cellule = new JCellule(this, colonne, ligne, new Zone("test"));
//				cellule.setBounds(colonne.getX(), ligne.getY(),colonne.getWidth(), ligne.getHeight());
//				this.add(cellule);
//				ligne.addCellule(cellule);
//				colonne.addCellule(cellule);
//				springLayout.putConstraint(SpringLayout.NORTH, cellule,
//		                ligne.getY(),
//		                SpringLayout.NORTH, this);
//				springLayout.putConstraint(SpringLayout.WEST, cellule,
//		                colonne.getX(),
//		                SpringLayout.WEST, this);
//				if (firstCellule) {
//					setCelluleSelectionne(cellule);
//					firstCellule = false;
//				}
//			}
//		}
	}
}
