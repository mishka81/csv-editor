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
	int nombreCellulesCrees;

	Map<Integer, Integer> mapLargeurParNumeroColonne = new HashMap<Integer, Integer>();

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
						// l'ascenseur a été déplacé vers la droite
						scrollerDroite(difference);
					} else {
						scrollerGauche(-difference);
					}
					System.out.println("nombre de colonnes chargées : " + listeColonne.size());
					// }
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
						updateUI();
					}
				});

			}
		});
	}

	void setValeurMaximumHorizontalScrollbar() {
		this.scrollbarHorizontal.setMaximum(modele.getNbColonnes() + 1);
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
		int indexDerniereColonneInseree = ColonneHelper.getInstance(this).insererComposantTitreDeColonne(numeroColonneMin, numeroColonneMax, indexMin);

		// // Renumérote les colonnes suivantes
		int nombreColonnesInserees = numeroColonneMax - numeroColonneMin + 1;
		int indexADecaller = indexDerniereColonneInseree + 1;
		for (int i = indexADecaller; i < listeColonne.size(); i++) {
			JColonne jColonne = listeColonne.get(i);
			jColonne.setNumero(jColonne.getNumero() + nombreColonnesInserees);
		}

		setValeurMaximumHorizontalScrollbar();
		System.out.println("nombre de colonnes chargées (après insersion) : " + listeColonne.size());
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

		ColonneHelper.getInstance(this).replacerListeColonne(colonne.getIndex() + 1);

		fillColonnesDroite();
		supprimerEnteteColonneDroite();

		updateUI();
	}

	/**
	 * Charge des colonnes à droite si besoin Utile dans le cas où une colonne a
	 * été supprimée, redimmensionnée, si la fenêtre a été redimmensionnée
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
		// rajoute les colonnes à droite si besoin
		while (droiteDerniereColonne < this.getWidth() && numeroDerniereColonne + 1 < modele.getNbColonnes()) {
			insererTitreDeColonne(numeroDerniereColonne + 1, numeroDerniereColonne + 1, indexDerniereColonne + 1);

			jDerniereColonne = listeColonne.get(listeColonne.size() - 1);
			droiteDerniereColonne = jDerniereColonne.getDroiteColonne();
		}

	}

	/**
	 * supprime les colonnes de droite si elle sont entièrement en dehors de
	 * l'écran (par exemple si on scrolle à gauche)
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

	protected void supprimerTitreDeColonne(int numeroColonneMin, int numeroColonneMax, int indexMin) {
		ColonneHelper.getInstance(this).supprimerComposantTitreDeColonne(numeroColonneMin, numeroColonneMax, indexMin);

		// décalle les colonnes suivantes si il y en a
		int nombreColonnesSupprimees = numeroColonneMax - numeroColonneMin + 1;
		int indexADecaller = indexMin;
		while (indexADecaller < listeColonne.size()) {
			JColonne jColonne = listeColonne.get(indexADecaller);
			jColonne.setNumero(jColonne.getNumero() - nombreColonnesSupprimees);
			indexADecaller++;
		}
		System.out.println("nombre de colonnes chargées (après suppression) : " + listeColonne.size());
	}

	private void scrollerGauche(int nombreColonnes) {
		for (int i = 0; i < nombreColonnes; i++) {
			JColonne jColonne = new JColonne(modele, numeroCelluleGauche - 1, 0, LARGEUR_NUMERO_LIGNE, LARGEUR_DEFAUT_COLONNE, this);
			JTableur2.this.panelGrille.add(jColonne);
			listeColonne.add(0, jColonne);
			numeroCelluleGauche--;
		}

		// Inutile de réindexer la colonne que l'in vient d'insérer, par contre
		// elle n'a jamais été placée
		ColonneHelper.getInstance(this).reindexerListeColonne(1);
		ColonneHelper.getInstance(this).replacerListeColonne(0);

		supprimerEnteteColonneDroite();
		updateUI();
	}

	private void scrollerDroite(int nombreColonnes) {
		// Supprime les colonnes de gauche
		ColonneHelper.getInstance(this).supprimerComposantTitreDeColonne(numeroCelluleGauche, numeroCelluleGauche + nombreColonnes - 1, 0);

		// Décale les cellules restantes
		numeroCelluleGauche += nombreColonnes;

		ColonneHelper.getInstance(this).reindexerListeColonne(0);
		ColonneHelper.getInstance(this).replacerListeColonne(0);

		// Rajoute des colonnes à droite si besoin
		fillColonnesDroite();

		updateUI();
	}

	// évènements
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
}
