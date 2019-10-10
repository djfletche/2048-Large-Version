import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

/*
 * This is the master class
 * this the class that decides everything that goes on in the rest of the program
 * 
 */

public class GameBoard {
	
	public static final int ROWS = 6;
	public static final int COLS = 6;
	
	private final int startingTiles = 2;
	private Tile[][] board;//keeps track of where the items are on the board.
	
	// the board shows the background colors and the final game board shows both board together
	private BufferedImage gameBoard;
	private BufferedImage finalBoard;
	private int x;
	private int y;
	private boolean dead;
	//Spacing is the amount of space in between the items on the board including the items and other squares
	private static int SPACING = 10;

	/*
	 * Get this width of the board and is them multiplied by the spacing
	 * Adding the rows plus one add a pixel then you multiply by the spacing 
	 */
	public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Tile.WIDTH;
	public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Tile.HEIGHT;
	
	private boolean hasStarted;

	public GameBoard(int x, int y) 
	{
		this.x = x; this.y = y;
		board = new Tile[ROWS][COLS];
		
		gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		createBoardImage();start();
	}

	/*
	 * Draw to the game board using the 16 rectangles while you decide the background 
	 * and boarder colors. you need the graphics element to import the actual image of the board
	 * you have to fill the rectangle to basically color in the shapes
	 */
	private void createBoardImage() 
	{
		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		g.setColor(Color.lightGray);
		
		for(int row = 0; row < ROWS; row++) 
		{
			for(int col = 0; col < COLS; col++)
			{
				// your dictating the points at which your going to draw on.
				int x = SPACING + SPACING * col + Tile.WIDTH * col;
				int y = SPACING + SPACING * row + Tile.HEIGHT * row;
				
				// Afterward you have to use the fill method to color in the shapes
				g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
			}
		}
	}
	/*
	 * These next two functions work together to choose where the locations are going to be
	 * The locations are random and they are used to decide where the new tiles will be placed
	 */
	private void start() 
	{
		for(int i = 0; i < startingTiles; i++) 
		{
			spawnRandom();
		}
	}
	
	private void spawnRandom() 
	{
		Random random = new Random();
		boolean notValid = true;
		
		while(notValid) 
		{
			//Creating the random locations of the rows and COlS.
			int location = random.nextInt(ROWS * COLS);
			int row = location / ROWS;// division
			int col = location % COLS;//modulate 
			//random number generated gets two different response numbers rather than using tow random ints.
			Tile current = board[row][col];
			// this is used to choose what the number will be
			
			if(current == null) 
			{
				int value = random.nextInt(10) < 9 ? 2 : 4;  //this dictates that the likely hood of a new tile is

				Tile tile = new Tile(value, getTileX(col), getTileY(row));
				board[row][col] = tile;
				notValid = false;
			}
		}
	}
	
	/*
	 * You need to return where the position of each x would be
	 * This is like deciding how wide your board is. You need to indent by the spacing plus the
	 * tile itself
	 */
	public int getTileX(int col) {return SPACING + col * Tile.WIDTH + col * SPACING;}
	
	public int getTileY(int row) {return SPACING + row * Tile.HEIGHT + row * SPACING;}
	
	/*
	 * You need to draw the tiles here using a double for loop you cycle through all of the tiles
	 */
	public void render(Graphics2D g) 
	{
		Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
		g2d.drawImage(gameBoard, 0, 0, null);
		
		for(int row = 0; row < ROWS; row++) 
		{
			for(int col = 0; col < COLS; col++) 
			{
				Tile current = board[row][col];
				if(current == null) continue;//if the current tile is null you dont need to render so you continue
				current.render(g2d);
			}
		}
		
		g.drawImage(finalBoard, x, y, null);
		g2d.dispose();
	}
	 /*
	  * updating the position of the tiles which will be used to determine the position
	  */
	public void update()
	
