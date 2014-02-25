package gui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gui.FenetrePrincipale;
import gui.JCellule;
import gui.JColonne;
import gui.JLigne;
import gui.Zone;

import javax.swing.AbstractAction;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelListener;

import modele.Cellule;
import modele.Colonne;
import modele.TableurModele;
import modele.TableurModeleStructureListener;

public class JTableur2 extends JPanel implements TableurModeleStructureListener {

	public static final int HAUTEUR_ENTETE_COLONNE = 30;
	public static final int LARGEUR_POIGNEE_ENTETE_COLONNE = 1;
	public static final int LARGEUR_NUMERO_LIGNE = 40;
	public static final int HAUTEUR_POIGNEE_ENTETE_LIGNE = 1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<JColonne> listeColonne = new ArrayList<JColonne>();
	List<JLigne> listeLigne = new ArrayList<JLigne>();
	JCellule celluleSelectionnee;
	
	TableurModele modele;
	SpringLayout springLayout = new SpringLayout();
	JScrollBar scrollbarHorizontal = new JScrollBar(JScrollBar.HORIZONTAL);
	JScrollBar scrollbarVertical = new JScrollBar(JScrollBar.VERTICAL);
	JPanel panelGrille = new JPanel();
	private JFrame fenetrePrincipale;
	int nombreCellulesCrees;
	
	/**
	 * numero de la colonne coll�e au bord gauche
	 */
	int numeroCelluleGauche = 0;

