package gui2;

import gui.JCellule;
import gui.JColonne;
import gui.JLigne;
import gui.Zone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import modele.modele2.Cellule;
import modele.modele2.TableurModele3;
import modele.modele2.TableurModeleStructureListener;

public class JTableur2 extends JPanel implements TableurModeleStructureListener {

	public static final int HAUTEUR_ENTETE_COLONNE = 30;
	public static final int LARGEUR_POIGNEE_ENTETE_COLONNE = 3;
	public static final int LARGEUR_NUMERO_LIGNE = 40;
	public static final int LARGEUR_DEFAUT_COLONNE = 100;
	public static final int HAUTEUR_POIGNEE_ENTETE_LIGNE = 1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<JColonne> listeColonne = new ArrayList<JColonne>();

	List<JLigne> listeLigne = new ArrayList<JLigne>();
	JCellule celluleSelectionnee;

	TableurModele3 modele;
	SpringLayout springLayout = new SpringLayout();
	JScrollBar scrollbarHorizontal = new JScrollBar(JScrollBar.HORIZONTAL);
	JScrollBar scrollbarVertical = new JScrollBar(JScrollBar.VERTICAL);
	JPanel panelGrille = new JPanel();
	private JFrame fenetrePrincipale;
	int nombreCellulesCrees;

	/**
	 * numero de la colonne collée au bord gauche
	 */
	int numeroCelluleGauche = 0;

