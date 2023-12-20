package tetromino;

import java.awt.Color;
import java.awt.Graphics2D;

import game.KeyHandler;
import game.Manager;

public class Mino {
	
	public Block b[] = new Block[4];
	public Block tempB[] = new Block[4];
	int autoDropCounter = 0;
	public int direction = 1; //4 directions (1/2/3/4)
	
	boolean leftCollision, rightCollision, bottomCollision;
	public boolean active = true;
	
	public boolean deactivating;
	private int deactivateCounter = 0;
	
	public void create(Color c) {
		for(int i = 0; i < 4; i++) {
			b[i] = new Block(c);
		}
		
		for(int i = 0; i < 4; i++) {
			tempB[i] = new Block(c);
		}
		
		
//		b[0] = new Block(c);
//		b[1] = new Block(c);
//		b[2] = new Block(c);
//		b[3] = new Block(c);
//		tempB[0] = new Block(c);
//		tempB[1] = new Block(c);
//		tempB[2] = new Block(c);
//		tempB[3] = new Block(c);
	}
	
	/** Override these methods in child classes */
	public void setXY(int x, int y) {}
	public void getDirection1() {}
	public void getDirection2() {}
	public void getDirection3() {}
	public void getDirection4() {}
	
	public void updateXY(int direction) {
		
		checkRotationCollision();
		
		if(!leftCollision && !rightCollision && !bottomCollision) {
			this.direction = direction;
			for(int i = 0; i < b.length; i++) {
				 b[i].x = tempB[i].x;
				 b[i].y = tempB[i].y;
			}
		}
		
	}
	
	
	public void checkMovementCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;
		
		checkStaticBlockCollision();
		
		//Check frame collision
		for(int i = 0; i < b.length; i++) {
			// Left Wall
			if(b[i].x == Manager.leftX){
				leftCollision = true;
			}
			//Right Wall
			if(b[i].x + Block.SIZE == Manager.rightX){
				rightCollision = true;
			}
			//Bottom
			if(b[i].y + Block.SIZE == Manager.bottomY){
				bottomCollision = true;
			}
			
		}
		
		
		
		
	}
	public void checkRotationCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;
		
		checkStaticBlockCollision();
		
		//Check frame collision
		for(int i = 0; i < b.length; i++) {
			// Left Wall
			if(tempB[i].x < Manager.leftX){
				leftCollision = true;
			}
			//Right Wall
			if(tempB[i].x + Block.SIZE > Manager.rightX){
				rightCollision = true;
			}
			//Bottom
			if(tempB[i].y + Block.SIZE > Manager.bottomY){
				bottomCollision = true;
			}
			
		}
	}
	public void checkStaticBlockCollision() {
		for(int i = 0; i < Manager.staticBlocks.size(); i++) {
			Block block = Manager.staticBlocks.get(i);
			int targetX = block.x;
			int targetY = block.y;
			
			for(int j = 0; j < b.length; j++) {
				if(b[j].y + Block.SIZE == targetY && b[j].x == targetX) {
					bottomCollision = true;
				}
				if(b[j].x - Block.SIZE == targetX && b[j].y == targetY) {
					leftCollision = true;
				}
				if(b[j].x + Block.SIZE == targetX && b[j].y == targetY) {
					rightCollision = true;
				}
			}
		}
	}
	public void update() {
		
		if(deactivating) {
			deactivate();
		}

		if (KeyHandler.upPressed) {
			switch (direction) {
			case 1:
				getDirection2();
				break;
			case 2:
				getDirection3();
				break;
			case 3:
				getDirection4();
				break;
			case 4:
				getDirection1();
				break;
			}
			KeyHandler.upPressed = false;

		}

		checkMovementCollision();

		if (KeyHandler.downPressed) {
			if (!bottomCollision) {
				for (int i = 0; i < b.length; i++) {
					b[i].y += Block.SIZE;
				}
				// When moved down, reset the autoDropCounter
				autoDropCounter = 0;
			}
			KeyHandler.downPressed = false;
		}

		if (KeyHandler.leftPressed) {
			if (!leftCollision) {
				for (int i = 0; i < b.length; i++) {
					b[i].x -= Block.SIZE;
				}
			}
			KeyHandler.leftPressed = false;
		}

		if (KeyHandler.rightPressed) {
			if (!rightCollision) {
				for (int i = 0; i < b.length; i++) {
					b[i].x += Block.SIZE;
				}
				KeyHandler.rightPressed = false;
			}
		}

		if (bottomCollision) {
			deactivating = true;
		} else {
		}
		autoDropCounter++; // increases every frame
		if (autoDropCounter == Manager.dropInterval) {
			for (int i = 0; i < b.length; i++) {
				b[i].y += Block.SIZE;
			}
			autoDropCounter = 0;
		}
	}

	
	public void draw(Graphics2D g2) {
		int margin = 2;
		g2.setColor(b[0].c);
		for(int i = 0; i < b.length; i++) {
			g2.fillRect(b[i].x + margin,  b[i].y + margin, Block.SIZE - (margin*2), Block.SIZE - (margin*2));
		}
//		g2.fillRect(b[0].x + margin,  b[0].y + margin, Block.SIZE - (margin*2), Block.SIZE - (margin*2));
//		g2.fillRect(b[1].x + margin,  b[1].y + margin, Block.SIZE - (margin*2), Block.SIZE - (margin*2));
//		g2.fillRect(b[2].x + margin,  b[2].y + margin, Block.SIZE - (margin*2), Block.SIZE - (margin*2));
//		g2.fillRect(b[3].x + margin,  b[3].y + margin, Block.SIZE - (margin*2), Block.SIZE - (margin*2));
	}
	
	/** provides for the sliding movement in tetris */
	public void deactivate() {
		deactivateCounter++;
		
		//Wait 45 frames to deactivate
		if(deactivateCounter == 45) {
			deactivateCounter = 0;
			checkMovementCollision();//check if bottom is colliding
			
			if(bottomCollision) {
				active = false;
			}
			
		}
	}

}
