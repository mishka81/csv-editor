package gui;

import gui2.JTableur2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import modele.modele2.Cellule;
import modele.modele2.CelluleListener;
import modele.modele2.TableurModele3;

public class JCellule extends JPanel implements CelluleListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int MODE_CONSULTATION = 0;
	private static final int MODE_MODIFICATION = 1;
	private int mode = MODE_CONSULTATION;

	private JColonne colonne;
	private JLigne ligne;

	private JTextPane jLabelContenu;
	private JTextArea jTextContenu;

	Cellule celluleCorrespondante;

	JTableur2 tableur;

	private boolean selectionnee;

	private TableurModele3 modele;

	public boolean isSelectionnee() {
		return selectionnee;
	}

	public void setSelectionnee(boolean selectionnee) {
		this.selectionnee = selectionnee;
		this.getjLabelContenu().requestFocus();
	}

	public JCellule(JTableur2 tableur, JColonne colonne, JLigne ligne, Cellule cellule, TableurModele3 modele) {
		super();
		this.celluleCorrespondante = cellule;
		updateCelluleColor();
		this.modele = modele;

		this.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.setLayout(new BorderLayout());
		this.colonne = colonne;
		this.ligne = ligne;
		this.tableur = tableur;

		setBounds(colonne.getX(), ligne.getY(), colonne.getWidth(), ligne.getHeight());
		this.setSize(new Dimension(colonne.getWidth(), ligne.getHeight()));
		this.setPreferredSize(new Dimension(colonne.getWidth(), ligne.getHeight()));
		setModeConsultation();
	}

	private void updateCelluleColor() {
		if (celluleCorrespondante == null) {
			this.setBackground(new Color(220, 220, 220));
		} else {
			this.setBackground(Color.WHITE);
		}
	}

	public JTextPane getjLabelContenu() {
		if (jLabelContenu == null) {
			jLabelContenu = new JTextPane();
			jLabelContenu.setOpaque(false);
			jLabelContenu.setFocusable(true);
			jLabelContenu.setEditable(false);
			jLabelContenu.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "none");
			jLabelContenu.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "none");
			jLabelContenu.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "none");
			jLabelContenu.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "none");
			jLabelContenu.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if (e.getClickCount() == 1) {
						JCellule.this.tableur.setCelluleSelectionne(JCellule.this);
					} else if (e.getClickCount() == 2) {
						setModeEdition();
					}
					e.consume();
				}
			});

			jLabelContenu.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "editionCellule");
			jLabelContenu.getActionMap().put("editionCellule", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setModeEdition();
				}
			});
		}
		return jLabelContenu;
	}

	public JTextArea getjTextContenu() {
		if (jTextContenu == null) {
			jTextContenu = new JTextArea();
			jTextContenu.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					super.focusLost(e);
					setModeConsultation();
				}
			});

			jTextContenu.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "validationCellule");
			jTextContenu.getActionMap().put("validationCellule", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					validerSaise();
				}
			});

			jTextContenu.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "annulationCellule");
			jTextContenu.getActionMap().put("annulationCellule", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setModeConsultation();
				}
			});

			jTextContenu.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.ALT_MASK), "sauterLigne");
			jTextContenu.getActionMap().put("sauterLigne", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					jTextContenu.setText(jTextContenu.getText() + "\n");
				}
			});

			// jTextContenu.addKeyListener(new KeyAdapter() {
			// @Override
			// public void keyPressed(KeyEvent e) {
			// super.keyPressed(e);
			// if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiers() ==
			// KeyEvent.ALT_MASK) {
			// jTextContenu.setText(jTextContenu.getText() + "\n");
			// } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// validerSaise();
			// } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			// setModeConsultation();
			// } else {
			// super.keyPressed(e);
			// }
			// e.consume();
			// }
			//
			//
			// });
		}
		return jTextContenu;
	}

	public void validerSaise() {
		if (this.mode == MODE_MODIFICATION) {
			if (celluleCorrespondante == null) {
				genererCellule();
			}
			celluleCorrespondante.setContenu(jTextContenu.getText());
			setModeConsultation();
		}
	}

	private void genererCellule() {
		modele.setValeur(this.getLigne().getNumero(), this.getColonne().getNumero(), "");
		updateCelluleWithModele();
	}

	private void updateCelluleWithModele() {
		if (celluleCorrespondante != null) {
			// La cellule est déjà créée
			return;
		}
		celluleCorrespondante = modele.getCellule(ligne.getNumero(), colonne.getNumero());
		JCellule celluleOnTheLeft = ligne.getCelluleOnTheLeft(this);
		updateCelluleColor();
		if (celluleOnTheLeft != null) {
			celluleOnTheLeft.updateCelluleWithModele();
		}

	}

	public void setModeConsultation() {
		this.mode = MODE_CONSULTATION;
		// SwingUtilities.invokeLater(new Runnable() {
		//
		// @Override
		// public void run() {
		JCellule.this.remove(getjTextContenu());
		JCellule.this.add(getjLabelContenu());
		if (celluleCorrespondante != null) {
			getjLabelContenu().setText(celluleCorrespondante.getContenu());
		}
		getjLabelContenu().requestFocus();
		JCellule.this.updateUI();
		// }
		// });
	}

	public void setModeEdition() {
		this.mode = MODE_MODIFICATION;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JCellule.this.remove(getjLabelContenu());
				JCellule.this.add(getjTextContenu());
				if (celluleCorrespondante != null) {
					getjTextContenu().setText(celluleCorrespondante.getContenu());
				}
				getjTextContenu().requestFocus();
				JCellule.this.updateUI();
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (isSelectionnee()) {
			g.setColor(Color.BLUE);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		} else {
			g.setColor(Color.LIGHT_GRAY);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		}
	}

	public JColonne getColonne() {
		return colonne;
	}

	public void setColonne(JColonne colonne) {
		this.colonne = colonne;
	}

	public JLigne getLigne() {
		return ligne;
	}

	public void setLigne(JLigne ligne) {
		this.ligne = ligne;
	}

	@Override
	public void onContenuChanged(String oldValue, String newValue) {
		// this.celluleCorrespondante.setContenu(newValue);
		// if (oldValue == null && newValue != null) {
		// this.setBackground(Color.WHITE);
		// }
	}
}