	/**
	 * Numéro de la colonne collée au haut de l'écran
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

	public JTableur2(TableurModele3 modele, JFrame fenetrePrincipale) {
		super();
		this.modele = modele;
		this.fenetrePrincipale = fenetrePrincipale;

		// Initialisation du tableur
		this.setBackground(new Color(177, 181, 186));

	}

	public void initialize() {
		this.setLayout(new BorderLayout());
		panelGrille.setLayout(springLayout);
		this.add(panelGrille);

		this.add(scrollbarHorizontal, BorderLayout.SOUTH);
		this.scrollbarHorizontal.setValue(numeroCelluleGauche);
		this.scrollbarHorizontal.setMaximum(modele.getNbColonnes() + 1);
		this.scrollbarHorizontal.getModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Object source = e.getSource();
				if (source instanceof BoundedRangeModel) {
					BoundedRangeModel aModel = (BoundedRangeModel) source;
					if (!aModel.getValueIsAdjusting()) {
						int difference = aModel.getValue() - numeroCelluleGauche;
						if (difference > 0) {
							// l'ascenseur a été déplacé vers la droite
							scrollerDroite(difference);
						} else { 
							scrollerGauche(-difference);
						}
						System.out.println("Changed: " + aModel.getValue());
					}
				} else {
					System.out.println("Something changed: " + source);
				}

			}
		});

		this.add(scrollbarVertical, BorderLayout.EAST);

		this.modele.addModeleStructureListener(this);

		// creerComposant();
		creerZoneComposant(numeroCelluleGauche, modele.getNbColonnes() - 1, 0, modele.getNbLignes() - 1);

		// Ajout des listener globaux
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

		this.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
			@Override
			public void ancestorResized(HierarchyEvent e) {
				super.ancestorResized(e);
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						fillColonnesDroite();
						updateUI();
					}
				});

			}
		});
	}

	private void creerComposant() {
		nombreCellulesCrees = 0;
		// supprime le composant tel qu'il existe
		// TODO supprimer les listeners des JCellule vers les cellules
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

		boolean firstCellule = true;
		boolean ligneDepasseBasGrille = false;

		while (numeroLigne < modele.getNbLignes() && !ligneDepasseBasGrille) {
			// Récupération de la ligne
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

			if (ligne.getY() + ligne.getHeight() > this.getSize().getHeight()) {
				ligneDepasseBasGrille = true;
			}
			boolean colonneDepasseDroiteGrille = false;
			while (numeroColonne <= nombreColonneSurLigne && !colonneDepasseDroiteGrille) {
				Cellule cellule = modele.getCellule(numeroLigne, numeroColonne);
				if (cellule != null) {
					// Il y a une donnée saisie à ces coordonnées
					// Récupération de la colonne
					while (numeroColonne - this.numeroCelluleGauche == listeColonne.size()) {
						int x = LARGEUR_NUMERO_LIGNE + LARGEUR_POIGNEE_ENTETE_COLONNE;
						if (numeroColonne - this.numeroCelluleGauche != 0) {
							JColonne jColonnePrecedente = listeColonne.get(numeroColonne - this.numeroCelluleGauche - 1);
							x = jColonnePrecedente.getX() + LARGEUR_POIGNEE_ENTETE_COLONNE + jColonnePrecedente.getWidth();
						}

						listeColonne.add(new JColonne(modele, numeroColonne, numeroColonne, x, 100, JTableur2.this));
					}
					JColonne colonne = this.listeColonne.get(numeroColonne - this.numeroCelluleGauche);

					// Création de la cellule
					JCellule jCellule = new JCellule(this, colonne, ligne, new Zone(cellule.getContenu()), cellule);
					jCellule.setBounds(colonne.getX(), ligne.getY(), colonne.getWidth(), ligne.getHeight());
					this.panelGrille.add(jCellule);
					nombreCellulesCrees++;
					ligne.addCellule(jCellule);
					colonne.addCellule(jCellule);

					springLayout.putConstraint(SpringLayout.NORTH, jCellule, ligne.getY(), SpringLayout.NORTH, this);
					springLayout.putConstraint(SpringLayout.WEST, jCellule, colonne.getX(), SpringLayout.WEST, this);
					if (firstCellule) {
						setCelluleSelectionne(jCellule);
						firstCellule = false;
					}
					if (colonne.getX() + colonne.getWidth() > this.getSize().getWidth()) {
						colonneDepasseDroiteGrille = true;
					}
				} else {
					// TODO gérer le cas des cellules inexistantes
				}

				numeroColonne++;
			}
			numeroLigne++;
		}

		// Création des entêtes de colonnes
		int droiteColonnePrecedente = LARGEUR_NUMERO_LIGNE;
		for (JColonne colonne : listeColonne) {
			int gaucheColonne = droiteColonnePrecedente + LARGEUR_POIGNEE_ENTETE_COLONNE;
			colonne.setLocation(gaucheColonne, 0);
			this.panelGrille.add(colonne);
			springLayout.putConstraint(SpringLayout.WEST, colonne, gaucheColonne, SpringLayout.WEST, this);
			// System.out.println(colonne.getBounds());
			droiteColonnePrecedente = colonne.getX() + colonne.getWidth();
		}

		// création des lignes
		int basLignePrecedente = HAUTEUR_ENTETE_COLONNE;
		for (JLigne ligne : listeLigne) {
			int hautLigne = basLignePrecedente + HAUTEUR_POIGNEE_ENTETE_LIGNE;
			ligne.setLocation(0, hautLigne);
			this.panelGrille.add(ligne);
			springLayout.putConstraint(SpringLayout.NORTH, ligne, hautLigne, SpringLayout.NORTH, this);
			System.out.println(ligne.getBounds());
			basLignePrecedente = ligne.getY() + ligne.getHeight();
		}
		System.out.println("Cellules créées : " + nombreCellulesCrees);

	}

	/**
	 * Scrolle d'une colonne sur la droite
	 * 
	 * @param difference
	 */
	// private void scrollerDroite(final int difference) {
	// SwingUtilities.invokeLater(new Runnable() {
	//
	// @Override
	// public void run() {
	// int largeurColonneSupprimee = 0;
	// for (int i = 0; i < difference; i++) {
	// // supprime les colonnes de gauche
	// JColonne jColonne = listeColonne.get(0);
	// Collection<JCellule> collectionCellule = jColonne.getCollectionCellule();
	// for (JCellule jCellule : collectionCellule) {
	// panelGrille.remove(jCellule);
	// }
	// listeColonne.remove(0);
	// panelGrille.remove(jColonne);
	// largeurColonneSupprimee += jColonne.getWidth() +
	// LARGEUR_POIGNEE_ENTETE_COLONNE;
	// }
	//
	// numeroCelluleGauche += difference;
	//
	// // décalle les colonnes suivantes
	// for (JColonne jColonne : listeColonne) {
	// jColonne.setLocation(jColonne.getX() - largeurColonneSupprimee,
	// jColonne.getY());
	// springLayout.putConstraint(SpringLayout.WEST, jColonne, jColonne.getX(),
	// SpringLayout.WEST, JTableur2.this);
	// Collection<JCellule> collectionCellule = jColonne.getCollectionCellule();
	// for (JCellule jCellule : collectionCellule) {
	// jCellule.setLocation(jCellule.getX() - largeurColonneSupprimee,
	// jCellule.getY());
	// springLayout.putConstraint(SpringLayout.WEST, jCellule, jCellule.getX(),
	// SpringLayout.WEST, JTableur2.this);
	// }
	// }
	//
	// // ajoute des colonnes à droite
	// JColonne derniereJColonneAffichee = listeColonne.get(listeColonne.size()
	// - 1);
	// int dernierNumeroColonneAffiche = derniereJColonneAffichee.getNumero();
	//
	// listeColonne.add(new JColonne(modele, dernierNumeroColonneAffiche + 1,
	// dernierNumeroColonneAffiche + 1,
	// derniereJColonneAffichee.getDroiteColonne() +
	// LARGEUR_POIGNEE_ENTETE_COLONNE, 100, JTableur2.this));
	//
	// int numeroLigne = numeroCelluleHaut;
	// boolean firstCellule = true;
	// boolean ligneDepasseBasGrille = false;
	// while (numeroLigne < modele.getNbLignes() && !ligneDepasseBasGrille) {
	// // Récupération de la ligne
	// JLigne ligne = listeLigne.get(numeroLigne - numeroCelluleHaut);
	// int nombreColonneSurLigne = modele.getNbColonnesSurLigne(numeroLigne);
	// int numeroColonne = dernierNumeroColonneAffiche;
	//
	// if (ligne.getY() + ligne.getHeight() >
	// JTableur2.this.getSize().getHeight()) {
	// ligneDepasseBasGrille = true;
	// }
	// boolean colonneDepasseDroiteGrille = false;
	// while (numeroColonne <= nombreColonneSurLigne &&
	// !colonneDepasseDroiteGrille) {
	// Cellule cellule = modele.getCellule(numeroLigne, numeroColonne);
	// if (cellule != null) {
	// // Il y a une donnée saisie à ces coordonnées
	// // Récupération de la colonne
	// while (numeroColonne - dernierNumeroColonneAffiche ==
	// listeColonne.size()) {
	// int x = LARGEUR_NUMERO_LIGNE + LARGEUR_POIGNEE_ENTETE_COLONNE;
	// if (numeroColonne - dernierNumeroColonneAffiche != 0) {
	// JColonne jColonnePrecedente = listeColonne.get(numeroColonne -
	// dernierNumeroColonneAffiche - 1);
	// x = jColonnePrecedente.getX() + LARGEUR_POIGNEE_ENTETE_COLONNE +
	// jColonnePrecedente.getWidth();
	// }
	//
	// listeColonne.add(new JColonne(modele, numeroColonne, numeroColonne, x,
	// 100, JTableur2.this));
	// }
	// JColonne colonne = listeColonne.get(numeroColonne -
	// dernierNumeroColonneAffiche);
	//
	// // Création de la cellule
	// JCellule jCellule = new JCellule(JTableur2.this, colonne, ligne, new
	// Zone(cellule.getContenu()), cellule);
	// jCellule.setBounds(colonne.getX(), ligne.getY(), colonne.getWidth(),
	// ligne.getHeight());
	// panelGrille.add(jCellule);
	// nombreCellulesCrees++;
	// ligne.addCellule(jCellule);
	// colonne.addCellule(jCellule);
	//
	// springLayout.putConstraint(SpringLayout.NORTH, jCellule, ligne.getY(),
	// SpringLayout.NORTH, JTableur2.this);
	// springLayout.putConstraint(SpringLayout.WEST, jCellule, colonne.getX(),
	// SpringLayout.WEST, JTableur2.this);
	// if (firstCellule) {
	// setCelluleSelectionne(jCellule);
	// firstCellule = false;
	// }
	// if (colonne.getX() + colonne.getWidth() >
	// JTableur2.this.getSize().getWidth()) {
	// colonneDepasseDroiteGrille = true;
	// }
	// } else {
	// // TODO gérer le cas des cellules inexistantes
	// }
	//
	// numeroColonne++;
	// }
	// numeroLigne++;
	// }
	//
	// // Création des entêtes de colonnes
	// int droiteColonnePrecedente = LARGEUR_NUMERO_LIGNE;
	// for (JColonne colonne : listeColonne) {
	// int gaucheColonne = droiteColonnePrecedente +
	// LARGEUR_POIGNEE_ENTETE_COLONNE;
	// colonne.setLocation(gaucheColonne, 0);
	// JTableur2.this.panelGrille.add(colonne);
	// springLayout.putConstraint(SpringLayout.WEST, colonne, gaucheColonne,
	// SpringLayout.WEST, JTableur2.this);
	// // System.out.println(colonne.getBounds());
	// droiteColonnePrecedente = colonne.getX() + colonne.getWidth();
	// }
	//
	// panelGrille.updateUI();
	//
	// }
	// });
	//
	// }

