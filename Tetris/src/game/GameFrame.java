package game;

import javax.swing.*;


@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	
	GameFrame(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Tetris");
		this.setResizable(false);
		
		
		GamePanel panel = new GamePanel();
		this.add(panel);
		this.pack();//panel becomes the size of the window.
		
		//has to be below the pack()
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		panel.launchGame();
	}

}
