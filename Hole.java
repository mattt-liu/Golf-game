// class for the hole in each level

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;

class Hole {
	
	public static final double size = 30;
	
	public static double x = 0;
	public static double y = 0;
	public static double w = size/2;
	public static double h = size/2;
	
	public static boolean contains(double px, double py) {
		if (x-w < px && px < x+w && y-h < py && py < y+h) { // inside boundary of hole
			if (Math.hypot(px - x, py - y) < size/2) {  // inside radius of hole
				return true;
			}
		}
		return false;
	}
	
	public static int[] getPos() {
		int[] out = new int[4];
		out[0] = (int)(x-w);
		out[1] = (int)(y-h);
		out[2] = (int)size;
		out[3] = (int)size;
		return out;
	}
}