	// @Override
	// public void onColonneInsered(final Colonne colonne) {
	// // TODO, supprimer la colonne la plus à droite si elle n'est pas
	// // affichée?
	// // creerComposant();
	// SwingUtilities.invokeLater(new Runnable() {
	//
	// @Override
	// public void run() {
	// insererTitreDeColonne(colonne.getNumero(), colonne.getNumero(),
	// colonne.getNumero() - numeroCelluleGauche);
	// JTableur2.this.updateUI();
	// }
	// });
	//
	// }

	// ///////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Crée les entêtes de colonnes dans une zone
	 * 
	 * @param numeroColonneMin
	 *            numero de la première colonne à créer
	 * @param numeroColonneMax
	 *            numero de la dernière colonne à créer
	 * @param index
	 *            Index de la première colonne (0 est la colonne la plus à
	 *            gauche)
	 */
	private void insererTitreDeColonne(int numeroColonneMin, int numeroColonneMax, int indexMin) {
		for (int i = numeroColonneMin; i <= numeroColonneMax; i++) {
			// l'index represente l'index dans la liste
			int index = indexMin - numeroColonneMin + i;
			int positionGauche = getCoordonneeGauche(index);
			JColonne jColonne = new JColonne(modele, i, index, positionGauche, LARGEUR_DEFAUT_COLONNE, this);
			JTableur2.this.panelGrille.add(jColonne);
			listeColonne.add(index, jColonne);

			springLayout.putConstraint(SpringLayout.WEST, jColonne, positionGauche, SpringLayout.WEST, JTableur2.this);
			System.out.println("Entête de colonne " + i + " créée à l'index " + index);

			if (getCoordonneeDroite(index) >= this.getWidth()) {
				break;
			}
		}

		// décalle les colonnes suivantes si il y en a
		int nombreColonnesInserees = numeroColonneMax - numeroColonneMin + 1;
		int indexDerniereColonneInseree = indexMin + nombreColonnesInserees - 1;
		int indexADecaller = indexDerniereColonneInseree + 1;
		while (indexADecaller < listeColonne.size()) {
			JColonne jColonne = listeColonne.get(indexADecaller);
			jColonne.setIndex(indexADecaller);
			jColonne.setNumero(jColonne.getNumero() + nombreColonnesInserees);
			int positionDroiteColonnePrecedente = getCoordonneeDroite(indexADecaller - 1);

			jColonne.setLocation(positionDroiteColonnePrecedente, 0);
			springLayout.putConstraint(SpringLayout.WEST, jColonne, positionDroiteColonnePrecedente, SpringLayout.WEST, JTableur2.this);
			indexADecaller++;
		}
	}

