// Golf.java
// Matthew Liu
// June 15, 2018

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;

public class Golf extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener {

	javax.swing.Timer myTimer;
	Game game;
	
	static final int frameWidth = 1000, frameHeight = 700;
	static final int limitX = frameHeight-39, limitY = frameWidth - 10;
	
	static final double frameRate = 1000/15; // 66.7 fps
	
	public Golf() {

		super("Geo Golf");
		setSize(frameWidth, frameHeight);

		myTimer = new javax.swing.Timer((int)(1000/frameRate), this);
		myTimer.start();

		game = new Game();
		
		add(game);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (game != null) {
			game.refresh();
			game.repaint();
		}
	}
	
	public static void main(String[] args) {
		new Golf();
	}
	
	public void keyPressed(KeyEvent e) {
    	game.setKey(e.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent e) {
    	game.setKey(e.getKeyCode(), false);
    	if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
    		game.pause();
    	}
	}
	public void keyTyped(KeyEvent e) {
	}
	
	public void mouseClicked(MouseEvent e) {
		Game.mSelect = true;
	}
	public void mousePressed(MouseEvent e) {
		if (Game.setMP == false) {
			Game.mousePX = e.getX() - 8;
			Game.mousePY = e.getY() - 31;
			Game.setMP = true;
		}
		if (Game.mouseCX != Game.mousePX) {
			Game.mouseCX = Game.mousePX;
		}
		if (Game.mouseCY != Game.mousePY) {
			Game.mouseCY = Game.mousePY;
		}
		Game.mClick = true;
		Game.mRelease = false;
	}
	public void mouseReleased(MouseEvent e) {
		Game.mouseDragged = false;
		Game.mClick = false;
		Game.mRelease = true;
		Game.setMP = false;
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {
		Game.mouseDragged = true;
		Game.mouseCX = e.getX() - 8;
		Game.mouseCY = e.getY() - 31;
		Game.mouseX = e.getX() - 8;
		Game.mouseY = e.getY() - 31;
	}
	public void mouseMoved(MouseEvent e) {
		Game.mouseX = e.getX() - 8;
		Game.mouseY = e.getY() - 31;
	}
}