	{
		checkKeys();
		
		for(int row = 0; row < ROWS; row++) 
		{
			for(int col = 0; col < COLS; col++) 
			{
				Tile current = board[row][col];
				if(current == null) continue;
				current.update();
				// you need to reset the position after you update.
				resetPosition(current, row, col);
			}
		}
	}
	
	private void resetPosition(Tile current,int row, int col) 
	
	{
		if(current == null) return;// if the current is null then you cant move it
		
		int x = getTileX(col);
		int y = getTileY(row);
		
		// set the coordinates
		
		// the distance is what is between the two space
		int distX = current.getX() - x;
		int distY = current.getY() - y;
		
		// if the value of the distance is less than the slide speed then you only move how much you need
		// if there are 10 pixels left but the slide speed is less than only move how much you need.
		if(Math.abs(distX) < Tile.SLIDE_SPEED) {current.setX(current.getX() - distX);}
		
		if(Math.abs(distY) < Tile.SLIDE_SPEED) {current.setY(current.getY() - distY);}
			
		if(distX < 0) {current.setX(current.getX() + Tile.SLIDE_SPEED);}
		
		if(distY < 0) {current.setY(current.getY() + Tile.SLIDE_SPEED);}
		
		if(distX > 0) {current.setX(current.getX() - Tile.SLIDE_SPEED);}
		
		if(distY > 0) {current.setY(current.getY() - Tile.SLIDE_SPEED);}
	}
	