	/**
	 * récupère la position gauche de la colonne en fonction de la colonne
	 * précédente
	 * 
	 * @param numeroColonneMin
	 *            index de colonne dont on veut la position
	 */
	private int getCoordonneeGauche(int indexColonne) {
		if (indexColonne == 0) {
			// C'est la colonne de gauche, on la positionne donc en fonction
			return LARGEUR_NUMERO_LIGNE;
		}
		if (indexColonne <= listeColonne.size()) {
			JColonne jColonnePrecedente = listeColonne.get(indexColonne - 1);
			return jColonnePrecedente.getX() + jColonnePrecedente.getWidth();
		}
		throw new RuntimeException("Impossible de récupérer la coordonnée gauche de la colonne à l'index " + indexColonne + " car actuellement, il n'existe que " + listeColonne.size() + " colonnes.");
	}

	/**
	 * récupère la position droite de la colonne en fonction de la colonne
	 * (position de la droite de la poignée de redimmentionnement) précédente
	 * 
	 * @param indexColonne
	 *            indexColonne de colonne dont on veut la position
	 */
	private int getCoordonneeDroite(int indexColonne) {
		if (indexColonne < 0) {
			return LARGEUR_NUMERO_LIGNE;
		}
		if (indexColonne < listeColonne.size()) {
			JColonne jColonne = listeColonne.get(indexColonne);
			return jColonne.getX() + jColonne.getWidth();
		}
		throw new RuntimeException("Impossible de récupérer la coordonnée droite de la colonne à l'index " + indexColonne + " car actuellement, il n'existe que " + listeColonne.size() + " colonnes.");
	}

