
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener, Runnable {
	
	/**
	 * We are setting up the game which is java's version of 
	 * Javascript in my opinion. Adding listeners and starting the game while
	 * selecting the font we have the set up  that we need to make the game move.
	 */

	public static final int WIDTH = 600;//screen width
	public static final int HEIGHT = 600;//screen height
	
	public static final Font main= new Font("Times New Roman", Font.PLAIN,32 );
	private Thread game;
	private boolean running;
	
	// double buffering in a sense to reduce flickering 
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private GameBoard board;
	
	public Game() 
	
	{
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		
		board = new GameBoard(WIDTH / 2 - GameBoard.BOARD_WIDTH / 2, HEIGHT - GameBoard.BOARD_HEIGHT - 10);
	}
	
	private void update() 
	
	{
		board.update();
		Keyboard.update();
	}
	
	/**
	 * The purpose of the render is to get the graphics of the game
	 * We are going to use this with the GUI to help create what we want
	 * the set up to look like.
	 */
	private void render() 
	
	{
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		board.render(g);
		g.dispose();
		
		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		
	}
	
	/**
	 * This is the game loop that creates the timer for the game
	 * When the Game is running we want to keep track of the time.
	 */
	@Override
	public void run() {
		
		int fps = 0; 
		int updates = 0;
		
		long fpsTimer = System.currentTimeMillis();
		double nsPerUpdate = 1000000000.0 / 60;//calculating the nanoseconds in between updates
		
	
		double then = System.nanoTime();// checks the update speed
		double unprocessed = 0;// how many updates we would need to do
		
		while(running) //while the game is running we need to get the times we need to update based on how many seconds has passed
		{
			boolean shouldRender = false;
			double now = System.nanoTime();
			unprocessed += (now - then) / nsPerUpdate;
			then = now;

			while(unprocessed >=1 ) 
			{
				updates++;
				update();
				unprocessed--;//done with update so subtract
				shouldRender = true;
				 
			}

			if(shouldRender) // we want to lock the frames per second at 60
			{
				fps++;
				render();
				shouldRender = false;// when your done rendering you can set to false
			}
			else 
			{
				try 
				{
					Thread.sleep(1);// if your not rendering then the thread needs to sleep
				}
				catch(Exception e) 
				{
					e.printStackTrace();// if we have any errors we want to print it out
				}
			}
		}

		if(System.currentTimeMillis() - fpsTimer > 1000) // the fps timer is used to debug
		{
			//prints out if the current update timer is greater than 60 seconds
			System.out.printf("%d fps %d updates", fps, updates);

			// these are now updated back to zero and the timer is reset to 1000
			fps = 0;
			updates = 0;
			fpsTimer += 1000;
		}
		
	}
	
	/*
	 * This starts the Thread by making running true.
	 * The method is synchronized because we need the 
	 * entire method to be called. 
	 */
	
	public synchronized void start()
	{
		if(running){return;}
		running = true;
	
		game =new Thread(this,"game");// A Thread is the execution of a program. It gets everything going.
		game.start();
		}
	
		public synchronized void stop()
			{
			if(!running){ return;}
			
			running=false;System.exit(0);
	}


	@Override
	public void keyTyped(KeyEvent e) {Keyboard.keyPressed(e);}
	@Override
	public void keyPressed(KeyEvent e) {Keyboard.keyPressed(e);}

	@Override
	public void keyReleased(KeyEvent e) {Keyboard.keyReleased(e);}
	
	
	
}
