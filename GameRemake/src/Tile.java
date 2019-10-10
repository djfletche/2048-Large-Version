import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile {
	
	// public variables are used with the GameBoard so these need to be public
	public static final int WIDTH = 80;
	public static final int HEIGHT = 80;
	public static final int SLIDE_SPEED = 25;
	public static final int ARC_WIDTH = 20;
	public static final int ARC_HEIGHT = 20;
	
	// When using rounded squares you need to also have ARC width and height
	
	// Private variables are only  needed to know what the tile looks like.	

	private int value;
	private BufferedImage tileImage;
	private Color background;
	private Color text;
	private Font font;
	private Point slideTo;
	private int x;
	private int y;
	
	private boolean canCombine = true; // needs to be tree because if they start as false it will never combine **
	
	/*
	 * Here is a tile created
	 * The value is the numerical value of the tile
	 * the x and y are the positions of the tile
	 * we also needed to create the tile with DrawImage as a buffered image object
	 */
	public Tile(int value, int x, int y) {
		this.value = value;
		this.x = x;
		this.y = y;
		slideTo = new Point(x, y);
		tileImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		drawImage();
	}
	/**
	 * The DrawImage is used to set the cube colors and text colors that are contained on the shapes.
	 * the colors I used were put into RGB. Keeping the text color black while changing the shapes makes it 
	 * easy on the eyes yet stimulating. 
	 */
	
	private void drawImage() {
		
		Graphics2D g = (Graphics2D)tileImage.getGraphics();
		
		if(value == 2) {background = new Color(0xE06666);text = new Color(0x000000);}
		
		else if(value == 4) {background = new Color(0xE69138);text = new Color(0x000000);}
		
		else if(value == 8) {background = new Color(0xFDE5CD);text = new Color(0xffffff);}
		
		else if(value == 16) {background = new Color(0xf28007);text = new Color(0xffffff);}
		
		else if(value == 32) {background = new Color(0xFF9900);text = new Color(0xffffff);}
		
		else if(value == 64) {background = new Color(0x93C47D);text = new Color(0xffffff);}
		
		else if(value == 128) {background = new Color(0xF1C232);text = new Color(0xffffff);}
		
		else if(value == 256) {background = new Color(0x45818E);text = new Color(0xffffff);}
		
		else if(value == 512) {background = new Color(0xD9D2E9);text = new Color(0xffffff);}
		
		else if(value == 1024) {background = new Color(0x8E7CC3);text = new Color(0xffffff);}
		
		else if(value == 2048) {background = new Color(0xD5A6BD);text = new Color(0xffffff);}
		
		else {background = Color.black;text = Color.white;}
		
		g.setColor(new Color(0,0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(background);
		g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
		
		g.setColor(text);
		
		if(value <= 64) {
			font = Game.main.deriveFont(36f);
		}else {
			font = Game.main;
		}
		g.setFont(font);
		
		//setting the font and using the drawutil
		// since we are centering the value we divide by two so it is in the center
		
		int drawX = WIDTH/2 - DrawUtils.getMessageWidth("" + value, font, g)/2;
		int drawY = HEIGHT/2 + DrawUtils.getMessageHeight("" + value, font, g)/2;
		g.drawString("" + value, drawX, drawY);
		g.dispose();
	}
	
	public void update()
	{
		
	}
	
	public void render(Graphics2D g) {g.drawImage(tileImage, x, y, null);}
	
	public void setValue(int value) {this.value = value;drawImage();}
	
	public int getValue() {return value;}

	/*
	 * used to determine if the blocks can combine or not
	 */
	public boolean CanCombine() {return canCombine;}

	public void setCanCombine(boolean canCombine) {this.canCombine = canCombine;}
	/*
	 * This is used to decide if the point(block coordinate)
	 * can move or not
	 */

	public Point getSlideTo() {return slideTo;}

	public void setSlideTo(Point slideTo) {this.slideTo = slideTo;}

	/*
	 *Getters and setters are used to get the x and y values and return them 
	 */
	public int getX() {return x;}

	public void setX(int x) {this.x = x;}

	public int getY() {return y;}

	public void setY(int y) {this.y = y;}
}