	private void creerTitreLigne(int numeroLigneMin, int numeroLigneMax) {

	}

	private void creerZoneComposant(int numeroColonneMin, int numeroColonneMax, int numeroLigneMin, int numeroLigneMax) {
		// TODO création des lignes/colonnes/cellules dans la zone choisie
		insererTitreDeColonne(numeroColonneMin, numeroColonneMax, 0);

		// TODO Si il existe des colonnes/lignes après les limites de la zone
		// choisie, les décaler

		// TODO, si le décallage fait sortir la ligne/colonne du composant, on
		// supprime toutes les lignes/colonnes suivantes

		// TODO si le décallage fait que la dernière ligne/colonne finit dans
		// les limites du composant, on en charge d'autres jusqu'à revenir au
		// bord du composant
	}

	public void setLargeurColonne(JColonne colonne, int nouvelleLargeur) {
		colonne.setPreferredSize(new Dimension(nouvelleLargeur, JTableur2.HAUTEUR_ENTETE_COLONNE));
		colonne.setSize(nouvelleLargeur, JTableur2.HAUTEUR_ENTETE_COLONNE);

		int indexColonneRedimmensionnee = colonne.getIndex();
		for (int indexColonneADeplacer = indexColonneRedimmensionnee + 1; indexColonneADeplacer < listeColonne.size(); indexColonneADeplacer++) {
			JColonne jColonne = listeColonne.get(indexColonneADeplacer);
			int positionDroiteColonnePrecedente = getCoordonneeDroite(indexColonneADeplacer - 1);

			jColonne.setLocation(positionDroiteColonnePrecedente, 0);
			springLayout.putConstraint(SpringLayout.WEST, jColonne, positionDroiteColonnePrecedente, SpringLayout.WEST, JTableur2.this);
		}

		fillColonnesDroite();

		updateUI();
	}

	/**
	 * Charge des colonnes à droite si besoin Utile dans le cas où une colonne a
	 * été supprimée, redimmensionnée, si la fenêtre a été redimmensionnée
	 */
	public void fillColonnesDroite() {
		JColonne jDerniereColonne = listeColonne.get(listeColonne.size() - 1);
		int droiteDerniereColonne = jDerniereColonne.getDroiteColonne();
		while (droiteDerniereColonne < this.getWidth() && jDerniereColonne.getNumero() + 1 < modele.getNbColonnes()) {
			insererTitreDeColonne(jDerniereColonne.getNumero() + 1, jDerniereColonne.getNumero() + 1, jDerniereColonne.getIndex() + 1);

			jDerniereColonne = listeColonne.get(listeColonne.size() - 1);
			droiteDerniereColonne = jDerniereColonne.getDroiteColonne();
		}
	}

