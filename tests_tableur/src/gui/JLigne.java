package gui;

import gui.action.InsererLigneAction;
import gui.action.SupprimerLigneAction;
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
import javax.swing.SwingUtilities;

import modele.modele2.TableurModele3;

public class JLigne extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numero;
	private int index;

	private JLabel jLabelNom;
	private JPanel jPanelPoignee = new JPanel();

	private Map<Integer, JCellule> mapCelluleParIndexColonne = new HashMap<Integer, JCellule>();

	private JMenuItem insererLigne = new JMenuItem();
	private JMenuItem supprimerLigne = new JMenuItem();
	private JPopupMenu menuPopup = new JPopupMenu();
	private JTableur2 jTableur;

	public JLigne(TableurModele3 modele, int numero, int index, int y, int hauteur, final JTableur2 jTableur) {
		super();
		this.jTableur = jTableur;
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(223, 227, 232));

		this.numero = numero;
		this.index = index;
		this.setLocation(1, y);
		this.setSize(JTableur2.LARGEUR_NUMERO_LIGNE, hauteur);

		jLabelNom = new JLabel(String.valueOf(numero));
		this.add(jLabelNom, BorderLayout.CENTER);

		jPanelPoignee.setBackground(Color.BLUE);
		jPanelPoignee.setPreferredSize(new Dimension(0, JTableur2.HAUTEUR_POIGNEE_ENTETE_LIGNE));
		this.add(jPanelPoignee, BorderLayout.SOUTH);
		jPanelPoignee.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));

		jPanelPoignee.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(final MouseEvent e) {
				if (e.getY() != 0) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							int nouvelleHauteur = getHeight() + e.getY();
							if (nouvelleHauteur > 20) {
								jTableur.setHauteurLigne(JLigne.this, nouvelleHauteur);
							}
						}

					});
				}
			}
		});

		// popup menu
		insererLigne.setAction(new InsererLigneAction(modele, this));
		insererLigne.setText("Insérer une ligne");
		menuPopup.add(insererLigne);

		supprimerLigne.setAction(new SupprimerLigneAction(modele, this));
		supprimerLigne.setText("Supprimer une ligne");
		menuPopup.add(supprimerLigne);
		this.setComponentPopupMenu(menuPopup);
	}

	public int getBasLigne() {
		return getY() + getHeight();
	}

	public JCellule getCelluleOnTheRight(JCellule cellule) {
		int indexColonne = cellule.getColonne().getIndex() + 1;
		if (mapCelluleParIndexColonne.containsKey(indexColonne)) {
			return mapCelluleParIndexColonne.get(indexColonne);
		}
		return cellule;
	}

	public JCellule getCelluleOnTheLeft(JCellule cellule) {
		int indexColonne = cellule.getColonne().getIndex() - 1;
		if (mapCelluleParIndexColonne.containsKey(indexColonne)) {
			return mapCelluleParIndexColonne.get(indexColonne);
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
		mapCelluleParIndexColonne.put(cellule.getColonne().getIndex(), cellule);
	}

	public Collection<JCellule> getCollectionCellule() {
		return mapCelluleParIndexColonne.values();
	}

	@Override
	public String toString() {
		return "Ligne numéro " + numero + " à l'index " + index + "(x : " + getX() + ", y : " + getY() + ", width : " + getWidth() + ", height : " + getHeight();
	}

	@Override
	public void setSize(Dimension d) {
		super.setPreferredSize(d);
		super.setSize(d);
		updateCelluleSize(Double.valueOf(d.getHeight()).intValue());
	}

	@Override
	public void setSize(int width, int height) {
		super.setPreferredSize(new Dimension(width, height));
		super.setSize(width, height);
		updateCelluleSize(height);
	}

	private void updateCelluleSize(int height) {
		Collection<JCellule> values = mapCelluleParIndexColonne.values();
		for (JCellule jCellule : values) {
			Dimension d = new Dimension(jCellule.getWidth(), height);
			jCellule.setPreferredSize(d);
			jCellule.setSize(d);
		}
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		jTableur.springLayout.putConstraint(SpringLayout.NORTH, this, y, SpringLayout.NORTH, jTableur);
		updateCelluleLocation(y);
	}

	@Override
	public void setLocation(Point p) {
		super.setLocation(p);
		updateCelluleLocation(p.y);
	}

	private void updateCelluleLocation(int y) {
		Collection<JCellule> values = mapCelluleParIndexColonne.values();
		for (JCellule jCellule : values) {
			Point d = new Point(jCellule.getLocation().x, y);
			jCellule.setLocation(d);
			jTableur.springLayout.putConstraint(SpringLayout.NORTH, jCellule, y, SpringLayout.NORTH, jTableur);
		}
	}
}
