/* handles all interactions of the ball class
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;

class Ball {
	
	public static final int size = 20;
	public static final double friction = 0.05;
	public static final double max = 10;
	
	public static double x, y;
	public static double direction;
	public static double velocity;
	public static double vx;
	public static double vy;
	
	public static void set(int px, int py) {
		x = (double)px;
		y = (double)py;
	}	
	public static void move() {
    	if (velocity <= 0) { 
    		velocity = 0;
    	}
    	else  {
    		x += vx;
    		y += vy;
    		velocity -= friction;
    		setVelocity(velocity, direction);
    	}
	}	
	public static void setVelocity(double v, double dir) {
		if (v > max) {
			velocity = max;
		}
		else if (v < 0) {
			velocity = 0;
		}
		else {
			velocity = v;
		}
		direction = dir;
		vx = velocity * Math.cos(direction);
		vy = velocity * Math.sin(direction);
	}	
	public static boolean isMoving() {
		if (velocity != 0) { return true; }
		return false;
	}	
	public static ArrayList<Point> getPoints() {
		
		ArrayList<Point> out = new ArrayList<Point>();
		
		Ellipse2D.Double ball = new Ellipse2D.Double(Ball.x - Ball.size/2, Ball.y - Ball.size/2, Ball.size, Ball.size);
		
		for (int i = -1*(size/2); i <= size/2; i ++) {
			for (int j = -1*(size/2); j <= size/2; j ++) {
				int a = (int)Math.round(x) + i;
				int b = (int)Math.round(y) + j;
				if (ball.contains(a, b)) {
					out.add(new Point(a, b));
				}
			}
		}
		return out;
	}
}