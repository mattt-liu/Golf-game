/* class for all moving rectangles
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;

class MRect {
	private int x;
	private int y;
	private int w;
	private int h;
	private int vx;
	private int vy;
	
	public MRect(Rectangle r, int vx, int vy) {
		x = r.x;
		y = r.y;
		w = r.width;
		h = r.height;
		this.vx = vx;
		this.vy = vy;
	}
	public void move() {
		x += vx;
		y += vy;
	}
	public Rectangle getBounds() {
		return new Rectangle(x, y, w, h);
	}
	public void checkCollisions(ArrayList<Rectangle> rects) {
		
		if (x < Game.limX) {
			x = Game.limX;
			vx *= -1;
		}
		if (x + w > Game.limX + Game.limW) {
			x = Game.limX + Game.limW - w;
			vx *= -1;
		}
		if (y < Game.limY) {
			y = Game.limY;
			vy *= -1;
		}
		if (y + h > Game.limY + Game.limH) {
			y = Game.limY + Game.limH - h;
			vy *= -1;
		}
		
		for (Rectangle r : rects) {
			if (r.intersects(getBounds())) {
				Line edge = Calculations.findEdge(new Point(x + w/2, y + h/2), r);
				if (edge.getAngle() == 0) {
					vy *= -1;
				}
				else {
					vx *= -1;
				}
				break;
			}
		}
	}
}