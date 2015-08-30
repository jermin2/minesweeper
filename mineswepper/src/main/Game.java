package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import main.Board.PicOrder;



public class Game {

	public enum State {Reset,Setup,Running,Win,Lose,End};
	
	public State state;
	
	private int BOARD_WIDTH = 10;
	private int BOARD_HEIGHT = 20;
	private int NUM_MINES = 15;
	
	private int mines_remaining = 5;
	
	public long start_time;

	public Tile[][] tiles;
	
	public Game(){
		this.state = State.Reset;
	}
	
	public void setState(State newState){
		this.state = newState;
	}
	
	public void run(){
		switch (state){
		case Reset:
			this.state = State.Setup;
			break;
			
		case Setup:
			//setup(BOARD_WIDTH, BOARD_HEIGHT);
			break;
		
		case Running:
			running();
			break;
			
		case End:
			end();
			break;
		}
	}
	
	private boolean checkBounds(int x, int y){
		if(x<0 || y < 0 || x >=BOARD_WIDTH || y >= BOARD_HEIGHT)
			return false;
		else
			return true;
	}
	
	/** Add a mine to the board, and increment the surrounding neighbours
	 * 
	 * @param x
	 * @param y
	 * @return true if added mine
	 */
	private boolean addMine(int x, int y){
		
		if(checkBounds(x,y) == false)
			return false;
		
		Tile t = tiles[x][y];
		
		if(t.value == -1)
			return false;
		
		t.updateValue(-1);
		
		//Add 1 to the value of the neighbours
		for (int f=-1;f<=1;f++)
			for (int g=-1;g<=1;g++){
				
				//Check the neighbours are within the bounds
				if(checkBounds(x+f, y+g)){
					Tile neighbour = tiles[x+f][y+g];
					
					//Check neighbour is not a mine
					if(neighbour.value != -1)
						neighbour.updateValue(neighbour.value + 1);
				}
			}
		
		return true;
	}
	
	public void startGame(){
		state = State.Running;
		start_time = System.currentTimeMillis();
	}
	
	public void stopGame(){
		//show all the tiles
		for (int x=0;x<BOARD_WIDTH;x++)
			for (int y=0;y<BOARD_HEIGHT;y++){
				tiles[x][y].updateVisible(Tile.CLICKED);
			}
		
		Tile.reset();
		state = State.End;
	}
	
	public void reset(){
		state = State.Reset;
		stopGame();
		setup();
	}
	
	public double get_elapsed_time(){
		long cur_time = System.currentTimeMillis() - start_time;
		return cur_time/1000.0;
	}
	
	public boolean isRunning(){
		return state == State.Running;
	}
	
	public boolean isEnded(){
		if(state == State.Lose || state == State.Win || state == State.End)
			return true;
		return false;
	}
	
	public int getNumMinesLeft(){
		return mines_remaining;
	}
	/**
	 * Setup using the default values
	 */
	public void setup(){
		setup(BOARD_WIDTH, BOARD_HEIGHT, NUM_MINES);
	}
	public void setup(int width, int height, int num_mines){
		state = State.Setup;
		
		BOARD_WIDTH = width;
		BOARD_HEIGHT = height;
		
		tiles = new Tile[BOARD_WIDTH][BOARD_HEIGHT];
		
		//initialize tiles
		for (int x=0;x<BOARD_WIDTH;x++)
			for (int y=0;y<BOARD_HEIGHT;y++){
				tiles[x][y] = new Tile();
			}
		
		//Randomise mines
		int i = 0;
		while (i < num_mines){
			
			//Get a random location
			int t_x = (int) (BOARD_WIDTH*Math.random());
			int t_y = (int) (BOARD_HEIGHT*Math.random());
			
			//If we failed to add a mine, then keep looking
			if (addMine(t_x,t_y) == true){
				i = i+ 1;
			}
		}		
		
		mines_remaining = NUM_MINES;
	}
	
	public boolean isSetup(){
		return state == State.Setup;
	}
	
	
	public void right_click(int x, int y){
		if (this.state == State.Running){
			Tile clicked_tile = tiles[x][y];
			clicked_tile.updateVisible(Tile.FLAG);
			mines_remaining--;
		}
	}
	
	private void lose(){
		JOptionPane.showMessageDialog(null, "You lose!");
		stopGame();
		System.out.println("you lose");
	}
	
	private void win(){
		JOptionPane.showMessageDialog(null, "You win!   Time: "+ get_elapsed_time());
		stopGame();
	}
	
	public void click(int x, int y){
		
		if (this.state == State.Running){
			Tile clicked_tile = tiles[x][y];
			
			//If we clicked a mine, then we lose
			if(clicked_tile.value == -1){
				clicked_tile.updateVisible(Tile.CLICKED);
				lose();
			}
			
			//If we clicked a tile with a number
			if(clicked_tile.value > 0){
				clicked_tile.updateVisible(Tile.CLICKED);
			}
			
			//If we clicked an empty box, we want to uncover all the other empty boxes around it
			if(clicked_tile.value == 0){
				clicked_tile.updateVisible(Tile.CLICKED);
				
				//Check neighbours for empty boxes
				for (int f=-1;f<=1;f++)
					for (int g=-1;g<=1;g++){
						
						
						//Check the neighbours are within the bounds
						if(checkBounds(x+f, y+g)){
							Tile neighbour = tiles[x+f][y+g];
							
							//don't want to click "flags"
							if (neighbour.visible == Tile.UNCLICKED){
								//Display the neighbours tile
								if(neighbour.value != -1)
									neighbour.updateVisible(Tile.CLICKED);
								
								//if the neighbour is a zero, call this recursively
								if(neighbour.value == 0)
									this.click(x+f, y+g);
							}
						}
					}
				
			}
			
			//Check for win condition
			//total tiles - num mines = Tile.getUncoveredTiles()
			if(BOARD_WIDTH*BOARD_HEIGHT - Tile.getUncoveredTiles() <= this.NUM_MINES){

				win();
			}
		}
	}
	
	public void DEBUG_printTiles(){
		for (int x=0;x<BOARD_WIDTH;x++){
			
			String line_text = "";
			for (int y=0;y<BOARD_HEIGHT;y++){
				Tile t= tiles[x][y];
				
				line_text = line_text.concat(", "+t.value);
				
			}
			System.out.println(line_text);
		}
	}
	
	private void running(){
		
	}
	
	private void end(){
		
	}
	
	
	
}
