package main;

import main.Board.PicOrder;



public class Game {

	public enum State {Reset,Setup,Running,Win,Lose,End};
	
	public State state;
	
	private int BOARD_WIDTH = 10;
	private int BOARD_HEIGHT = 20;
	private int NUM_MINES = 20;
	
	private int mines_remaining = 5;

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
	
	/**
	 * Setup using the default values
	 */
	public void setup(){
		setup(BOARD_WIDTH, BOARD_HEIGHT, NUM_MINES);
	}
	public void setup(int width, int height, int num_mines){
		
		BOARD_WIDTH = width;
		BOARD_HEIGHT = height;
		
		tiles = new Tile[BOARD_WIDTH][BOARD_HEIGHT];
		
		//initialize tiles
		for (int x=0;x<BOARD_WIDTH;x++)
			for (int y=0;y<BOARD_HEIGHT;y++){
				tiles[x][y] = new Tile();
			}
		
		//Randomise mines
		for (int i=0;i<num_mines;i++){
			
			//Get a random location
			int t_x = (int) (BOARD_WIDTH*Math.random());
			int t_y = (int) (BOARD_HEIGHT*Math.random());
			
			//If we failed to add a mine, then keep looking
			if (addMine(t_x,t_y) == false){
				i = i+ 1;
			}
		}		
		
		mines_remaining = NUM_MINES;
	}
	
	
	public void click(int x, int y){
		
		if (this.state == State.Running){
			Tile clicked_tile = tiles[x][y];
			
			//If we clicked a mine, then we lose
			if(clicked_tile.value == -1)
				this.state = State.Lose;
			
			//If we clicked a tile with a number
			if(clicked_tile.value > 0){
				clicked_tile.updateVisible(Tile.CLICKED);;
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
	
	
	public class Tile {
		
		public static final int UNCLICKED = 0;
		public static final int CLICKED = 1;
		public static final int FLAG = 2;
		public static final int QUESTION = 3;
		int value = 0;
		int visible = Tile.CLICKED;
		PicOrder picture = PicOrder.BLANK;
		
		
		public void updateVisible(int vis){
			this.visible = vis;
			if(visible == Tile.FLAG){
				picture = PicOrder.FLAG;
			}
			if(visible == Tile.UNCLICKED){
				picture = PicOrder.BLANK;
			}
			if(visible == Tile.CLICKED){
				updateValue(this.value);
			}
		}
		public void updateValue(int val){
			this.value = val;
			
			if(this.visible == Tile.CLICKED){		
				switch (value){
				case 0:
					picture = PicOrder.BLANK_PR;
					break;
				case 1:
					picture = PicOrder.ONE;
					break;
				case 2:
					picture = PicOrder.TWO;
					break;
				case 3:
					picture = PicOrder.THREE;
					break;
				case 4:
					picture = PicOrder.FOUR;
					break;
				case 5:
					picture = PicOrder.FIVE;
					break;
				case 6:
					picture = PicOrder.SIX;
					break;
				case 7:
					picture = PicOrder.SEVEN;
					break;
				case 8:
					picture = PicOrder.EIGHT;
					break;
				case -1:
					picture = PicOrder.MINE;
					break;
				}
			}
		}
	}
}
