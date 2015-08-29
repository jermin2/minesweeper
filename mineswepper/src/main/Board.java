package main;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import main.Game.Tile;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

public class Board extends JPanel{

	private JLabel statusbar;
	private JLabel timerlbl;
	
	Timer update_timer;
	//private Image image;
	
	HashMap<PicOrder, Image> images;
	public int PIC_SIZE = 30;
	public int BORDER_SIZE = 3;
	
	public int GAME_WIDTH = 10;
	public int GAME_HEIGHT = 8;
	public int GAME_MINES = 5;
	
	private Game game;
	
	public enum PicOrder 
	{
		BLANK(0), FLAG(1), MINE(2), BLANK_PR(3), ONE(4), TWO(5), THREE(6), FOUR(7), FIVE(8), SIX(9), SEVEN(10), EIGHT(11);
		
		private final int mask;
		
		private PicOrder(int mask) {
			this.mask = mask;
		}
		
		public int getMask(){
			return mask;
		}
	};
	
	public void setupBoard(int width, int height){
		setSize(width*PIC_SIZE, height*PIC_SIZE);
		
	}
	
	public Board(JLabel statusbar, JLabel timerlbl){
		this.statusbar = statusbar;
		this.timerlbl = timerlbl;
		
		setupBoard(GAME_WIDTH, GAME_HEIGHT);
		
		setDoubleBuffered(true);
		loadImage();
		
		game = new Game();
		game.setup(GAME_WIDTH, GAME_HEIGHT, GAME_MINES);
		game.state = Game.State.Setup;
		
		update_timer = new Timer(100, new TimerListener());
		
		addMouseListener(new MinesAdapter());
		statusbar.setText("Loaded");
		
		repaint();
	}
	
	public void loadImage(){
		
		//SpriteStore.get().getSprite("minesweeper_tiles.jpg");
		
		int pic_size = 30;
		
		int num_pic_width = 4;
		int num_pic_height = 3;
		URL url = getClass().getResource("minesweeper_tiles.jpg");
		
		BufferedImage sourceImage = null;
		
		//System.out.println(url.toString());
		
		try {
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
		int pic_width = sourceImage.getWidth() / num_pic_width;
		int pic_height = sourceImage.getHeight() / num_pic_height;
		
		//Graphics acceleration
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		images = new HashMap<PicOrder, Image>();
		//Image[] images = new Image[num_pic_width*num_pic_height];
		
		//Create a list of the enums
		PicOrder[] val = PicOrder.values();
		
		int index = 0;
		for(int y=0;y<num_pic_height;y++)
			for(int x=0;x<num_pic_width;x++){
				Image img = gc.createCompatibleImage(pic_width, pic_height,Transparency.BITMASK);
				img.getGraphics().drawImage(sourceImage, 0, 0, pic_size, pic_size, x*pic_width, y*pic_height, (x+1)*pic_width, (y+1)*pic_height, null);

				//Place the image in the images repository
				images.put(val[index++], img);			
			}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);


			for (int x = 0; x<GAME_WIDTH;x++)
				for(int y=0; y<GAME_HEIGHT;y++){
					g.drawImage(images.get(game.tiles[x][y].picture), x*PIC_SIZE, y*PIC_SIZE, this);
					//g.drawImage(images.get(PicOrder.BLANK_PR), x*PIC_SIZE, y*PIC_SIZE, this);
				}

	}
	
	public void startGame(){
		
		update_timer.restart();
		update_timer.start();
		game.startGame();
	}
	
	public void stopGame(){
		update_timer.stop();
	}
	
	class TimerListener implements ActionListener {
		
		DecimalFormat f = new DecimalFormat("###.#");
		public void actionPerformed(ActionEvent e){
						
			String ms = f.format(game.get_elapsed_time());
			timerlbl.setText(ms);

		}
	}
	
	class MinesAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e){
			
			if (!game.isRunning())
				startGame();
			statusbar.setText(e.getX() + " " + e.getY());
			int x = e.getX()/PIC_SIZE;
			int y = e.getY()/PIC_SIZE;
			game.click(x, y);
			
			repaint();
			//Find the tile that was clicked
		}
	}
}
