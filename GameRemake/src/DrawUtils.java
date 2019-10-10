import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class DrawUtils 
{
	
	private DrawUtils() 
	{
		
	}
	// This is used to get the width and center it on the screen
	// checks how wide the message is and center that on the tile
	// This is used to center the text because it makes thigs easier on the tile class
	public static int getMessageWidth(String message, Font font, Graphics2D g) 
	{
		g.setFont(font);
		//choose the font then get the boundaries and sets the width accordingly
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(message, g);
		
		return (int)bounds.getWidth();
		
	}
	
	public static int getMessageHeight(String message, Font font, Graphics2D g) 
	{
		g.setFont(font);//padding is buit into the height of a font
		
		if(message.length() == 0) return 0;// if the length is 0 just return
		// textLayout is used to get the size and bound of a message. or text
		
		TextLayout tl = new TextLayout(message, font, g.getFontRenderContext());
		return (int) tl.getBounds().getHeight();
	}
}