	private boolean move(int row, int col, int horizontalDirection, int verticalDirection, Direction dir) 
	{
		boolean canMove = false;//needs to return a boolean
		Tile current = board[row][col];//capture the current tile
		
		if(current == null) return false; // if the current tile can't be moved its automatically placed as false
		boolean move = true;
		
		// the new columns and rows are set to the positions
		int newCol = col;
		int newRow = row;
		
		/*
		 * while the tile is able to move without touching another 
		 * block or touching the edges of the board its going to check
		 */
		while(move) 
		{
			newCol += horizontalDirection;
			newRow += verticalDirection;
			
			if(checkOutOfBounds(dir, newRow, newCol)) break; // if the block being moved goes out of bounds we break the loop
			
			if(board[newRow][newCol] == null) // if the loop isnt broken then we need to keep checking
			
			{
				/*
				 *  if the new column is null then you can move to that position.
				 *  when you move you set the old tile to null
				 *  the slideTo allows you to make the move
				 */
		
				board[newRow][newCol] = current;
				board[newRow - verticalDirection][newCol - horizontalDirection] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
				canMove = true;
			}
			/*
			 * if the current tile value is the same on the board then you can combine them
			 * but only one time so if you have multiple of the same values you cannon do double combinations
			 */
			else if(board[newRow][newCol].getValue() == current.getValue() && board[newRow][newCol].CanCombine()) 
			{
				board[newRow][newCol].setCanCombine(false);//cannot combine twice
				
				board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2);// set the value to double
				
				canMove = true;
				// if you are able to move then you will slide to a new position
				board[newRow - verticalDirection][newCol - horizontalDirection] = null;//this changes the old position to null making it available
				board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
			}
			else {
				move = false;
			}
		}
		return canMove;
	}
	/**
	 * Checking to see if the move is possible.
	 * checks for each direction
	 * if the col is less than 0 towards the left its out 
	 * vice versa for right.
	 * @param dir
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean checkOutOfBounds(Direction dir, int row, int col) 
	
	{
		if(dir == Direction.LEFT) {return col < 0;}
		
		else if(dir == Direction.RIGHT){return col > COLS - 1;}
		
		else if(dir == Direction.UP){return row < 0;}
		
		else if(dir == Direction.DOWN){return row > ROWS - 1;}
		
		return false;// if none of the directions are caught then we return false.
		
	}

	/*
	 * This is going to handle moving the tiles
	 */
	private void moveTiles(Direction dir) {
		
		// spawn only to tiles that can move and you need to use the direction to dictate where you can move
		boolean canMove = false;
		int horizontalDirection = 0;
		int verticalDirection = 0;
		
		/*
		 * This function is repeated 4 times
		 * if you can move this function will be true the entire time
		 * 
		 * if the last tile can't move its false
		 * if any tile can move then this will be true.
		 */
		if(dir == Direction.LEFT) 
		{
			horizontalDirection = -1;//move left, no vertical changes
			for(int row = 0; row < ROWS; row++) 
			{
				for(int col = 0; col < COLS; col++) 
				{
					if(!canMove) 
					{
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					}
					else move(row, col, horizontalDirection, verticalDirection, dir);
				}
			}
		}
		
		else if(dir == Direction.RIGHT) 
		{
			horizontalDirection = 1;// move right
			
			for(int row = 0; row < ROWS; row++)
			{
				for(int col = COLS - 1 ; col >= 0; col--) 
				{
					if(!canMove) 
					{
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					}
					else move(row, col, horizontalDirection, verticalDirection, dir);
				}
			}
		}
		
		else if(dir == Direction.UP) 
		{
			verticalDirection = -1;
			for(int row = 0; row < ROWS; row++) 
			{
				for(int col = 0; col < COLS; col++) 
				{
					if(!canMove) 
					{
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					}
					else move(row, col, horizontalDirection, verticalDirection, dir);
				}
			}
		}
		
		else if(dir == Direction.DOWN) 
		{
			verticalDirection = 1;
			for(int row = ROWS - 1; row >=0 ; row--) 
			{
				for(int col = 0; col < COLS; col++) 
				{
					if(!canMove) {
						
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					}
					else move(row, col, horizontalDirection, verticalDirection, dir);
				}
			}
		}
		else 
		{
			System.out.println(dir + " is not a valid direction.");
		}
		/**
		 * going through all the tiles
		 */
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				Tile current = board[row][col];
				if(current == null) continue;
				current.setCanCombine(true); 
			}
		}
		
		if(canMove) {
			spawnRandom();
			checkDead();
		}
	}
	
	/*
	 * Check through each tile to see if there is a space
	 * If the tile is returned as null then there is room
	 * 
	 */
	private void checkDead() 
	{
		for(int row = 0; row < ROWS; row++) 
		{
			for(int col = 0; col < COLS; col++) 
			{
				if(board[row][col] == null) return;//Open space that is returned
				
				if(checkSurroudingTiles(row, col, board[row][col])) 
				{
					return;
				}
			}
		}
		dead = true;
		//setHighScore(score);
	}
	/*
	 * one function that is repeated
	 * we are checking for out of bounds exceptions
	 * 
	 */
	private boolean checkSurroudingTiles(int row, int col, Tile current) 
	{
		if(row > 0) {
			// if the row is 0 do not check it off that would create a out of bounds exception
			Tile check = board[row - 1][col];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true; // if the values are the same then they are true
		}
		if(row < ROWS - 1) 
		{
			Tile check = board[row + 1][col];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		if(col > 0) 
		{
			Tile check = board[row][col - 1];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		if(col < COLS - 1) 
		{
			Tile check = board[row][col + 1];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		return false;
	}
	
	private void checkKeys() 
	{
		
		if(Keyboard.typed(KeyEvent.VK_LEFT)) {moveTiles(Direction.LEFT);if(!hasStarted) hasStarted = true;}
		
		if(Keyboard.typed(KeyEvent.VK_RIGHT)) {moveTiles(Direction.RIGHT);if(!hasStarted) hasStarted = true;}
		
		if(Keyboard.typed(KeyEvent.VK_UP)) {moveTiles(Direction.UP);if(!hasStarted) hasStarted = true;}
		
		if(Keyboard.typed(KeyEvent.VK_DOWN)) { moveTiles(Direction.DOWN); if(!hasStarted) hasStarted = true;}
	}
}