	/**
	 * Num�ro de la colonne coll�e au haut de l'�cran
	 */
	int numeroCelluleHaut = 0;
	
	
	public void setCelluleSelectionne(final JCellule cellule) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				if (JTableur2.this.celluleSelectionnee != null) {
					JTableur2.this.celluleSelectionnee.setSelectionnee(false);
					JTableur2.this.celluleSelectionnee.validerSaise();
					JTableur2.this.celluleSelectionnee.repaint();
				}
				JTableur2.this.celluleSelectionnee = cellule;
				if (JTableur2.this.celluleSelectionnee != null) {
					JTableur2.this.celluleSelectionnee.setSelectionnee(true);
					JTableur2.this.celluleSelectionnee.repaint();
				}
			}
		});
	}
	
	public void selectCelluleOnRight(JCellule cellule) {
		JLigne ligne = cellule.getLigne();
		this.setCelluleSelectionne(ligne.getCelluleOnTheRight(cellule));
	}
	
	public void selectCelluleOnLeft(JCellule cellule) {
		JLigne ligne = cellule.getLigne();
		this.setCelluleSelectionne(ligne.getCelluleOnTheLeft(cellule));
	}
	public void selectCelluleOnTop(JCellule cellule) {
		JColonne colonne = cellule.getColonne();
		this.setCelluleSelectionne(colonne.getCelluleOnTheTop(cellule));
	}
	
	public void selectCelluleOnBottom(JCellule cellule) {
		JColonne colonne = cellule.getColonne();
		this.setCelluleSelectionne(colonne.getCelluleOnTheBottom(cellule));
	}

	public JTableur2(TableurModele modele, JFrame fenetrePrincipale) {
		super();
		this.modele = modele;
		this.fenetrePrincipale = fenetrePrincipale;
		
		//Initialisation du tableur
		this.setBackground(new Color(177,181,186));
		
		this.setLayout(new BorderLayout());
		panelGrille.setLayout(springLayout);
		this.add(panelGrille);
		
		
		this.add(scrollbarHorizontal,BorderLayout.SOUTH);
		this.scrollbarHorizontal.getModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Object source = e.getSource();
			    if (source instanceof BoundedRangeModel) {
			      BoundedRangeModel aModel = (BoundedRangeModel) source;
			      if (!aModel.getValueIsAdjusting()) {
			    	System.out.println(scrollbarHorizontal.getValue());  
			        System.out.println("Changed: " + aModel.getValue());
			      }
			    } else {
			      System.out.println("Something changed: " + source);
			    }
				
			}
		});
		
		this.add(scrollbarVertical,BorderLayout.EAST);
		
		modele.addModeleStructureListener(this);
		
		creerComposant();
		
		//Ajout des listener globaux
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "selectionnerCelluleSurLaDroite");
		this.getActionMap().put("selectionnerCelluleSurLaDroite", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnRight(JTableur2.this.celluleSelectionnee);
			}
		});

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "selectionnerCelluleSurLaGauche");
		this.getActionMap().put("selectionnerCelluleSurLaGauche", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnLeft(JTableur2.this.celluleSelectionnee);
			}
		});

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "selectionnerCelluleSurLeHaut");
		this.getActionMap().put("selectionnerCelluleSurLeHaut", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnTop(JTableur2.this.celluleSelectionnee);
			}
		});

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "selectionnerCelluleSurLeBas");
		this.getActionMap().put("selectionnerCelluleSurLeBas", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnBottom(JTableur2.this.celluleSelectionnee);
			}
		});
	}

	private void creerComposant() {
		//supprime le composant tel qu'il existe
		//TODO supprimer les listeners des JCellule vers les cellules
		for (JLigne jLigne : this.listeLigne) {
			this.remove(jLigne);
			Collection<JCellule> collectionCellule = jLigne.getCollectionCellule();
			for (JCellule jCellule : collectionCellule) {
				this.remove(jCellule);
			}
		}

		for (JColonne jColonne : this.listeColonne) {
			this.remove(jColonne);
		}
		listeLigne.clear();
		listeColonne.clear();
		
		
		int numeroLigne = this.numeroCelluleHaut;
		
		boolean firstCellule=true;
		boolean ligneDepasseBasGrille = false;
		
		while (numeroLigne < modele.getNbLignes()  && !ligneDepasseBasGrille) {
			//R�cup�ration de la ligne
			if (numeroLigne - this.numeroCelluleHaut == this.listeLigne.size()) {
				int y = HAUTEUR_ENTETE_COLONNE + HAUTEUR_POIGNEE_ENTETE_LIGNE;
				if (numeroLigne - this.numeroCelluleHaut != 0) {
					JLigne jLignePrecedente = listeLigne.get(numeroLigne - this.numeroCelluleHaut - 1);
					y = jLignePrecedente.getY() + HAUTEUR_POIGNEE_ENTETE_LIGNE + jLignePrecedente.getHeight(); 
				}
				
				listeLigne.add(new JLigne(numeroLigne, numeroLigne, y, 30));
			}
			JLigne ligne = listeLigne.get(numeroLigne - this.numeroCelluleHaut);
			int nombreColonneSurLigne = modele.getNbColonnesSurLigne(numeroLigne);
			int numeroColonne = this.numeroCelluleGauche;

			//TODO trouver mieux que la dimension de la fen�tre
			if (ligne.getY() + ligne.getHeight() > fenetrePrincipale.getPreferredSize().getHeight()) {
				ligneDepasseBasGrille = true;
			}
			boolean colonneDepasseDroiteGrille = false;
			while (numeroColonne <= nombreColonneSurLigne && !colonneDepasseDroiteGrille) {
				Cellule cellule = modele.getCellule(numeroLigne, numeroColonne);
				if (cellule != null) {
					//Il y a une donn�e saisie � ces coordonn�es
					//R�cup�ration de la colonne
					while (numeroColonne - this.numeroCelluleGauche  == listeColonne.size()) {
						int x = LARGEUR_NUMERO_LIGNE + LARGEUR_POIGNEE_ENTETE_COLONNE;
						if (numeroColonne - this.numeroCelluleGauche != 0) {
							JColonne jColonnePrecedente = listeColonne.get(numeroColonne - this.numeroCelluleGauche - 1);
							x = jColonnePrecedente.getX() + LARGEUR_POIGNEE_ENTETE_COLONNE + jColonnePrecedente.getWidth(); 
						}
						
						listeColonne.add(new JColonne(modele, numeroColonne, numeroColonne, x, 100));
					}
					JColonne colonne = this.listeColonne.get(numeroColonne - this.numeroCelluleGauche);

					//Cr�ation de la cellule
					JCellule jCellule = new JCellule(this, colonne, ligne, new Zone(cellule.getContenu()), cellule);
					jCellule.setBounds(colonne.getX(), ligne.getY(),colonne.getWidth(), ligne.getHeight());
					this.panelGrille.add(jCellule);
					nombreCellulesCrees++;
					ligne.addCellule(jCellule);
					colonne.addCellule(jCellule);

					springLayout.putConstraint(SpringLayout.NORTH, jCellule,
			                ligne.getY(),
			                SpringLayout.NORTH, this);
					springLayout.putConstraint(SpringLayout.WEST, jCellule,
			                colonne.getX(),
			                SpringLayout.WEST, this);
					if (firstCellule) {
						setCelluleSelectionne(jCellule);
						firstCellule = false;
					}
					if (colonne.getX() + colonne.getWidth() > fenetrePrincipale.getPreferredSize().getWidth()) {
						colonneDepasseDroiteGrille = true;
					}
				} else {
					//TODO g�rer le cas des cellules inexistantes
				}
				
				numeroColonne++;
			}
			numeroLigne++;
		}
		
		//Cr�ation des ent�tes de colonnes
		int droiteColonnePrecedente = LARGEUR_NUMERO_LIGNE;
		for (JColonne colonne : listeColonne) {
			int gaucheColonne = droiteColonnePrecedente + LARGEUR_POIGNEE_ENTETE_COLONNE;
			colonne.setLocation(gaucheColonne, 0);
			this.panelGrille.add(colonne);
			springLayout.putConstraint(SpringLayout.WEST, colonne,
					gaucheColonne,
	                SpringLayout.WEST, this);
			//System.out.println(colonne.getBounds());
			droiteColonnePrecedente = colonne.getX() + colonne.getWidth();
		}
		
		//cr�ation des lignes
		int basLignePrecedente = HAUTEUR_ENTETE_COLONNE;
		for (JLigne ligne : listeLigne) {
			int hautLigne = basLignePrecedente + HAUTEUR_POIGNEE_ENTETE_LIGNE;
			ligne.setLocation(0,hautLigne);
			this.panelGrille.add(ligne);
			springLayout.putConstraint(SpringLayout.NORTH, ligne,
	                hautLigne,
	                SpringLayout.NORTH, this);
			System.out.println(ligne.getBounds());
			basLignePrecedente = ligne.getY() + ligne.getHeight();
		}
		System.out.println("Cellules cr��es : " + nombreCellulesCrees);
		
		
	}
	
	/**
	 * Scrolle d'une colonne sur la droite
	 */
	private void scrollerDroite() {
		
	}

	@Override
	public void onColonneInsered(Colonne colonne) {
		//TODO, supprimer la colonne la plus � droite si elle n'est pas affich�e?
		creerComposant();
		
	}
}
