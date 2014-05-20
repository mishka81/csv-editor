package gui2;

import gui.JCellule;
import gui.JColonne;
import gui.JLigne;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modele.modele2.TableurModele3;
import modele.modele2.TableurModeleStructureListener;

public class JTableur2 extends JPanel implements TableurModeleStructureListener {

	public static final int HAUTEUR_ENTETE_COLONNE = 30;
	public static final int LARGEUR_POIGNEE_ENTETE_COLONNE = 3;
	public static final int LARGEUR_NUMERO_LIGNE = 40;
	public static final int LARGEUR_DEFAUT_COLONNE = 100;
	public static final int HAUTEUR_DEFAUT_LIGNE = 30;
	public static final int HAUTEUR_POIGNEE_ENTETE_LIGNE = 3;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<JColonne> listeColonne = new ArrayList<JColonne>();

	List<JLigne> listeLigne = new ArrayList<JLigne>();
	JCellule celluleSelectionnee;

	TableurModele3 modele;
	public SpringLayout springLayout = new SpringLayout();
	JScrollBar scrollbarHorizontal = new JScrollBar(JScrollBar.HORIZONTAL);
	JScrollBar scrollbarVertical = new JScrollBar(JScrollBar.VERTICAL);
	JPanel panelGrille = new JPanel();
	int nombreCellulesCrees;

	Map<Integer, Integer> mapLargeurParNumeroColonne = new HashMap<Integer, Integer>();

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

	public JTableur2(TableurModele3 modele) {
		super();
		this.modele = modele;

		// Initialisation du tableur
		this.setBackground(new Color(177, 181, 186));

	}

