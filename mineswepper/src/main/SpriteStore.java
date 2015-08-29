package main;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteStore {

	private static SpriteStore single = new SpriteStore();
	
	public static SpriteStore get() {
		return single;
	}
	
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	public Sprite getSprite(String ref) {
		
		//If we have the sprite, return it
		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(ref);
		}
		
		//otherwise, go and grab it
		BufferedImage sourceImage = null;
		
		try{
			URL url = this.getClass().getClassLoader().getResource(ref);
			
			if( url == null) {
				fail("can't find ref: "+ref);
			}
			
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			fail("Failed to load: "+ref);
		}
		
		// create accelerated image of the right size to store sprite in
		
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);
		
		//Draw souce image into accelerated iamge
		image.getGraphics().drawImage(sourceImage, 0, 0, null);
		
		//create a sprite, and add it to the cache then return it
		
		Sprite sprite = new Sprite(image);
		sprites.put(ref, sprite);
		
		return sprite;
	}
	
	private void fail(String message){
		// dump message and exit
		
		System.err.println(message);
		System.exit(0);
	}
}