	@Override
	public void onColonneInsered(final int numero) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				insererTitreDeColonne(numero, numero, numero - numeroCelluleGauche);
				JTableur2.this.updateUI();
			}
		});

	}

	@Override
	public void onColonneRemoved(final int numero) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				supprimerTitreDeColonne(numero, numero, numero - numeroCelluleGauche);
				JTableur2.this.updateUI();
			}
		});

	}

	protected void supprimerTitreDeColonne(int numeroColonneMin, int numeroColonneMax, int indexMin) {
		// Supprime les colonnes
		for (int i = numeroColonneMin; i <= numeroColonneMax; i++) {
			JColonne colonneASupprimer = listeColonne.get(indexMin);
			listeColonne.remove(colonneASupprimer);
			this.panelGrille.remove(colonneASupprimer);
		}

		// décalle les colonnes suivantes si il y en a
		int nombreColonnesSupprimees = numeroColonneMax - numeroColonneMin + 1;
		int indexPremiereColonneSupprimee = indexMin;
		int indexADecaller = indexPremiereColonneSupprimee;
		while (indexADecaller < listeColonne.size()) {
			JColonne jColonne = listeColonne.get(indexADecaller);
			jColonne.setIndex(indexADecaller);
			jColonne.setNumero(jColonne.getNumero() - nombreColonnesSupprimees);
			int positionDroiteColonnePrecedente = LARGEUR_NUMERO_LIGNE;
			if (indexADecaller != 0) {
				positionDroiteColonnePrecedente = getCoordonneeDroite(indexADecaller - 1);
			}

			jColonne.setLocation(positionDroiteColonnePrecedente, 0);
			springLayout.putConstraint(SpringLayout.WEST, jColonne, positionDroiteColonnePrecedente, SpringLayout.WEST, JTableur2.this);
			indexADecaller++;
		}
		fillColonnesDroite();

	}

	private void scrollerGauche(int nombreColonnes) {
		// insererTitreDeColonne(numeroCelluleGauche - nombreColonnes,
		// numeroCelluleGauche - nombreColonnes, 0);
		// Ne pas utiliser insérerTitreColonne (car modifie le numéro des
		// colonnes suivantes), mais plutôt créer une nouvelle colonne

		numeroCelluleGauche -= nombreColonnes;
		for (int i = nombreColonnes; i < listeColonne.size(); i++) {
			replacerColonne(listeColonne.get(i));
		}
		updateUI();
	}

	private void scrollerDroite(int nombreColonnes) {
		// Supprime les colonnes de gauche
		for (int i = 0; i < nombreColonnes; i++) {
			JColonne jColonne = listeColonne.get(0);
			listeColonne.remove(0);
			this.panelGrille.remove(jColonne);
		}

		// Décale les cellules restantes
		numeroCelluleGauche += nombreColonnes;
		for (int i = 0; i < listeColonne.size(); i++) {
			JColonne jColonne = listeColonne.get(i);
			replacerColonne(jColonne);
			jColonne.setIndex(jColonne.getIndex() - nombreColonnes);
		}

		// Rajoute des colonnes à droite si besoin
		fillColonnesDroite();

		updateUI();
	}

	private void replacerColonne(JColonne jColonne) {
		if (jColonne.getNumero() == numeroCelluleGauche) {
			// C'est la colonne la plus à gauche, il faut la mettre à la
			// position correspondante
			positionnerColonne(jColonne, LARGEUR_NUMERO_LIGNE);
		} else {
			int indexColonne = listeColonne.indexOf(jColonne);
			int coordonneeDroiteColonnePrecedente = getCoordonneeDroite(indexColonne - 1);
			positionnerColonne(jColonne, coordonneeDroiteColonnePrecedente);
		}
	}

	private void positionnerColonne(JColonne jColonne, int positionGauche) {
		jColonne.setLocation(positionGauche, 0);
		springLayout.putConstraint(SpringLayout.WEST, jColonne, positionGauche, SpringLayout.WEST, JTableur2.this);
	}
}
