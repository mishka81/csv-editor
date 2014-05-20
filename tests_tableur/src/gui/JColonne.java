package gui;

import gui.action.InsererColonneAction;
import gui.action.SupprimerColonneAction;
import gui2.JTableur2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import modele.modele2.TableurModele3;

public class JColonne extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numero;
	private int index;

	private JLabel jLabelNom;
	private JPanel jPanelPoignee = new JPanel();

	private Map<Integer, JCellule> mapCelluleParIndexLigne = new HashMap<Integer, JCellule>();

	private JMenuItem insererColonne = new JMenuItem();
	private JMenuItem supprimerColonne = new JMenuItem();
	private JPopupMenu menuPopup = new JPopupMenu();
	private final JTableur2 jTableur;

	public JColonne(TableurModele3 modele, int numero, int index, int x, int largeur, final JTableur2 jTableur) {
		super();
		this.jTableur = jTableur;
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(223, 227, 232));

		this.numero = numero;
		this.index = index;
		this.setLocation(x, 1);
		this.setSize(largeur, JTableur2.HAUTEUR_ENTETE_COLONNE);
		this.setPreferredSize(new Dimension(largeur, JTableur2.HAUTEUR_ENTETE_COLONNE));

		jLabelNom = new JLabel(String.valueOf(numero));
		jLabelNom = new JLabel(String.valueOf(numero), SwingConstants.CENTER);
		this.add(jLabelNom, BorderLayout.CENTER);

		jPanelPoignee.setBackground(Color.BLUE);
		jPanelPoignee.setPreferredSize(new Dimension(JTableur2.LARGEUR_POIGNEE_ENTETE_COLONNE, 0));
		this.add(jPanelPoignee, BorderLayout.EAST);
		jPanelPoignee.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));

		jPanelPoignee.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(final MouseEvent e) {
				if (e.getX() != 0) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							int nouvelleLargeur = getWidth() + e.getX();
							if (nouvelleLargeur > 20) {
								jTableur.setLargeurColonne(JColonne.this, nouvelleLargeur);
							}
						}

					});
				}
			}
		});

		// popup menu
		insererColonne.setAction(new InsererColonneAction(modele, this));
		insererColonne.setText("Insérer une colonne");
		menuPopup.add(insererColonne);

		supprimerColonne.setAction(new SupprimerColonneAction(modele, this));
		supprimerColonne.setText("Supprimer une colonne");
		menuPopup.add(supprimerColonne);
		this.setComponentPopupMenu(menuPopup);
	}

	public int getDroiteColonne() {
		return getX() + getWidth();
	}

	public JCellule getCelluleOnTheBottom(JCellule cellule) {
		int indexLigne = cellule.getLigne().getIndex() + 1;
		if (mapCelluleParIndexLigne.containsKey(indexLigne)) {
			return mapCelluleParIndexLigne.get(indexLigne);
		}
		return cellule;
	}

	public JCellule getCelluleOnTheTop(JCellule cellule) {
		int indexLigne = cellule.getLigne().getIndex() - 1;
		if (mapCelluleParIndexLigne.containsKey(indexLigne)) {
			return mapCelluleParIndexLigne.get(indexLigne);
		}
		return cellule;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
		jLabelNom.setText(String.valueOf(numero));
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void addCellule(JCellule cellule) {
		mapCelluleParIndexLigne.put(cellule.getLigne().getIndex(), cellule);
	}

	public Collection<JCellule> getCollectionCellule() {
		return mapCelluleParIndexLigne.values();
	}

	@Override
	public String toString() {
		return "Colonne numéro " + numero + " à l'index " + index + "(x : " + getX() + ", y : " + getY() + ", width : " + getWidth() + ", height : " + getHeight();
	}

	@Override
	public void setSize(Dimension d) {
		super.setPreferredSize(d);
		super.setSize(d);
		updateCelluleSize(Double.valueOf(d.getWidth()).intValue());
	}

	@Override
	public void setSize(int width, int height) {
		super.setPreferredSize(new Dimension(width, height));
		super.setSize(width, height);
		updateCelluleSize(width);
	}

	private void updateCelluleSize(int width) {
		Collection<JCellule> values = mapCelluleParIndexLigne.values();
		for (JCellule jCellule : values) {
			Dimension d = new Dimension(width, jCellule.getHeight());
			jCellule.setPreferredSize(d);
			jCellule.setSize(d);
		}
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		jTableur.springLayout.putConstraint(SpringLayout.NORTH, this, y, SpringLayout.NORTH, jTableur);
		updateCelluleLocation(x);
	}

	@Override
	public void setLocation(Point p) {
		super.setLocation(p);
		updateCelluleLocation(p.x);
	}

	private void updateCelluleLocation(int x) {
		Collection<JCellule> values = mapCelluleParIndexLigne.values();
		for (JCellule jCellule : values) {
			Point d = new Point(x, jCellule.getLocation().y);
			jCellule.setLocation(d);
			jTableur.springLayout.putConstraint(SpringLayout.WEST, jCellule, x, SpringLayout.WEST, jTableur);
		}
	}
}
