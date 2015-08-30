package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Mine extends JFrame{

	private final int FRAME_WIDTH = 320;
	private final int FRAME_HEIGHT = 300;
	
	private final JLabel statusbar;
	private final JLabel timerlbl;
	private final JLabel minesLeft;
	
	private Board board;
	
	public Mine() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("MineSweeper");
		
		
		statusbar = new JLabel("");
		timerlbl = new JLabel("00", JLabel.LEFT);
		timerlbl.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		minesLeft = new JLabel("Mines Left = 1");
		
		board = new Board(statusbar, timerlbl, minesLeft);

		
		JButton button = new JButton ("Reset");
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e){
				board.reset();
			}
		});
		JPanel panel = new JPanel();
		panel.add(timerlbl, BorderLayout.WEST);
		panel.add(button, BorderLayout.CENTER);
		panel.add(minesLeft, BorderLayout.EAST);
		
		add(panel, BorderLayout.NORTH);

		add(statusbar, BorderLayout.SOUTH);
		add(board);
		
		setup(0,0,0);


	}
	public void setup(int x, int y, int size){
		setResizable(true);
		
		pack();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame ex = new Mine();
				ex.setVisible(true);
				//Game g = new Game();
				//g.setup();
				//g.DEBUG_printTiles();
			}
		});
	}
}
