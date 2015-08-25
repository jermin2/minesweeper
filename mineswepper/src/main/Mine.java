package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Mine extends JFrame{

	private final int FRAME_WIDTH = 250;
	private final int FRAME_HEIGHT = 290;
	
	private final JLabel statusbar;
	
	public Mine() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("MineSweeper");
		
		statusbar = new JLabel("");
		add(statusbar, BorderLayout.SOUTH);
		add(new Board(statusbar));
		
		setResizable(false);
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame ex = new Mine();
				ex.setVisible(true);
			}
		});
	}
}
