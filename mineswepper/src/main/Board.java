package main;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel{

	private JLabel statusbar;
	
	public Board(JLabel statusbar){
		this.statusbar = statusbar;
		
		statusbar.setText("Loaded");
		
		addMouseListener(new MinesAdapter());
	}
	
	
	class MinesAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e){
			statusbar.setText(e.getX() + " " + e.getY());
		}
	}
}