	public void initialize() {
		this.setLayout(new BorderLayout());
		panelGrille.setLayout(springLayout);
		this.add(panelGrille);

		this.add(scrollbarHorizontal, BorderLayout.SOUTH);
		this.scrollbarHorizontal.setValue(numeroCelluleGauche);
		setValeurMaximumHorizontalScrollbar();
		this.scrollbarHorizontal.getModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Object source = e.getSource();
				if (source instanceof BoundedRangeModel) {
					BoundedRangeModel aModel = (BoundedRangeModel) source;
					// if (!aModel.getValueIsAdjusting()) {
					int difference = aModel.getValue() - numeroCelluleGauche;
					if (difference > 0) {
						// l'ascenseur a �t� d�plac� vers la droite
						scrollerDroite(difference);
					} else {
						scrollerGauche(-difference);
					}
					System.out.println("nombre de colonnes charg�es : " + listeColonne.size());
					// }
				} else {
					System.out.println("Something changed: " + source);
				}

			}
		});

		this.add(scrollbarVertical, BorderLayout.EAST);
		this.scrollbarVertical.setValue(numeroCelluleHaut);
		setValeurMaximumVerticalScrollbar();
		this.scrollbarVertical.getModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Object source = e.getSource();
				if (source instanceof BoundedRangeModel) {
					BoundedRangeModel aModel = (BoundedRangeModel) source;
					// if (!aModel.getValueIsAdjusting()) {
					int difference = aModel.getValue() - numeroCelluleHaut;
					if (difference > 0) {
						// l'ascenseur a �t� d�plac� vers le bas
						scrollerBas(difference);
					} else {
						scrollerHaut(-difference);
					}
					System.out.println("nombre de lignes charg�es : " + listeLigne.size());
					// }
				} else {
					System.out.println("Something changed: " + source);
				}

			}
		});

		this.modele.addModeleStructureListener(this);

		// creerComposant();
		creerZoneComposant(numeroCelluleGauche, modele.getNbColonnes() - 1, numeroCelluleHaut, modele.getNbLignes() - 1);

		// Ajout des listener globaux
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "selectionnerCelluleSurLaDroite");
		this.getActionMap().put("selectionnerCelluleSurLaDroite", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnRight(JTableur2.this.celluleSelectionnee);
			}
		});

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "selectionnerCelluleSurLaGauche");
		this.getActionMap().put("selectionnerCelluleSurLaGauche", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnLeft(JTableur2.this.celluleSelectionnee);
			}
		});

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "selectionnerCelluleSurLeHaut");
		this.getActionMap().put("selectionnerCelluleSurLeHaut", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTableur2.this.selectCelluleOnTop(JTableur2.this.celluleSelectionnee);
			}
		});

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "selectionnerCelluleSurLeBas");
		this.getActionMap().put("selectionnerCelluleSurLeBas", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
						supprimerEnteteColonneDroite();

						fillLignesBas();
						supprimerEnteteLigneBas();
						updateUI();
					}
				});

			}
		});
	}

	void setValeurMaximumHorizontalScrollbar() {
		this.scrollbarHorizontal.setMaximum(modele.getNbColonnes() + 1);
	}

	void setValeurMaximumVerticalScrollbar() {
		this.scrollbarVertical.setMaximum(modele.getNbLignes() + 1);
	}

	// private void creerComposant() {
	// nombreCellulesCrees = 0;
	// // supprime le composant tel qu'il existe
	// // TODO supprimer les listeners des JCellule vers les cellules
	// for (JLigne jLigne : this.listeLigne) {
	// this.remove(jLigne);
	// Collection<JCellule> collectionCellule = jLigne.getCollectionCellule();
	// for (JCellule jCellule : collectionCellule) {
	// this.remove(jCellule);
	// }
	// }
	//
	// for (JColonne jColonne : this.listeColonne) {
	// this.remove(jColonne);
	// }
	// listeLigne.clear();
	// listeColonne.clear();
	//
	// int numeroLigne = this.numeroCelluleHaut;
	//
	// boolean firstCellule = true;
	// boolean ligneDepasseBasGrille = false;
	//
	// while (numeroLigne < modele.getNbLignes() && !ligneDepasseBasGrille) {
	// // R�cup�ration de la ligne
	// if (numeroLigne - this.numeroCelluleHaut == this.listeLigne.size()) {
	// int y = HAUTEUR_ENTETE_COLONNE + HAUTEUR_POIGNEE_ENTETE_LIGNE;
	// if (numeroLigne - this.numeroCelluleHaut != 0) {
	// JLigne jLignePrecedente = listeLigne.get(numeroLigne -
	// this.numeroCelluleHaut - 1);
	// y = jLignePrecedente.getY() + HAUTEUR_POIGNEE_ENTETE_LIGNE +
	// jLignePrecedente.getHeight();
	// }
	//
	// listeLigne.add(new JLigne(numeroLigne, numeroLigne, y, 30));
	// }
	// JLigne ligne = listeLigne.get(numeroLigne - this.numeroCelluleHaut);
	// int nombreColonneSurLigne = modele.getNbColonnesSurLigne(numeroLigne);
	// int numeroColonne = this.numeroCelluleGauche;
	//
	// if (ligne.getY() + ligne.getHeight() > this.getSize().getHeight()) {
	// ligneDepasseBasGrille = true;
	// }
	// boolean colonneDepasseDroiteGrille = false;
	// while (numeroColonne <= nombreColonneSurLigne &&
	// !colonneDepasseDroiteGrille) {
	// Cellule cellule = modele.getCellule(numeroLigne, numeroColonne);
	// if (cellule != null) {
	// // Il y a une donn�e saisie � ces coordonn�es
	// // R�cup�ration de la colonne
	// while (numeroColonne - this.numeroCelluleGauche == listeColonne.size()) {
	// int x = LARGEUR_NUMERO_LIGNE + LARGEUR_POIGNEE_ENTETE_COLONNE;
	// if (numeroColonne - this.numeroCelluleGauche != 0) {
	// JColonne jColonnePrecedente = listeColonne.get(numeroColonne -
	// this.numeroCelluleGauche - 1);
	// x = jColonnePrecedente.getX() + LARGEUR_POIGNEE_ENTETE_COLONNE +
	// jColonnePrecedente.getWidth();
	// }
	//
	// listeColonne.add(new JColonne(modele, numeroColonne, numeroColonne, x,
	// 100, JTableur2.this));
	// }
	// JColonne colonne = this.listeColonne.get(numeroColonne -
	// this.numeroCelluleGauche);
	//
	// // Cr�ation de la cellule
	// JCellule jCellule = new JCellule(this, colonne, ligne, new
	// Zone(cellule.getContenu()), cellule);
	// jCellule.setBounds(colonne.getX(), ligne.getY(), colonne.getWidth(),
	// ligne.getHeight());
	// this.panelGrille.add(jCellule);
	// nombreCellulesCrees++;
	// ligne.addCellule(jCellule);
	// colonne.addCellule(jCellule);
	//
	// springLayout.putConstraint(SpringLayout.NORTH, jCellule, ligne.getY(),
	// SpringLayout.NORTH, this);
	// springLayout.putConstraint(SpringLayout.WEST, jCellule, colonne.getX(),
	// SpringLayout.WEST, this);
	// if (firstCellule) {
	// setCelluleSelectionne(jCellule);
	// firstCellule = false;
	// }
	// if (colonne.getX() + colonne.getWidth() > this.getSize().getWidth()) {
	// colonneDepasseDroiteGrille = true;
	// }
	// } else {
	// // TODO g�rer le cas des cellules inexistantes
	// }
	//
	// numeroColonne++;
	// }
	// numeroLigne++;
	// }
	//
	// // Cr�ation des ent�tes de colonnes
	// int droiteColonnePrecedente = LARGEUR_NUMERO_LIGNE;
	// for (JColonne colonne : listeColonne) {
	// int gaucheColonne = droiteColonnePrecedente +
	// LARGEUR_POIGNEE_ENTETE_COLONNE;
	// colonne.setLocation(gaucheColonne, 0);
	// this.panelGrille.add(colonne);
	// springLayout.putConstraint(SpringLayout.WEST, colonne, gaucheColonne,
	// SpringLayout.WEST, this);
	// // System.out.println(colonne.getBounds());
	// droiteColonnePrecedente = colonne.getX() + colonne.getWidth();
	// }
	//
	// // cr�ation des lignes
	// int basLignePrecedente = HAUTEUR_ENTETE_COLONNE;
	// for (JLigne ligne : listeLigne) {
	// int hautLigne = basLignePrecedente + HAUTEUR_POIGNEE_ENTETE_LIGNE;
	// ligne.setLocation(0, hautLigne);
	// this.panelGrille.add(ligne);
	// springLayout.putConstraint(SpringLayout.NORTH, ligne, hautLigne,
	// SpringLayout.NORTH, this);
	// System.out.println(ligne.getBounds());
	// basLignePrecedente = ligne.getY() + ligne.getHeight();
	// }
	// System.out.println("Cellules cr��es : " + nombreCellulesCrees);
	//
	// }

	// ///////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Cr�e les ent�tes de colonnes dans une zone
	 * 
	 * @param numeroColonneMin
	 *            numero de la premi�re colonne � cr�er
	 * @param numeroColonneMax
	 *            numero de la derni�re colonne � cr�er
	 * @param index
	 *            Index de la premi�re colonne (0 est la colonne la plus �
	 *            gauche)
	 */
	private void insererTitreDeColonne(int numeroColonneMin, int numeroColonneMax, int indexMin) {
		int indexDerniereColonneInseree = ColonneHelper.getInstance(this).insererComposantTitreDeColonne(numeroColonneMin, numeroColonneMax, indexMin);

		// // Renum�rote les colonnes suivantes
		int nombreColonnesInserees = numeroColonneMax - numeroColonneMin + 1;
		int indexADecaller = indexDerniereColonneInseree + 1;
		renumeroterColonnes(indexADecaller, nombreColonnesInserees);

		setValeurMaximumHorizontalScrollbar();
		System.out.println("nombre de colonnes charg�es (apr�s insersion) : " + listeColonne.size());
	}

	/**
	 * Cr�e les ent�tes de colonnes dans une zone
	 * 
	 * @param numeroLigneMin
	 *            numero de la premi�re ligne � cr�er
	 * @param numeroLigneMax
	 *            numero de la derni�re ligne � cr�er
	 * @param index
	 *            Index de la premi�re ligne (0 est la ligne la plus en haut)
	 */
	private void insererNumeroDeLigne(int numeroLigneMin, int numeroLigneMax, int indexMin) {
		int indexDerniereLigneInseree = LigneHelper.getInstance(this).insererComposantNumeroDeLigne(numeroLigneMin, numeroLigneMax, indexMin);

		// // Renum�rote les colonnes suivantes
		int nombreLignesInserees = numeroLigneMax - numeroLigneMin + 1;
		int indexADecaller = indexDerniereLigneInseree + 1;
		renumeroterLignes(indexADecaller, nombreLignesInserees);

		setValeurMaximumVerticalScrollbar();
		System.out.println("nombre de lignes charg�es (apr�s insersion) : " + listeLigne.size());
	}

	private void creerZoneComposant(int numeroColonneMin, int numeroColonneMax, int numeroLigneMin, int numeroLigneMax) {
		// TODO cr�ation des lignes/colonnes/cellules dans la zone choisie
		insererTitreDeColonne(numeroColonneMin, numeroColonneMax, 0);
		insererNumeroDeLigne(numeroLigneMin, numeroLigneMax, 0);
		genererCellule(numeroLigneMin, numeroLigneMax, numeroColonneMin, numeroColonneMax);

		// TODO Si il existe des colonnes/lignes apr�s les limites de la zone
		// choisie, les d�caler

		// TODO, si le d�callage fait sortir la ligne/colonne du composant, on
		// supprime toutes les lignes/colonnes suivantes

		// TODO si le d�callage fait que la derni�re ligne/colonne finit dans
		// les limites du composant, on en charge d'autres jusqu'� revenir au
		// bord du composant
	}

	private void genererCellule(int numeroLigneMin, int numeroLigneMax, int numeroColonneMin, int numeroColonneMax) {
		for (int numeroLigne = numeroLigneMin; numeroLigne <= numeroLigneMax; numeroLigne++) {
			for (int numeroColonne = numeroColonneMin; numeroColonne <= numeroColonneMax; numeroColonne++) {
				JColonne colonne = listeColonne.get(numeroColonne);
				JLigne ligne = listeLigne.get(numeroLigne);
				JCellule jCellule = new JCellule(this, colonne, ligne, modele.getCellule(numeroLigne, numeroColonne), modele);
				jCellule.setBounds(colonne.getX(), ligne.getY(), colonne.getWidth(), ligne.getHeight());
				this.panelGrille.add(jCellule);
				// nombreCellulesCrees++;
				ligne.addCellule(jCellule);
				colonne.addCellule(jCellule);
				springLayout.putConstraint(SpringLayout.NORTH, jCellule, ligne.getY(), SpringLayout.NORTH, this);
				springLayout.putConstraint(SpringLayout.WEST, jCellule, colonne.getX(), SpringLayout.WEST, this);
			}
		}

	}

	public void setLargeurColonne(JColonne colonne, int nouvelleLargeur) {
		colonne.setPreferredSize(new Dimension(nouvelleLargeur, JTableur2.HAUTEUR_ENTETE_COLONNE));
		colonne.setSize(nouvelleLargeur, JTableur2.HAUTEUR_ENTETE_COLONNE);

		ColonneHelper.getInstance(this).replacerListeColonne(colonne.getIndex() + 1);

		fillColonnesDroite();
		supprimerEnteteColonneDroite();

		updateUI();
	}

	public void setHauteurLigne(JLigne ligne, int nouvelleHauteur) {

		ligne.setSize(JTableur2.LARGEUR_NUMERO_LIGNE, nouvelleHauteur);

		LigneHelper.getInstance(this).replacerListeLigne(ligne.getIndex() + 1);

		fillLignesBas();
		supprimerEnteteLigneBas();

		updateUI();
	}

	/**
	 * Charge des colonnes � droite si besoin Utile dans le cas o� une colonne a
	 * �t� supprim�e, redimmensionn�e, si la fen�tre a �t� redimmensionn�e
	 */
	public void fillColonnesDroite() {
		int droiteDerniereColonne = LARGEUR_NUMERO_LIGNE;

		int numeroDerniereColonne = numeroCelluleGauche - 1;
		int indexDerniereColonne = -1;
		JColonne jDerniereColonne;
		if (listeColonne.size() != 0) {
			jDerniereColonne = listeColonne.get(listeColonne.size() - 1);
			droiteDerniereColonne = jDerniereColonne.getDroiteColonne();
			numeroDerniereColonne = jDerniereColonne.getNumero();
			indexDerniereColonne = jDerniereColonne.getIndex();
		}
		// rajoute les colonnes � droite si besoin
		while (droiteDerniereColonne < this.getWidth() && numeroDerniereColonne + 1 < modele.getNbColonnes()) {
			insererTitreDeColonne(numeroDerniereColonne + 1, numeroDerniereColonne + 1, indexDerniereColonne + 1);

			jDerniereColonne = listeColonne.get(listeColonne.size() - 1);
			droiteDerniereColonne = jDerniereColonne.getDroiteColonne();
		}

	}

	/**
	 * Charge des lignes � droite si besoin Utile dans le cas o� une ligne a �t�
	 * supprim�e, redimmensionn�e, si la fen�tre a �t� redimmensionn�e
	 */
	public void fillLignesBas() {
		int basDerniereLigne = HAUTEUR_ENTETE_COLONNE;

		int numeroDerniereLigne = numeroCelluleHaut - 1;
		int indexDerniereLigne = -1;
		JLigne jDerniereLigne;
		if (listeLigne.size() != 0) {
			jDerniereLigne = listeLigne.get(listeLigne.size() - 1);
			basDerniereLigne = jDerniereLigne.getBasLigne();
			numeroDerniereLigne = jDerniereLigne.getNumero();
			indexDerniereLigne = jDerniereLigne.getIndex();
		}
		// rajoute les colonnes � droite si besoin
		while (basDerniereLigne < this.getHeight() && numeroDerniereLigne + 1 < modele.getNbLignes()) {
			insererNumeroDeLigne(numeroDerniereLigne + 1, numeroDerniereLigne + 1, indexDerniereLigne + 1);

			jDerniereLigne = listeLigne.get(listeLigne.size() - 1);
			basDerniereLigne = jDerniereLigne.getBasLigne();
		}

	}

	/**
	 * supprime les colonnes de droite si elle sont enti�rement en dehors de
	 * l'�cran (par exemple si on scrolle � gauche)
	 */
	public void supprimerEnteteColonneDroite() {
		int gaucheDerniereColonne = LARGEUR_NUMERO_LIGNE;
		JColonne jDerniereColonne;
		if (listeColonne.size() == 0) {
			return;
		}
		jDerniereColonne = listeColonne.get(listeColonne.size() - 1);
		gaucheDerniereColonne = ColonneHelper.getInstance(this).getCoordonneeGauche(jDerniereColonne.getIndex());

		while (gaucheDerniereColonne > this.getWidth() && listeColonne.size() > 1) {
			supprimerTitreDeColonne(jDerniereColonne.getNumero(), jDerniereColonne.getNumero(), jDerniereColonne.getIndex());
			jDerniereColonne = listeColonne.get(listeColonne.size() - 1);
			gaucheDerniereColonne = ColonneHelper.getInstance(this).getCoordonneeGauche(jDerniereColonne.getIndex());
		}
	}

	/**
	 * supprime les lignes du bas si elle sont enti�rement en dehors de l'�cran
	 * (par exemple si on scrolle vers le haut)
	 */
	public void supprimerEnteteLigneBas() {
		int hautDerniereLigne = HAUTEUR_ENTETE_COLONNE;
		JLigne jDerniereLigne;
		if (listeLigne.size() == 0) {
			return;
		}
		jDerniereLigne = listeLigne.get(listeLigne.size() - 1);
		hautDerniereLigne = LigneHelper.getInstance(this).getCoordonneeHaut(jDerniereLigne.getIndex());

		while (hautDerniereLigne > this.getHeight() && listeLigne.size() > 1) {
			supprimerNumeroDeLigne(jDerniereLigne.getNumero(), jDerniereLigne.getNumero(), jDerniereLigne.getIndex());
			jDerniereLigne = listeLigne.get(listeLigne.size() - 1);
			hautDerniereLigne = LigneHelper.getInstance(this).getCoordonneeHaut(jDerniereLigne.getIndex());
		}
	}

	protected void supprimerTitreDeColonne(int numeroColonneMin, int numeroColonneMax, int indexMin) {
		ColonneHelper.getInstance(this).supprimerComposantTitreDeColonne(numeroColonneMin, numeroColonneMax, indexMin);

		// d�calle les colonnes suivantes si il y en a
		int nombreColonnesSupprimees = numeroColonneMax - numeroColonneMin + 1;
		renumeroterColonnes(indexMin, -nombreColonnesSupprimees);
		System.out.println("nombre de colonnes charg�es (apr�s suppression) : " + listeColonne.size());
	}

	protected void supprimerNumeroDeLigne(int numeroLigneMin, int numeroLigneMax, int indexMin) {
		LigneHelper.getInstance(this).supprimerComposantTitreDeLigne(numeroLigneMin, numeroLigneMax, indexMin);

		// d�calle les lignes suivantes si il y en a
		int nombreLignesSupprimees = numeroLigneMax - numeroLigneMin + 1;
		renumeroterLignes(indexMin, -nombreLignesSupprimees);
		System.out.println("nombre de lignes charg�es (apr�s suppression) : " + listeLigne.size());
	}

	private void renumeroterColonnes(int indexMin, int nombreColonnesDelta) {
		int indexADecaller = indexMin;
		while (indexADecaller < listeColonne.size()) {
			JColonne jColonne = listeColonne.get(indexADecaller);
			jColonne.setNumero(jColonne.getNumero() + nombreColonnesDelta);
			indexADecaller++;
		}
	}

	private void renumeroterLignes(int indexMin, int nombreLignesDelta) {
		int indexADecaller = indexMin;
		while (indexADecaller < listeLigne.size()) {
			JLigne jLigne = listeLigne.get(indexADecaller);
			jLigne.setNumero(jLigne.getNumero() + nombreLignesDelta);
			indexADecaller++;
		}
	}

	private void scrollerGauche(int nombreColonnes) {
		for (int i = 0; i < nombreColonnes; i++) {
			JColonne jColonne = new JColonne(modele, numeroCelluleGauche - 1, 0, LARGEUR_NUMERO_LIGNE, LARGEUR_DEFAUT_COLONNE, this);
			JTableur2.this.panelGrille.add(jColonne);
			listeColonne.add(0, jColonne);
			numeroCelluleGauche--;
		}

		// Inutile de r�indexer la colonne que l'in vient d'ins�rer, par contre
		// elle n'a jamais �t� plac�e
		ColonneHelper.getInstance(this).reindexerListeColonne(1);
		ColonneHelper.getInstance(this).replacerListeColonne(0);

		supprimerEnteteColonneDroite();
		updateUI();
	}

	private void scrollerHaut(int nombreLignes) {
		for (int i = 0; i < nombreLignes; i++) {
			JLigne jLigne = new JLigne(modele, numeroCelluleHaut - 1, 0, HAUTEUR_ENTETE_COLONNE, HAUTEUR_DEFAUT_LIGNE, this);
			JTableur2.this.panelGrille.add(jLigne);
			listeLigne.add(0, jLigne);
			numeroCelluleHaut--;
		}

		// Inutile de r�indexer la ligne que l'in vient d'ins�rer, par contre
		// elle n'a jamais �t� plac�e
		LigneHelper.getInstance(this).reindexerListeLigne(1);
		LigneHelper.getInstance(this).replacerListeLigne(0);

		supprimerEnteteLigneBas();
		updateUI();
	}

	private void scrollerDroite(int nombreColonnes) {
		// Supprime les colonnes de gauche
		ColonneHelper.getInstance(this).supprimerComposantTitreDeColonne(numeroCelluleGauche, numeroCelluleGauche + nombreColonnes - 1, 0);

		// D�cale les cellules restantes
		numeroCelluleGauche += nombreColonnes;

		ColonneHelper.getInstance(this).reindexerListeColonne(0);
		ColonneHelper.getInstance(this).replacerListeColonne(0);

		// Rajoute des colonnes � droite si besoin
		fillColonnesDroite();

		updateUI();
	}

	private void scrollerBas(int nombreLignes) {
		// Supprime les lignes de gauche
		LigneHelper.getInstance(this).supprimerComposantTitreDeLigne(numeroCelluleHaut, numeroCelluleHaut + nombreLignes - 1, 0);

		// D�cale les cellules restantes
		numeroCelluleHaut += nombreLignes;

		LigneHelper.getInstance(this).reindexerListeLigne(0);
		LigneHelper.getInstance(this).replacerListeLigne(0);

		// Rajoute des colonnes � droite si besoin
		fillLignesBas();

		updateUI();
	}

	// �v�nements
	@Override
	public void onColonneInsered(final int numero) {
		insererTitreDeColonne(numero, numero, numero - numeroCelluleGauche);
		JTableur2.this.updateUI();
	}

	@Override
	public void onColonneRemoved(final int numero) {
		supprimerTitreDeColonne(numero, numero, numero - numeroCelluleGauche);
		JTableur2.this.updateUI();
	}

	@Override
	public void onLigneInsered(final int numero) {
		insererNumeroDeLigne(numero, numero, numero - numeroCelluleHaut);
		JTableur2.this.updateUI();
	}

	@Override
	public void onLigneRemoved(final int numero) {
		supprimerNumeroDeLigne(numero, numero, numero - numeroCelluleHaut);
		JTableur2.this.updateUI();
	}
}
