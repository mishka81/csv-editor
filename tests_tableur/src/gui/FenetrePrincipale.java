package gui;

import gui2.JTableur2;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FenetrePrincipale extends JFrame{

	public FenetrePrincipale() {
		super();
		
		this.setPreferredSize(new Dimension(640,480));
		
		this.getContentPane().add(new JTableur2());
		
		
		this.pack();
		this.setLocationRelativeTo(null);
//		this.getRootPane().updateUI();
//		this.getRootPane().update(this.getRootPane().getGraphics());
		
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
	}

	
	
}
