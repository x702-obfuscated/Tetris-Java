package game;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import tetromino.*;


public class Manager {
	
	//Main Play Area
	private final int WIDTH = 360;
	private final int HEIGHT = 600;
	
	public static int leftX;
	public static int rightX;
	
	public static int topY;
	public static int bottomY;
	
	//Mino
	Mino currentMino;
	final int MINO_START_X;
	final int MINO_START_Y;
	
	//Next Mino
	Mino nextMino;
	final int NEXTMINO_X;
	final int NEXTMINO_Y;
	public static ArrayList<Block> staticBlocks = new ArrayList<>();
	
	//effects
	boolean effectCounterOn;
	int effectCounter;
	ArrayList<Integer> effectY = new ArrayList<>();
	
	//score
	int level = 1;
	int lines = 0;
	int score = 0;
	
	
	//Others
	public static int dropInterval = 60; //mino drops in every 60 frames
	boolean gameOver;
	
	public Manager(){
		//Main Play Area Frame
		leftX = (GamePanel.WIDTH/2)-(WIDTH/2);
		rightX = leftX + WIDTH;
		topY = 50;
		bottomY = topY + HEIGHT;
		
		MINO_START_X = leftX + (WIDTH/2) - Block.SIZE;
		MINO_START_Y = topY + Block.SIZE;
		
		NEXTMINO_X = rightX + 175;
		NEXTMINO_Y = topY + 500;
		
		// Set the starting Mino
		currentMino = pickMino();
		currentMino.setXY(MINO_START_X,MINO_START_Y);
		
		nextMino = pickMino();
		nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
	}
	
	public void update() {
		
		if(!currentMino.active) {
			for(int i = 0; i < currentMino.b.length; i++) {
				staticBlocks.add(currentMino.b[i]);
			}
			
			//check if game is over
			if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
				//current mino is colliding immediately
				gameOver = true;
			}
			
			currentMino.deactivating = false;
			
			currentMino = nextMino;
			currentMino.setXY(MINO_START_X,MINO_START_Y);
			
			nextMino = pickMino();
			nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
			
			checkDelete();
		}
		else {
			currentMino.update();	
		}
	}
	
	private void checkDelete() {
		int x = leftX;
		int y = topY;
		int blockCount = 0;
		int lineCount = 0;
		
		while(x < rightX && y < bottomY) {
			
			for(int i = 0; i < staticBlocks.size(); i++) {
				if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
					blockCount++;
				}
			}
			
			x += Block.SIZE;
			if(x == rightX) {
				//Check if row is full
				if(blockCount == 12) {
					
					effectCounterOn = true;
					effectY.add(y);
					
					//remove all the blocks in the current y line
					for(int i = staticBlocks.size()-1; i > -1; i--) {
						if(staticBlocks.get(i).y == y) {
							staticBlocks.remove(i);
						}
					}
					
					lineCount++;
					lines++;
					
					//Drop Speed
					//if line score hits certain number or greater, increase the drop speed
					// 1 is the fastest
					if(lines % 10 == 10 && dropInterval > 1) {
						dropInterval -= 10;
					}
					else {
						dropInterval -= 1;
					}
					
					//Shift Down
					for(int i = 0; i < staticBlocks.size(); i++) {
						if(staticBlocks.get(i).y < y) {
							staticBlocks.get(i).y += Block.SIZE;
						}
					}
				}
				
				blockCount = 0;
				x = leftX;
				y+= Block.SIZE;
			}
			
		}
		//Add Score 
		if(lineCount > 0) {
			int singleLineScore = 10 * level;
			score += singleLineScore * lineCount;
		}
	}
	
	public void draw(Graphics2D g2) {
		//Draw Play Area Frame
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(4f));
		g2.drawRect(leftX - 4, topY - 4, WIDTH + 8, HEIGHT + 8);
		
		//Draw Next Mino Frame
		int x = rightX + 100;
		int y = bottomY - 200;
		g2.drawRect(x,y,200,200);
		g2.setFont(new Font("Arial", Font.PLAIN, 30));
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.drawString("NEXT", x + 60, y + 60);
		
		// Draw the currentMino
		if(currentMino != null) {
			currentMino.draw(g2);
		}
		
		//Draw Next Mino
		nextMino.draw(g2);
		
		//Draw Static Blocks
		for(int i = 0; i < staticBlocks.size(); i++) {
			staticBlocks.get(i).draw(g2);
		}
		drawScoreFrame(x,y,g2);
		drawEffect(g2);
		gameOver(g2);
		drawPause(g2);
		drawTitle(g2);
	}
	
	public void drawTitle(Graphics2D g2) {
		int x = 35;
		int y = topY +320;
		
		g2.setColor(Color.green);
		g2.setFont(new Font("Times New Roman", Font.ITALIC, 60));
		g2.drawString("TETRIS", x + 20, y);
	}
	
	private Mino pickMino() {
		Mino mino = null;
		int i = new Random().nextInt(7);
		
		switch(i) {
		case 0: mino = new L1();break;
		case 1: mino = new L2();break;
		case 2: mino = new Square();break;
		case 3: mino = new Bar();break;
		case 4: mino = new T();break;
		case 5: mino = new Z1();break;
		case 6: mino = new Z2();break;
		}
		return mino;
		
	}
	
	public void drawScoreFrame(int x, int y, Graphics2D g2) {
		g2.setColor(Color.white);
		g2.drawRect(x, topY, 250,300);
		x += 40;
		y = topY + 90;
		g2.drawString("LEVEL: " + level,x,y);
		y+=70;
		g2.drawString("LINES: " + lines,x,y);
		y+=70;
		g2.drawString("SCORE: " + score,x,y);
	}
	
	public static void drawPause(Graphics2D g2) {
		g2.setColor(Color.orange);
		g2.setFont(g2.getFont().deriveFont(50f));
		if(KeyHandler.pausePressed) {
			int x = leftX + 70;
			int y = topY + 320;
			g2.drawString("PAUSED",x,y);
		}
		
	}
	
	public void drawEffect(Graphics2D g2) {
		if(effectCounterOn) {
			effectCounter++;
			g2.setColor(Color.red);
			for(int i = 0; i < effectY.size(); i++) {
				g2.fillRect(leftX,  effectY.get(i),  WIDTH,  Block.SIZE);
			}
			
			if(effectCounter == 20) {
				effectCounterOn = false;
				effectCounter = 0;
				effectY.clear();
			}
		}
	}
	
	public void gameOver(Graphics g2) {
		if(gameOver) {
			int x = leftX + 25;
			int y = topY + 320;
			g2.setColor(Color.red);
			g2.setFont(g2.getFont().deriveFont(50f));
			g2.drawString("GAME OVER", x, y);
		}
	}
	

}
