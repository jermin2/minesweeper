package main;

import main.Board.PicOrder;

public class Tile {
	
	public static final int UNCLICKED = 0;
	public static final int CLICKED = 1;
	public static final int FLAG = 2;
	public static final int QUESTION = 3;
	int value = 0;
	int visible = Tile.UNCLICKED;
	PicOrder picture = PicOrder.BLANK;
	
	public static int uncoveredTiles = 0;
	
	public void updateVisible(int vis){
		
		if(vis == Tile.FLAG){
			picture = PicOrder.FLAG;
		}
		if(vis == Tile.UNCLICKED){
			picture = PicOrder.BLANK;
		}
		if(vis == Tile.CLICKED){
			if(this.visible==Tile.UNCLICKED)
				uncoveredTiles++;
			
			this.visible = vis;
			updateValue(this.value);
		}
		
		this.visible = vis;
	}
	
	public static void reset(){
		uncoveredTiles = 0;
	}
	
	public static int getUncoveredTiles(){
		return uncoveredTiles;
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