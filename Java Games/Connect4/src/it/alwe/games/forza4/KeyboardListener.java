package it.alwe.games.forza4;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
	private Forza4 main;
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getID() == KeyEvent.VK_1) {
			main.frame.getContentPane().add(main.lblCircle);
        }
		if (e.getID() == KeyEvent.VK_2) {
			
        }
		if (e.getID() == KeyEvent.VK_3) {
			
        }
		if (e.getID() == KeyEvent.VK_4) {
			
        }
		if (e.getID() == KeyEvent.VK_5) {
			
        }
		if (e.getID() == KeyEvent.VK_6) {
			
        }
		if (e.getID() == KeyEvent.VK_7) {
			
        }
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
