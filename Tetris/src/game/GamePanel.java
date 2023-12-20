package game;
import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable {
	
	private static final int FPS = 60;
	static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	
	private Thread gameThread;
	private Manager manager;
	
	GamePanel(){
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(Color.black);
		this.setLayout(null);
		
		this.addKeyListener(new KeyHandler());
		this.setFocusable(true);
		
		manager = new Manager();
	}
	
	public void launchGame() {
		gameThread = new Thread(this);
		gameThread.start();//calls run() automatically
	}

	@Override
	public void run() {
		
		//Game Loop
		double drawInterval = 1E9/FPS;//draw every 60th of a second
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
		
	}
	
	public void update() {
		
		if(!KeyHandler.pausePressed && !manager.gameOver ) {
			manager.update();
		}
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		manager.draw(g2);
	}

}
