// Calculations.java
// Matthew Liu

import java.awt.*;
import java.util.*;

public class Calculations {
	
	public static Line findEdge(Point a, Polygon p) {
		/* returns the edge of a polygon that point 'a'
		 * lies on, or returns null
		 */
		
		// check each side of polygon
		for (Line line : getEdges(p)) {
			
			if (line.onLine(a)) {
				return line;
			}
		}
		
		return null;
	}
	public static Line findEdge(Point a, Rectangle r) {
		Point p1 = new Point(r.x, r.y);
		Point p2 = new Point(r.x + r.width, r.y);
		Point p3 = new Point(r.x + r.width, r.y + r.height);
		Point p4 = new Point(r.x, r.y + r.height);
		Line[] lines = {new Line(p1, p2), new Line(p2, p3), new Line(p3, p4), new Line(p4, p1)};
		ArrayList<Double> dists = new ArrayList<Double>();
		for (Line ln : lines) {
			dists.add(ln.dist(a));
		}
		double min = Double.POSITIVE_INFINITY;
		int index = 0;
		for (int i = 0; i < dists.size(); i ++) {
			double d = dists.get(i);
			if (d < min) {
				min = d;
				index = i;
			}
		}
		return lines[index];
	}	
	public static Line[] findEdges(Point a, Polygon p) {
		/* if point 'a' is a vertex returns the 2 edges
		 * of the polygon that it lies on
		 */
		
		Line[] out = new Line[2];
		int index = 0;
		
		// check each side of polygon
		for (Line ln : getEdges(p)) {
			
			if (ln.onLine(a)) {
				out[index] = ln;
				index ++;
			}
		}
		return out;
	}	
	public static Point findVertex(Point a, Polygon p) {
		/* finds the closest vertex of a polygon to 
		 * a point by calculating the distance to all
		 * vertices and returning the closest point
		 */
		
		// find lowest distance
		int min = Integer.MAX_VALUE;
		int index = -1; // index of the lowest value
		for (int i = 0; i < getVertices(p).size(); i ++) {
			Point point = getVertices(p).get(i);
			int val = (int)Math.round(Math.hypot(a.x - point.x, a.y - point.y));
			if (val < min) {
				min = val;
				index = i;
			}
		}
		return getVertices(p).get(index);
	}
	
	public static Line nextEdge(double x, double y, int len, double angle, Polygon p) {
		/* returns the next edge on a polygon
		 * that an object travelling on a path
		 * would intersect
		 */
		
		for (int i = 1; i <= len; i ++){
			
			double nx = x + (double)i * Math.cos(angle); // next coordinate along the line
			double ny = y + (double)i * Math.sin(angle);
			
			Point point = new Point((int)Math.round(nx),(int)Math.round(ny));
						
			if ( p.contains(point) ) {
				return nearEdge(point, p);
			}
		}
		return null;
	}
	
	public static Line nearEdge(Point a, Polygon p) {
		/* returns the closest edge of 
		 * a polygon to point a
		 */
		
		ArrayList<Double> dists = new ArrayList<Double>();
		// calculate distances
		for (Line ln : getEdges(p)) {
			double dist = ln.dist(a);
			dists.add(dist); }
		
		// find lowest distance
		double min = Double.POSITIVE_INFINITY;
		int index = 0;
		for (int i = 0; i < dists.size(); i ++) {
			double dist = dists.get(i);
			if (dist < min) {
				min = dist;
				index = i;
			}
		}
		return getEdges(p).get(index);
	}
	
	public static Line nearEdge(Point a, Rectangle r) {
		int x1 = r.x;
		int y1 = r.y;
		int x2 = r.x + r.width;
		int y2 = r.y + r.height;
		ArrayList<Line> edges = new ArrayList<Line>();
		edges.add(new Line(new Point(x1, y1), new Point(x1, y2)));
		edges.add(new Line(new Point(x1, y2), new Point(x2, y2)));
		edges.add(new Line(new Point(x2, y2), new Point(x2, y1)));
		edges.add(new Line(new Point(x2, y1), new Point(x1, y1)));
		
		ArrayList<Double> dists = new ArrayList<Double>();
		
		for (Line ln : edges) {
			double dist = ln.dist(a);
			dists.add(dist);
		}
		
		// find lowest distance
		double min = Double.POSITIVE_INFINITY;
		int index = 0;
		
		for (int i = 0; i < dists.size(); i ++) {
			double dist = dists.get(i);
			if (dist < min) {
				min = dist;
				index = i;
			}
		}
		return edges.get(index);
	}
	
	public static Point nearVertex(Point a, Polygon p) {
		/* returns the closest edge of 
		 * a polygon to point a
		 */
		
		// calculate distances
		ArrayList<Double> dists = new ArrayList<Double>();
		
		for (Point point : getVertices(p)) {
			double dist = Math.hypot(a.x - point.x, a.y - point.y);
			dists.add(dist);
		}
		// find lowest distance
		double min = Double.POSITIVE_INFINITY;
		int index = 0;
		
		for (int i = 0; i < dists.size(); i ++) {
			double dist = dists.get(i);
			if (dist < min) {
				min = dist;
				index = i;
			}
		}
		return getVertices(p).get(index);
	}
	
	public static Line edgeTouch(ArrayList<Point> points, Polygon p) {
		/* returns the edge with the most points lying on it
		 * from polygon p
		 */
		
		// create array filled with 0's
		int[] count = new int[getEdges(p).size()];
		for (int i = 0; i < count.length; i ++) { count[i] = 0; }
		
		// check each edge's intersections
		for (int i = 0; i < getEdges(p).size(); i ++) {
			
			Line ln = getEdges(p).get(i);
			// check for all intersections
			for (Point point : points) {
				int x = point.x;
				int y = point.y;
				if (ln.onLine(new Point(x,y))) {
					count[i] ++;
				}
			}
		}
		
		// find the highest number of intersections
		int max = Integer.MIN_VALUE;
		int index = 0;
		
		for (int i = 0; i < count.length; i ++) {
			if (count[i] > max) {
				index = i;
				max = count[i];
			}
		}
		return getEdges(p).get(index);
	}
	
	public static ArrayList<Line> getEdges(Polygon p) {
		/* returns all sides of a polygon as 
		 * Line classes
		 */
		
		ArrayList<Line> out = new ArrayList<Line>();
		
		// take in points of polygon
		int[] x = p.xpoints;
		int[] y = p.ypoints;
		int len = Math.min(x.length, y.length);
		Point[] points = new Point[len];
		for (int i = 0; i < len; i ++) {
			points[i] = new Point(x[i], y[i]); }
		
		// create each side of polygon
		for (int i = 0; i < len; i ++) {
			Point p1;
			Point p2;
			// if p1 is the last point, make p2 the first point
			if (i == len-1) {
				p1 = points[i];
				p2 = points[0]; }
			else {
				p1 = points[i];
				p2 = points[i+1]; }
			
			Line line = new Line(p1, p2);
			out.add(line);
		}
		return out;
	}	
	public static ArrayList<Point> getVertices(Polygon p) {
		/* return all points in a polygon as an
		 * arraylist
		 */
		
		// take in points of polygon
		int[] x = p.xpoints;
		int[] y = p.ypoints;
		int len = Math.min(x.length, y.length);
		
		ArrayList<Point> points = new ArrayList<Point>();
		
		for (int i = 0; i < len; i ++) {
			points.add(new Point(x[i], -1*y[i]));
		}
		return points;
	}
}